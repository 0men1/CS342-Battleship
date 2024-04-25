package controllers;
import gamebackend.Ship;
import javafx.util.Pair;
import messages.Message;
import gamebackend.Board;
import messages.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.foreign.MemorySegment;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

public class Server {
    private final Consumer<Serializable> callback;
    TheServer server;
    HashMap<Integer, Player> players = new HashMap<>();
    GameStatus gameStatus = GameStatus.Home;

    public Server(Consumer<Serializable> callback_) {
        callback = callback_;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread {
        public void run() {
            try (ServerSocket socket = new ServerSocket(5555)) {
                System.out.println("Server is waiting for a client");
                while (true) {
                    Player c = new Player(socket.accept(), players.size() + 1);
                    Message msg = new Message();
                    msg.createLogMessage("Client found!");
                    callback.accept(msg);
                    players.put(players.size() + 1, c);
                    c.start();
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.createLogMessage("Server socket did not launch");
                callback.accept(msg);
            }
        }
    }

    public void searchForMatch() {
        if (players.size() >= 2) {
            System.out.println(players.size());
            boolean matchFound = false;
            Player player1 = null;
            Player player2 = null;


            for (Player player: players.values()) {
                if (player.playerStatus == PlayerStatus.InQueue) {
                    if (player1 == null) {
                        player1 = player;
                    } else {
                        player2 = player;
                        matchFound = true;
                        break;
                    }
                }
            }

            if (matchFound) {
                player1.playerStatus = PlayerStatus.PlacingShips;
                player2.playerStatus = PlayerStatus.PlacingShips;
                Message m = new Message();
                m.msgType = MessageType.OpponentFound;
                try {
                    player1.out.writeObject(m);
                    player2.out.writeObject(m);
                    player1.opponent = player2;
                    player2.opponent = player1;
                    callback.accept(m);
                } catch (Exception e) {
                    Message newMsg = new Message();
                    callback.accept(newMsg.createLogMessage("There was an error informing players that a match was found"));
                }
            }
        }
    }

    public class Player extends Thread {
        private final Socket connection;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        int count;
        PlayerStatus playerStatus;
        Board gameBoard; // Init board when both players connect
        Player opponent;

        public Player(Socket socket, int count_) {
            this.connection = socket;
            this.count = count_;
            playerStatus = PlayerStatus.Home;
        }

        public void run() {
            try{
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                Message m = new Message();
                m.createLogMessage("Stream opening error with client #" + count + ": " + e.getMessage());
                callback.accept(m);
            }

            while (true) {
                try {
                    Message msg = (Message) in.readObject();
                    if (msg != null) {
                        Message m = new Message();
                        switch (msg.msgType) {
                            case GameStatusUpdate:
                                gameStatus = (GameStatus) msg.payload.get("Status");
                                m.createLogMessage("Game status changed to: " + gameStatus);
                                callback.accept(m);
                                break;

                            case Log:
                                callback.accept(m.createLogMessage("Log Message received: " + msg.payload.get("Content").toString()));
                                break;

                            case StartQueue:
                                try{
                                    playerStatus = PlayerStatus.InQueue;
                                    msg.payload.put("Request-Status", true);
                                    callback.accept(m.createLogMessage("Client #" + count + " is in queue"));
                                    out.writeObject(msg);
                                    searchForMatch();
                                } catch (Exception e) {
                                    msg.payload.put("Request-Status", false);
                                    out.writeObject(msg);
                                    callback.accept(m.createLogMessage("Client #" + count + " error going into queue"));
                                }
                                break;

                            case QueueAccept:
                                playerStatus = PlayerStatus.QueueAccepted;
                                if (opponent.playerStatus == PlayerStatus.QueueAccepted) {
                                    m.msgType = MessageType.SendToShipPlacement;
                                    out.writeObject(m);
                                    callback.accept(m);
                                }
                                break;

                            case ShipPlacement:
                                Ship ship = (Ship) msg.payload.get("Ship");
                                int x = (int) msg.payload.get("X");
                                int y = (int) msg.payload.get("Y");
                                boolean req_status = gameBoard.placeShip(ship, x, y);
                                msg.payload.put("Request-Status", req_status);
                                out.writeObject(msg);
                                callback.accept(m.createLogMessage("Ship at coords: " + x + ", " + y));
                                break;
                        }
                    }
                } catch (Exception e) {
                    Message m = new Message();
                    callback.accept(m.createLogMessage("Client #" + count + " closed socket"));
                    players.remove(count);
                    break;
                }
            }
        } // end of run
    } // end of client thread
} // end of server

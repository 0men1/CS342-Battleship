package controllers;
import messages.Message;
import gamebackend.Board;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

public class Server {
    private final Consumer<Serializable> callback;
    int count = 1;
    TheServer server;
    HashMap<Integer, Player> players = new HashMap<>();
    GameStatus gameStatus;

    public Server(Consumer<Serializable> callback_) {
        callback = callback_;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread {
        public void run() {
            try (ServerSocket socket = new ServerSocket(5555)) {
                System.out.println("Server is waiting for a client");
                while (players.size() != 2) {
                    Player c = new Player(socket.accept(), count);
                    Message msg = new Message();
                    msg.createLogMessage("Client found!");
                    callback.accept(msg);
                    players.put(count, c);
                    c.start();
                    count++;
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.createLogMessage("Server socket did not launch");
                callback.accept(msg);
            }
        }
    }

    public class Player extends Thread {
        private final Socket connection;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        int count;

        Board gameBoard = new Board();

        public Player(Socket socket, int count_) {
            this.connection = socket;
            this.count = count_;
        }

        public void run() {
            try{
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                Message m = new Message();
                m.createLogMessage("Connection error with client: " + e.getMessage());
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
                                m.createLogMessage("Log Message received: " + msg.payload.get("Content").toString());
                                callback.accept(m);
                                break;
                        }
                    }
                } catch (Exception e) {
                    Message m = new Message();
                    callback.accept(m.createLogMessage("Something wrong with client"));
                    break;
                }
            }
        } // end of run
    } // end of client thread
} // end of server

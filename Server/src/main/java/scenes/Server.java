package scenes;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    private Consumer<Serializable> callback;
    int count = 1;
    TheServer server;

    ArrayList<ClientThread> clients = new ArrayList<>();
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
                    ClientThread c = new ClientThread(socket.accept(), count);
                    Message msg = new Message();
                    msg.createLogMessage("Client found!");
                    callback.accept(msg);
                    clients.add(c);
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

    public class ClientThread extends Thread {
        private Socket connection;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        int count;
        public ClientThread(Socket socket, int count_) {
            this.connection = socket;
            this.count = count_;
        }

        public void run() {
            try{
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                callback.accept("Connection error with client: "  + e.getMessage());
            }

            while (true) {
                try {
                    Message msg = (Message) in.readObject();
                    if (msg != null) {
                        switch (msg.msgType) {
                            case Log:
                                Message m = new Message();
                                m.createLogMessage("Hi");
                                out.writeObject(m);
                                break;
                        }
                    }
                } catch (Exception e) {
                    Message m = new Message();
                    m.createLogMessage("Something wrong with client");
                    callback.accept(m);
                    break;
                }
            }
        } // end of run
    } // end of client thread
} // end of server

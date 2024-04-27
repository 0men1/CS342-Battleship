package network;

import messages.Message;
import messages.MessageType;
import scenes.BattleshipScene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

public class Client extends Thread {
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    private final Consumer<Serializable> callback;
    public HashMap<String, BattleshipScene> sceneMap;
    String currentScene;

    public Client(Consumer<Serializable> callback_){
        callback = callback_;
    }

    public void run() {
        try{
            socketClient = new Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
        } catch( Exception e) {
            System.out.println("Streams not open: " + e);
        }
        while (true) {
            try {
                Message msg = (Message) in.readObject();
                if (msg != null ) {
                    Message m = new Message();
                    switch(msg.msgType) {
                        case StartQueue:
                            callback.accept(msg);
                            break;
                        case OpponentFound:
                            callback.accept(msg);
                            break;
                        case SendToShipPlacement:
                            callback.accept(msg);
                            break;
                        case ShipPlacement:
                            callback.accept(msg);
                            break;
                        case StartGame:
                            callback.accept(msg);
                            break;
                    }
                }

            } catch (Exception e) {
                System.out.println("Error getting message");
            }
        }
    }

    public void sendToScene(String scene, Object content) {

    }

    public void send(Message msg_) {
        try {
            out.writeObject(msg_);
        } catch (Exception e) {
            Message m = new Message();
            callback.accept(m.createLogMessage("There was an error sending message: " + e));
        }
    }

    public void close() {
        try {
            if (!socketClient.isClosed()) {
                socketClient.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing socket");
        }
    }
}

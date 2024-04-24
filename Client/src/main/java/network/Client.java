package network;

import messages.Message;
import scenes.BattleshipScene;

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
    private Consumer<Serializable> callback;
    HashMap<String, BattleshipScene> sceneMap;
    String currentScene;

    public Client(Consumer<Serializable> callback_, HashMap<String, BattleshipScene> sceneMap_){
        callback = callback_;
        sceneMap = sceneMap_;
        currentScene = "Home";
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
                callback.accept(msg);
            } catch (Exception e) {
                System.out.println("Error getting message");
            }
        }
    }

    public void send(Message msg_) {
        try {
            out.writeObject(msg_);
        } catch (Exception e) {
            Message m = new Message();
            m.createLogMessage("There was an error sending message: " + e);
            callback.accept(m);
        }
    }
}

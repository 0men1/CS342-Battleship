package network;

import messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    private Consumer<Serializable> callback;
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

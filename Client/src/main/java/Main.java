import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

import scenes.BattleshipScene;
import scenes.Game;
import scenes.Home;
import scenes.Message;

public class Main extends Application {
    public class Client extends Thread {
        Socket socketClient;
        ObjectOutputStream out;
        ObjectInputStream in;
        private Consumer<Serializable> callback;
        Client(Consumer<Serializable> callback_){
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

    Client clientConnection;
    String currentScene;
    HashMap<String, BattleshipScene> all_scenes = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Launching Client");
        launch(args);
        System.out.println("Closing Client");
    }

    @Override
    public void start(Stage stage) {
        all_scenes.put("Home", new Home(stage, all_scenes));
        all_scenes.put("Game", new Game(stage, all_scenes));

        clientConnection = new Client(data -> {
            Platform.runLater(() -> {
                Message newMsg = (Message) data;
                switch (newMsg.msgType) {
                    case Log:
                        all_scenes.get(currentScene).handleMessage(newMsg);
                }
            });
        });

        clientConnection.start();

        try{
            all_scenes.get("Home").render();
            currentScene = "Home";
        } catch (Exception e){
            System.out.println("Error launching Client-Home scene");
        }
        stage.show();
    }

}

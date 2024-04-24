import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.HashMap;

import network.Client;
import scenes.*;
import messages.Message;

public class Main extends Application {
    Client clientConnection;
    String currentScene;
    HashMap<String, BattleshipScene> all_scenes = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Launching network.Client");
        launch(args);
        System.out.println("Closing network.Client");
    }

    @Override
    public void start(Stage stage) {

        clientConnection = new Client(data -> {
            Platform.runLater(() -> {
                Message newMsg = (Message) data;
                switch (newMsg.msgType) {
                    case Log:
                        all_scenes.get(currentScene).handleMessage(newMsg);
                }
            });
        }, all_scenes);

        all_scenes.put("Home", new Home(stage, clientConnection));
        all_scenes.put("InQueue", new InQueue(stage, clientConnection));
        all_scenes.put("PreGame", new PreGame(stage, clientConnection));
        all_scenes.put("Game", new Game(stage, clientConnection));

        clientConnection.start();


        try{
            all_scenes.get("Home").render();
            currentScene = "Home";
        } catch (Exception e){
            System.out.println("Error launching network.Client-Home scene");
        }
        stage.show();
    }

}

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.util.HashMap;
import network.Client;
import scenes.*;
import messages.Message;

public class Main extends Application {
    Client clientConnection;
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
                if (data instanceof Message) {
                    Message newMsg = (Message) data;
                    switch (newMsg.msgType) {
                        case StartQueue:
                            all_scenes.get("InQueue").render();
                            break;
                        case OpponentFound:
                            all_scenes.get("InQueue").handleMessage(newMsg);
                            break;
                        case SendToShipPlacement:
                            all_scenes.get("PreGame").render();
                            break;
                        case ShipPlacement:
                            all_scenes.get("PreGame").handleMessage(newMsg);
                            break;
                        case StartGame:
                            all_scenes.get("Game").render();
                            break;
                    }
                }
            });
        });


        clientConnection.start();

        all_scenes.put("Home", new Home(stage, clientConnection));
        all_scenes.put("InQueue", new InQueue(stage, clientConnection));
        all_scenes.put("PreGame", new PreGame(stage, clientConnection));
        all_scenes.put("Game", new Game(stage, clientConnection));

        clientConnection.sceneMap = all_scenes;

        try{
            all_scenes.get("Home").render();
        } catch (Exception e){
            System.out.println("Error launching Home scene");
        }

        stage.show();
    }

}

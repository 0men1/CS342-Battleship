import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import scenes.BattleshipScene;
import scenes.ServerHome;
import scenes.Message;
import scenes.Server;

import java.util.HashMap;

public class Main  extends Application {
    Server serverConnection;

    public static void main(String[] args) {
        System.out.println("Launching Server");
        launch(args);
        System.out.println("Closing Server");
    }


    // Make sure when you make a new scene you construct it, so it is added to map
    @Override
    public void start(Stage stage) {
        HashMap<String, BattleshipScene> all_scenes = new HashMap<>();
        ServerHome home_scene = new ServerHome(stage, all_scenes);

        serverConnection = new Server(data -> {
            Platform.runLater(() -> {
                Message msg = (Message) data;
                home_scene.handleMessage(msg);
            });
        });

        try {
            all_scenes.get("Home").render();
        } catch (Exception e) {
            System.out.println("Error launching Server-Home scene");
        }

        stage.show();
    }
}

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scenes.BattleshipScene;
import scenes.ServerHome;
import scenes.Message;
import scenes.Server;

import java.util.HashMap;
import java.util.Objects;

public class Main  extends Application {
    Server serverConnection;
    HashMap<String, BattleshipScene> all_scenes = new HashMap<>();
    String currentScene;
    public static void main(String[] args) {
        System.out.println("Launching Server");
        launch(args);
        System.out.println("Closing Server");
    }


    // Make sure when you make a new scene you construct it, so it is added to map
    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/Home.fxml")));
            Scene home_scene = new Scene(root, 500, 500);
            all_scenes.put("Home", new ServerHome(home_scene, stage, all_scenes));
        } catch(Exception e) {
            System.out.println("There was an error getting the scenes: " + e);
            System.exit(0);
        }

        serverConnection = new Server(data -> {
            Platform.runLater(() -> {
                Message msg = (Message) data;
                all_scenes.get(currentScene).handleMessage(msg);
            });
        });
        try {
            all_scenes.get("Home").render();
            currentScene = "Home";
        } catch (Exception e) {
            System.out.println("Error launching Server-Home scene");
        }

        stage.show();
    }
}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scenes.BattleshipScene;
import scenes.Home;

import java.util.HashMap;

public class Main  extends Application {
    public static void main(String[] args) {
        System.out.println("Launching Server");
        launch(args);
        System.out.println("Closing Server");
    }


    // Make sure when you make a new scene you construct it, so it is added to map
    @Override
    public void start(Stage stage) {
        HashMap<String, BattleshipScene> all_scenes = new HashMap<>();
        Home home_scene = new Home(stage, all_scenes);

        try {
            all_scenes.get("Home").render();
        } catch (Exception e) {
            System.out.println("Error launching Server-Home scene");
        }

        stage.show();
    }
}

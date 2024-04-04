import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;

import scenes.BattleshipScene;
import scenes.Game;
import scenes.Home;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("Launching Client");
        launch(args);
        System.out.println("Closing Client");
    }

    @Override
    public void start(Stage stage) {
        HashMap<String, BattleshipScene> all_scenes = new HashMap<>();
        Home home_scene = new Home(stage, all_scenes);
        Game game_scene = new Game(stage, all_scenes);

        try{
            all_scenes.get("Home").render();
        } catch (Exception e){
            System.out.println("Error launching Client-Home scene");
        }
        stage.show();
    }

}

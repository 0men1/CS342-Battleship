package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.HashMap;

public class Game implements BattleshipScene{
    Stage stage;
    BorderPane game_pane = new BorderPane();
    Button game_button = new Button("Game");
    HashMap<String, BattleshipScene> scenes = new HashMap<>();
    Scene game = new Scene(game_pane, 300, 300);


    public Game(Stage stage, HashMap<String, BattleshipScene> scenes) {
        this.stage = stage;
        this.scenes = scenes;
        create();
    }

    void create() {
        game_pane.setCenter(game_button);

        game_button.setOnAction(e -> {
            scenes.get("Home").render();
        });

        scenes.put("Game", this);
    }

    @Override
    public void render() {
        stage.setScene(game);
        stage.setTitle("Game");
    }
}

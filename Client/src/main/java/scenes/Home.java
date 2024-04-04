package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.HashMap;

public class Home implements BattleshipScene{
    Stage stage;
    BorderPane home_pane = new BorderPane();
    Button home_button = new Button("Home");
    HashMap<String, BattleshipScene> scenes = new HashMap<>();
    Scene home = new Scene(home_pane, 300, 300);


    public Home(Stage stage, HashMap<String, BattleshipScene> scenes) {
        this.stage = stage;
        this.scenes = scenes;
        create();
    }

    void create() {
        home_pane.setCenter(home_button);

        home_button.setOnAction(e -> {
            scenes.get("Game").render();
        });

        scenes.put("Home", this);
    }


    @Override
    public void render() {
        stage.setScene(home);
        stage.setTitle("Home");
    }
}

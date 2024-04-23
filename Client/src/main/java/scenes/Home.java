package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.HashMap;

public class Home implements BattleshipScene{
    Stage stage;
    BorderPane home_pane = new BorderPane();
    Button home_button = new Button("Home");
    HashMap<String, BattleshipScene> scenes;
    Scene home = new Scene(home_pane, 500, 500);
    ListView<String> list = new ListView<>();
    String curScene;


    public Home(Stage stage, HashMap<String, BattleshipScene> scenes) {
        this.stage = stage;
        this.scenes = scenes;
        create();
    }


    @Override
    public void handleMessage(Message msg) {
        list.getItems().add(msg.payload.get("Content").toString());
    }

    void create() {
        home_pane.setTop(list);
        home_pane.setCenter(home_button);
        home_button.setOnAction(e -> {
            scenes.get("Game").render();
        });
    }

    @Override
    public void render() {
        stage.setScene(home);
        stage.setTitle("Home");
    }
}

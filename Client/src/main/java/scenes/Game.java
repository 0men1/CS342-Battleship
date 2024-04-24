package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messages.Message;

import java.util.HashMap;

public class Game implements BattleshipScene{
    Stage stage;
    BorderPane game_pane = new BorderPane();
    Button game_button = new Button("Game");
    HashMap<String, BattleshipScene> scenes;
    Scene game = new Scene(game_pane, 500, 500);
    ListView<String> list = new ListView<>();

    public Game(Stage stage, HashMap<String, BattleshipScene> scenes) {
        this.stage = stage;
        this.scenes = scenes;
        create();
    }


    @Override
    public void handleMessage(Message msg) {
        list.getItems().add(msg.payload.get("Content").toString());

    }

    void create() {
        game_pane.setTop(list);
        game_pane.setCenter(game_button);

        game_button.setOnAction(e -> {
            scenes.get("Home").render();
        });
    }

    @Override
    public void render() {
        stage.setScene(game);
        stage.setTitle("Game");
    }
}

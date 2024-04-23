package scenes;

import com.sun.security.ntlm.Server;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.HashMap;

public class ServerHome implements BattleshipScene {
    Stage stage;
    BorderPane home_pane = new BorderPane();
    Button home_button = new Button("Home");
    HashMap<String, BattleshipScene> scenes = new HashMap<>();
    Scene home = new Scene(home_pane, 300, 300);
    ListView<String> updates = new ListView<>();

    Server serverConnection;

    public ServerHome(Stage stage, HashMap<String, BattleshipScene> scenes) {
        this.stage = stage;
        this.scenes = scenes;
        create();
    }


    public void handleMessage(Message msg) {
        updates.getItems().add(msg.payload.get("Content").toString());
    }

    void create() {
        home_pane.setTop(updates);
        home_pane.setCenter(home_button);
        Scene home = new Scene(home_pane, 300, 300);
        scenes.put("Home", this);
    }

    @Override
    public void render() {
        stage.setScene(home);
        stage.setTitle("Home");
    }
}

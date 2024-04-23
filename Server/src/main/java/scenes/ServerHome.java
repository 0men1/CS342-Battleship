package scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ServerHome implements BattleshipScene, Initializable {
    Stage stage;
    HashMap<String, BattleshipScene> scenes;
    Scene home;


    @FXML
    private ListView<String> updates;

    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }

    public ServerHome(Scene myScene, Stage stage, HashMap<String, BattleshipScene> scenes) {
        this.stage = stage;
        this.scenes = scenes;
        home = myScene;
    }


    @Override
    public void handleMessage(Message msg) {
        updates.getItems().add(msg.payload.get("Content").toString());
    }


    @Override
    public void render() {
        stage.setScene(home);
        stage.setTitle("Home");
    }

}
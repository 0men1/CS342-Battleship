package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messages.Message;
import network.Client;

import java.util.HashMap;

public class Game implements BattleshipScene{
    Stage stage;
    BorderPane game_pane = new BorderPane();
    Button game_button = new Button("Game");
    Scene game = new Scene(game_pane, 500, 500);
    ListView<String> list = new ListView<>();
    Client clientConnection;

    public Game(Stage stage, Client clientConnection_) {
        this.stage = stage;
        clientConnection = clientConnection_;
        initScene();
    }


    @Override
    public void handleMessage(Message msg) {
        list.getItems().add(msg.payload.get("Content").toString());

    }

    void initScene() {
        game_pane.setTop(list);
        game_pane.setCenter(game_button);
    }

    @Override
    public void render() {
        stage.setScene(game);
        stage.setTitle("Game");
    }
}

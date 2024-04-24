package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messages.Message;
import network.Client;

import java.util.HashMap;

public class Home implements BattleshipScene{
    Stage stage;
    BorderPane home_pane = new BorderPane();
    Button CoopButton = new Button("Co-Op");
    Button AIButton = new Button("AI");
    Scene home = new Scene(home_pane, 500, 500);
    ListView<String> list = new ListView<>();
    Client clientConnection;


    public Home(Stage stage, Client clientConnection_) {
        this.stage = stage;
        clientConnection = clientConnection_;
        create();
    }


    @Override
    public void handleMessage(Message msg) {
        list.getItems().add(msg.payload.get("Content").toString());
    }

    void create() {
        home_pane.setTop(list);
    }

    @Override
    public void render() {
        stage.setScene(home);
        stage.setTitle("Home");
    }
}

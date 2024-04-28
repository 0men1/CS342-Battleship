package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import messages.Message;
import messages.MessageType;
import network.Client;

import java.util.HashMap;

public class Home implements BattleshipScene{
    Stage stage;
    BorderPane home_pane = new BorderPane();
    Button CoopButton = new Button("Co-Op");
    Button AIButton = new Button("AI");
    HBox buttonsBox = new HBox(CoopButton, AIButton);
    Scene home;
    Label welcomeLabel = new Label("Welcome to battleship");
    VBox mainBox = new VBox(welcomeLabel, buttonsBox);
    Client clientConnection;
    HashMap<String, BattleshipScene> all_scenes = new HashMap<>();


    public Home(Stage stage, Client clientConnection) {
        this.stage = stage;
        this.clientConnection = clientConnection;
        initScene();
    }


    void initScene() {
        home_pane.setCenter(mainBox);

        CoopButton.setOnAction(e -> {
            sendStartQueueMessage();
        });

        AIButton.setOnAction(e -> {
            //TODO: Create AI
        });

        home = new Scene(home_pane, 200, 100);
    }

    public void sendStartQueueMessage() {
        Message queueMessage = new Message();
        queueMessage.msgType = MessageType.StartQueue;
        queueMessage.payload.put("Request-Status", false);
        clientConnection.send(queueMessage);
    }

    @Override
    public void handleMessage(Message msg) {
        //Never need prolly
    }


    @Override
    public void render() {
        stage.setScene(home);
        stage.setTitle("Home");
    }
}

package scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messages.Message;
import messages.MessageType;
import network.Client;
import sun.security.util.Debug;

import java.util.HashMap;

public class InQueue implements BattleshipScene{
    BorderPane queuePane = new BorderPane();
    Button acceptButton = new Button("Accept");
    Stage mainStage;
    Label inqueueLabel = new Label("In queue...");
    Scene inqueueScene;
    Client clientConnection;

    public InQueue(Stage stage_, Client clientConnection_) {
        mainStage = stage_;
        clientConnection = clientConnection_;
        initScene();
    }

    private void initScene() {
        queuePane.setTop(inqueueLabel);
        queuePane.setCenter(acceptButton);

        acceptButton.setDisable(true);

        acceptButton.setOnAction(e -> {
            Message newMsg = new Message();
            newMsg.msgType = MessageType.QueueAccept;
            acceptButton.setDisable(true);
            clientConnection.send(newMsg);
        });

        inqueueScene = new Scene(queuePane, 400, 200);
    }


    @Override
    public void render() {
        mainStage.setScene(inqueueScene);
        mainStage.setTitle("In Queue");
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.msgType) {
            case OpponentFound:
                inqueueLabel.setText("Match found!");
                acceptButton.setDisable(false);
        }
    }
}

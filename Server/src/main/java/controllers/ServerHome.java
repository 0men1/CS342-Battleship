package controllers;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messages.Message;
public class ServerHome {
    Stage stage;
    private final ListView<String> updatesList = new ListView<>();
    private final BorderPane pane = new BorderPane();
    Scene home = new Scene(pane, 500, 500);

    public ServerHome(Stage stage) {
        this.stage = stage;
        initLayout();

    }
    Server serverConnection = new Server(data -> {
        Platform.runLater(() -> {
            if (data instanceof Message) {
                Message m = (Message) data;
                switch (m.msgType) {
                    case Log:
                        updateLog(m.payload.get("Content").toString());
                    case GameStatusUpdate:
                        updateLog("Game status has been updated to: " + m.payload.get("Status"));
                }
            }
        });
    });

    void initLayout() {
        pane.setCenter(updatesList);
    }

    public void updateLog(String msg) {
        updatesList.getItems().add(msg);
    }
    public void render() {
        stage.setScene(home);
        stage.setTitle("Home");
    }
}
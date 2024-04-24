package scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messages.Message;

import java.util.HashMap;

public class InQueue implements BattleshipScene{
    BorderPane queuePane = new BorderPane();
    Button cancelButton = new Button("Cancel");
    HashMap<String, BattleshipScene> allScenes;
    Stage mainStage;
    Label inqueueLabel = new Label("In queue...");
    Scene inqueueScene;


    public InQueue(Stage stage_, HashMap<String, BattleshipScene> allScenes_) {
        mainStage = stage_;
        allScenes = allScenes_;
        initLayout();
    }

    private void initLayout() {
        queuePane.setTop(inqueueLabel);
        queuePane.setCenter(cancelButton);
        inqueueScene = new Scene(queuePane, 400, 200);
    }


    @Override
    public void render() {
        mainStage.setTitle("In Queue");
        mainStage.setScene(inqueueScene);
    }

    @Override
    public void handleMessage(Message msg) {

    }
}

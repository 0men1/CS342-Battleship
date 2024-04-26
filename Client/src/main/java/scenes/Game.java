package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messages.Message;
import network.Client;


public class Game implements BattleshipScene{
    Stage stage;
    BorderPane game_pane = new BorderPane();
    Label spotOnHand = new Label("Pointing at: (x, y)");
    Label logs = new Label("Update: ");
    Button fireButton = new Button("Fire");


    Scene game = new Scene(game_pane, 500, 500);
    Client clientConnection;

    public Game(Stage stage, Client clientConnection_) {
        this.stage = stage;
        clientConnection = clientConnection_;
        initScene();
    }


    @Override
    public void handleMessage(Message msg) {
    }

    void initScene() {
    }

    @Override
    public void render() {
        stage.setScene(game);
        stage.setTitle("Game");
    }
}

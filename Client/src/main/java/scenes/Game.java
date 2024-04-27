package scenes;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import messages.Message;
import network.Client;



public class Game implements BattleshipScene{
    Stage stage;
    BorderPane game_pane = new BorderPane();
    Label spotOnHand = new Label("Pointing at: (x, y)");
    Label logs = new Label("Update: ");

    Label enemyLabel = new Label("Opponent grid:");

    Label allyLabel = new Label("Ally grid:");
    Button fireButton = new Button("Fire");
    Button[][] enemyCells = new Button[10][10];
    GridPane enemyGrid = new GridPane();
    VBox enemyBox = new VBox(10, enemyLabel, enemyGrid, spotOnHand, fireButton);

    Button[][] allyCells = new Button[10][10];
    GridPane allyGrid = new GridPane();
    VBox allyBox = new VBox(10, allyLabel, allyGrid, logs);


    Scene game = new Scene(game_pane, 700, 500);
    Client clientConnection;

    public Game(Stage stage, Client clientConnection_) {
        this.stage = stage;
        clientConnection = clientConnection_;
        initScene();
    }

    void initScene() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button allyCell = new Button("#");
                Button enemyCell = new Button("#");

                allyCells[i][j] = allyCell;
                enemyCells[i][j] = enemyCell;

                int finalI = i;
                int finalJ = j;

                enemyCell.setOnMouseClicked(e -> {
                    onCellClick(e, finalI, finalJ);
                });

                allyGrid.add(allyCell, j, i);
                enemyGrid.add(enemyCell, j, i);
            }
        }


        game_pane.setLeft(allyBox);
        game_pane.setRight(enemyBox);
    }


    void onCellClick(MouseEvent e, int x, int y) {

    }

    @Override
    public void handleMessage(Message msg) {
    }

    @Override
    public void render() {
        stage.setScene(game);
        stage.setTitle("Game");
    }
}

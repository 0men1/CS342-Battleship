package scenes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import messages.Message;
import javafx.scene.paint.Color;
import messages.MessageType;
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

    int fireX;
    int fireY;
    boolean coordSet = false;

    public Game(Stage stage, Client clientConnection_) {
        this.stage = stage;
        clientConnection = clientConnection_;
        initScene();
    }

    void initScene() {
        game_pane.setLeft(allyBox);
        game_pane.setRight(enemyBox);


        fireButton.setOnAction(e -> {
            handleFireAction();
        });

        fireButton.setDisable(true);
    }


    void handleFireAction() {
        Message fireMsg = new Message();
        fireMsg.msgType = MessageType.SendShot;
        fireMsg.payload.put("X", fireX);
        fireMsg.payload.put("Y", fireY);
        fireMsg.payload.put("Hit-Status", false);
        clientConnection.send(fireMsg);
    }


    void onCellClick(MouseEvent e, int x, int y) {
        spotOnHand.setText("Pointing at: " + "(" + x +", " + y + ")");
        fireX = x;
        fireY = y;
        coordSet = true;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.msgType) {
            case StartGame:
                boolean myTurn = (boolean) msg.payload.get("Turn");
                fireButton.setDisable(!myTurn);
                break;

            case SendShot:
                int sendX = (int) msg.payload.get("X");
                int sendY = (int) msg.payload.get("Y");
                boolean send_hit_status = (boolean) msg.payload.get("Hit-Status");
                if (send_hit_status) { // IF hits mark it on enemy ggriddd
                    enemyCells[sendX][sendY].setText("*");
                    enemyCells[sendX][sendY].setBackground(Background.fill(Color.RED));
                    enemyCells[sendX][sendY].setDisable(true);
                    logs.setText("Update: (" + sendX + ", " + sendY + ") was a hit!");
                } else {
                    enemyCells[sendX][sendY].setBackground(Background.fill(Color.BLUE));
                    enemyCells[sendX][sendY].setDisable(true);
                    fireButton.setDisable(true);
                    logs.setText("Update: (" + sendX + ", " + sendY + ") was a miss!");
                }
                break;

            case ReceiveShot:
                int recX = (int) msg.payload.get("X");
                int recY = (int) msg.payload.get("Y");
                boolean rec_hit_status = (boolean) msg.payload.get("Hit-Status");
                if (rec_hit_status) {
                    allyCells[recX][recY].setBackground(Background.fill(Color.RED));
                } else {
                    fireButton.setDisable(false);
                }
                break;

            case GridInfo:
                allyCells = (Button[][]) msg.payload.get("Grid");
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        Button allyCell = allyCells[i][j];
                        Button enemyCell = new Button("#");
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
                allyGrid.setDisable(true);
                break;
        }
    }

    @Override
    public void render() {
        stage.setScene(game);
        stage.setTitle("Game");
    }
}

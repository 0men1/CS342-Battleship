package scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import messages.Message;
import network.Client;

public class PreGame implements BattleshipScene{
    BorderPane PreGameBorderPane = new BorderPane();
    GridPane cellGrid = new GridPane();
    Label leftClickLabel = new Label("Use your left click to place a ship vertically");
    Label rightClickLabel = new Label("Use your right click to place a ship horizontally");
    Button[][] cells = new Button[10][10];
    Client clientConnection;
    Stage stage;
    Scene PreGameScene;

    public PreGame(Stage stage_, Client clientConnection_) {
        stage = stage_;
        clientConnection = clientConnection_;
    }


    private void initScene() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = new Button("#");

                int finalI = i;
                int finalJ = j;
                cell.setOnAction(e -> onCellClick(finalI, finalJ));

                cellGrid.add(cell, j, i);
            }
        }
        PreGameScene = new Scene(PreGameBorderPane, 500, 500);
    }


    private void onCellClick(int x_, int y_) {
        //sends a message to client which sends to server that the user has clicked the button at x, y
    }


    @Override
    public void render() {
        stage.setTitle("Pre Game");
        stage.setScene(PreGameScene);
    }

    @Override
    public void handleMessage(Message msg) {

    }



}

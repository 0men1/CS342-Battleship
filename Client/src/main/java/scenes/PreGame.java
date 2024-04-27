package scenes;

import gamebackend.Ship;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import messages.Message;
import messages.MessageType;
import network.Client;
import java.util.ArrayList;
import java.util.Stack;


public class PreGame implements BattleshipScene{
    BorderPane PreGameBorderPane = new BorderPane();
    GridPane cellGrid = new GridPane();
    Label leftClickLabel = new Label("Use your left click to place a ship vertically");
    Label rightClickLabel = new Label("Use your right click to place a ship horizontally");
    Label shipOnHand = new Label("Ship on hand: ");
    Label logs = new Label("logs will show up here");
    VBox labels = new VBox(leftClickLabel, rightClickLabel, shipOnHand, logs);
    Button[][] cells = new Button[10][10];
    Client clientConnection;
    Stage stage;
    Scene PreGameScene;
    Stack<Ship> ships;
    Button ship1 = new Button("Ship - 1");
    Button ship2 = new Button("Ship - 2");
    Button ship3 = new Button("Ship - 3");
    Button ship4 = new Button("Ship - 4");
    Button ship5 = new Button("Ship - 5");
    Button readyButton = new Button("Ready");
    Ship selectedShip = null;
    Button selectedButton = null;
    ArrayList<Integer> shipsPlaced = new ArrayList<>();
    VBox shipButtonsBox = new VBox(ship1, ship2, ship3, ship4, ship5, readyButton);


    public PreGame(Stage stage_, Client clientConnection_) {
        stage = stage_;
        clientConnection = clientConnection_;
        initScene();
    }


    private void initScene() {
        // Init ships stack
        for (int i = 1; i <= 5; i++) {
            ships = new Stack<>();
            ships.push(new Ship(true, i));
        }

        // Init cells of buttons
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = new Button("#");
                cells[i][j] = cell;

                int finalI = i;
                int finalJ = j;
                cell.setOnMouseClicked(e -> onCellClick(e, finalJ, finalI));

                cellGrid.add(cell, j, i);
            }
        }

        ship1.setOnAction(e -> {
            selectedShip = new Ship(true, 1);
            shipOnHand.setText("Ship on hand: 1");
            selectedButton = ship1;
        });
        ship2.setOnAction(e -> {
            selectedShip = new Ship(true, 2);
            shipOnHand.setText("Ship on hand: 2");
            selectedButton = ship2;
        });
        ship3.setOnAction(e -> {
            selectedShip = new Ship(true, 3);
            shipOnHand.setText("Ship on hand: 3");
            selectedButton = ship3;
        });
        ship4.setOnAction(e -> {
            selectedShip = new Ship(true, 4);
            shipOnHand.setText("Ship on hand: 4");
            selectedButton = ship4;
        });

        ship5.setOnAction(e -> {
            selectedShip = new Ship(true, 5);
            shipOnHand.setText("Ship on hand: 5");
            selectedButton = ship5;
        });

        readyButton.setDisable(true);

        readyButton.setOnAction(e -> {


            Message newMsg = new Message();
            newMsg.msgType = MessageType.DonePlacingShips;
            cellGrid.setDisable(true);
            readyButton.setDisable(true);
            clientConnection.send(newMsg);

            Message shipInfo = new Message();
            shipInfo.msgType = MessageType.GridInfo;
            shipInfo.payload.put("Grid", cells);
            clientConnection.callback.accept(shipInfo);
        });


        PreGameBorderPane.setTop(labels);
        PreGameBorderPane.setLeft(shipButtonsBox);
        PreGameBorderPane.setCenter(cellGrid);

        PreGameScene = new Scene(PreGameBorderPane, 500, 500);
    }

    private void onCellClick(MouseEvent e, int x_, int y_) {
        if (selectedShip != null) {
            logs.setText("");
            selectedShip.isVert = e.getButton() == MouseButton.PRIMARY;
            Message newMsg = new Message();
            clientConnection.send(newMsg.createShipPlacementMessage(selectedShip.size, selectedShip.isVert, x_, y_));
        } else {
            logs.setText("You need to pick a ship from the left");
        }
    }


    @Override
    public void render() {
        stage.setTitle("Pre Game");
        stage.setScene(PreGameScene);
    }

    @Override
    public void handleMessage(Message msg) {
        boolean request_status = (boolean) msg.payload.get("Request-Status");
        if (msg.msgType == MessageType.ShipPlacement && request_status) {
            int x = (int) msg.payload.get("X");
            int y = (int) msg.payload.get("Y");
            boolean isVert = (boolean) msg.payload.get("Vertical");
            int size = (int) msg.payload.get("Ship");
            placeShip(size, isVert, x, y);
            selectedButton.setDisable(true);
            selectedShip = null;
            shipOnHand.setText("Ship on hand: ");
        } else {
            logs.setText("Could not place that ship there");
        }
    }


    public void placeShip(int size, boolean isVert, int x, int y) {
        if (isVert) {
            for (int i = y; i < (size+y); i++) {
                cells[i][x].setText("*");
                cells[i][x].setBackground(Background.fill(Color.GREEN));
            }
        } else {
            for (int i = x; i < (size+x); i++) {
                cells[y][i].setText("*");
                cells[y][i].setBackground(Background.fill(Color.GREEN));
            }
        }

        shipsPlaced.add(size);

        if (shipsPlaced.size() == 5) {
            readyButton.setDisable(false);
        }
    }


}

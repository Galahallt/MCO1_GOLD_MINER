package sample;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import static javafx.scene.paint.Color.WHITE;

public class Menu
{
    public RadioButton rbIntRand;
    public RadioButton rbIntSmart;

    public TextField tfGrid;
    public TextField tfGold;

    public TextField tfPit;
    public TextField tfBeacon;

    public Button btnPitAdd;
    public Button btnBeaconAdd;

    public Button btnPitRem;
    public Button btnBeaconRem;

    public TextArea taPit;
    public TextArea taBeacon;

    public Button btnStart;

    public Button btnSize;
    public Button btnGold;

    public Button btnPitSet;
    public Button btnBeaconSet;

    // Scene builder
    public Scene buildMenu()
    {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.setVgap(8);
        grid.setHgap(10);

        // Background
        Image image = new Image("sample/Cave.jpg", true);
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );
        grid.setBackground(new Background(bgImage));

        //Intelligence level label
        Label lblInt = new Label("Intelligence Level:");
        lblInt.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblInt, 0, 0);
        GridPane.setHalignment(lblInt, HPos.CENTER);

        //Intelligence level radio button
        rbIntRand = new RadioButton("Random");
        rbIntRand.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(rbIntRand, 1, 0);

        rbIntSmart = new RadioButton("Smart");
        rbIntSmart.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(rbIntSmart, 2, 0);

        //Intelligence level toggle group
        final ToggleGroup tgInt = new ToggleGroup();
        rbIntRand.setToggleGroup(tgInt);
        rbIntRand.setSelected(true);
        rbIntSmart.setToggleGroup(tgInt);

        //Grid label
        Label lblGrid = new Label("Grid Size:");
        lblGrid.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblGrid, 0, 1);
        GridPane.setHalignment(lblGrid, HPos.CENTER);

        //Grid input
        tfGrid = new TextField();
        GridPane.setConstraints(tfGrid, 1, 1);
        tfGrid.setPromptText("pick a number from 8-64");

        // Set Grid button
        btnSize = new Button("Set Size");
        btnSize.setDisable(true);
        GridPane.setConstraints(btnSize, 2, 1);
        btnSize.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnSize.setTextFill(WHITE);

        //Pits label
        Label lblPit = new Label("Pits");
        lblPit.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblPit, 0, 3);
        GridPane.setHalignment(lblPit, HPos.CENTER);

        //Pits input
        tfPit = new TextField();
        GridPane.setConstraints(tfPit, 0, 4);
        tfPit.setPrefSize(25, 25);
        tfPit.setDisable(true);

        //Pit Add Coordinate Button
        btnPitAdd = new Button("Add Pit");
        GridPane.setConstraints(btnPitAdd, 0, 5);
        btnPitAdd.setDisable(true);
        btnPitAdd.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnPitAdd.setTextFill(WHITE);

        //Pit Remove Coordinate Button
        btnPitRem = new Button ("Remove Pit");
        GridPane.setConstraints(btnPitRem, 0, 5);
        GridPane.setHalignment(btnPitRem, HPos.RIGHT);
        btnPitRem.setDisable(true);
        btnPitRem.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnPitRem.setTextFill(WHITE);

        //Pit Coordinates Label
        Label lblPitXY = new Label("Pit Coordinates:");
        lblPitXY.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblPitXY, 0, 6);
        lblPitXY.setDisable(true);

        //Set pits button
        btnPitSet = new Button("Set Pits");
        GridPane.setConstraints(btnPitSet, 0, 6);
        GridPane.setHalignment(btnPitSet, HPos.RIGHT);
        btnPitSet.setDisable(true);
        btnPitSet.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnPitSet.setTextFill(WHITE);

        //Pit Text Area Coordinates
        taPit = new TextArea();
        taPit.setStyle("-fx-opacity: 1;");
        taPit.setDisable(true);
        taPit.setPrefSize(170, 800);
        ScrollPane pScroll = new ScrollPane(taPit);
        pScroll.setFitToWidth(true);
        pScroll.setVmax(800);
        grid.add(pScroll, 0, 7);

        //Beacon label
        Label lblBeacon = new Label("Beacon");
        lblBeacon.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblBeacon, 1, 3);
        GridPane.setHalignment(lblBeacon, HPos.CENTER);

        //Beacon input
        tfBeacon = new TextField();
        GridPane.setConstraints(tfBeacon, 1, 4);
        tfBeacon.setPrefSize(25, 25);
        tfBeacon.setDisable(true);

        //Beacon Add Coordinate Button
        btnBeaconAdd = new Button("Add Beacon");
        GridPane.setConstraints(btnBeaconAdd, 1, 5);
        btnBeaconAdd.setDisable(true);
        btnBeaconAdd.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnBeaconAdd.setTextFill(WHITE);

        //Beacon Remove Coordinate Button
        btnBeaconRem = new Button ("Remove Beacon");
        GridPane.setConstraints(btnBeaconRem, 1, 5);
        GridPane.setHalignment(btnBeaconRem, HPos.RIGHT);
        btnBeaconRem.setDisable(true);
        btnBeaconRem.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnBeaconRem.setTextFill(WHITE);

        //Beacon Coordinates Label
        Label lblBeaconXY = new Label("Beacon Coordinates:");
        lblBeaconXY.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblBeaconXY, 1, 6);
        lblBeaconXY.setDisable(true);

        //Set Beacons Button
        btnBeaconSet = new Button("Set Beacons");
        GridPane.setConstraints(btnBeaconSet, 1, 6);
        GridPane.setHalignment(btnBeaconSet, HPos.RIGHT);
        btnBeaconSet.setDisable(true);
        btnBeaconSet.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnBeaconSet.setTextFill(WHITE);

        //Beacon Text Area Coordinates
        taBeacon = new TextArea();
        taBeacon.setPrefSize(170, 800);
        taBeacon.setStyle("-fx-opacity: 1;");
        taBeacon.setDisable(true);
        ScrollPane bScroll = new ScrollPane(taBeacon);
        bScroll.setFitToWidth(true);
        bScroll.setVmax(800);
        grid.add(bScroll, 1, 7);

        //Gold Tile label
        Label lblGold = new Label("Gold Tile");
        lblGold.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblGold, 0, 2);
        GridPane.setHalignment(lblGold, HPos.CENTER);

        //Gold Tile input
        tfGold = new TextField();
        tfGold.setPrefSize(25, 25);
        GridPane.setConstraints(tfGold, 1, 2);
        tfGold.setDisable(true);

        //Set Gold button
        btnGold = new Button("Set Gold");
        GridPane.setConstraints(btnGold, 2, 2);
        btnGold.setDisable(true);
        btnGold.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnGold.setTextFill(WHITE);

        //Start Button
        btnStart = new Button("START!");
        GridPane.setConstraints(btnStart, 2, 7);
        btnStart.setPrefSize(150, 75);
        btnStart.setDisable(true);
        btnStart.setStyle("-fx-background-image: url('/sample/Wood.png'); -fx-background-position: center;");
        btnStart.setTextFill(WHITE);

        GridPane.setHalignment(btnStart, HPos.CENTER);

        grid.getChildren().addAll(lblGrid, tfGrid, btnSize, lblInt, rbIntRand,
                rbIntSmart, lblPit, tfPit, lblBeacon, tfBeacon,
                lblGold, tfGold, btnGold, btnStart, btnPitAdd, btnBeaconAdd,
                btnPitRem, btnBeaconRem, taBeacon, taPit, btnPitSet, btnBeaconSet,
                lblPitXY, lblBeaconXY);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d/grid.getColumnCount());

        for (int i = 0; i < grid.getColumnCount(); i ++)
            grid.getColumnConstraints().add(cc);

        Scene scene = new Scene(grid, 600, 500);
        grid.requestFocus();

        return scene;
    }

    // Allows events of listed objects to be handled
    public void setEventHandlers(Controller cont)
    {
        btnStart.setOnAction((EventHandler) cont);

        btnPitAdd.setOnAction((EventHandler) cont);
        btnPitRem.setOnAction((EventHandler) cont);

        btnBeaconAdd.setOnAction((EventHandler) cont);
        btnBeaconRem.setOnAction((EventHandler) cont);

        btnSize.setOnAction((EventHandler) cont);
        btnGold.setOnAction((EventHandler) cont);

        btnPitSet.setOnAction((EventHandler) cont);
        btnBeaconSet.setOnAction((EventHandler) cont);

        tfGrid.textProperty().addListener(cont);
        tfGold.textProperty().addListener(cont);
        tfPit.textProperty().addListener(cont);
    }
}
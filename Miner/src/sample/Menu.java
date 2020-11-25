package sample;

import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.awt.*;

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

    // Scene builder
    public Scene buildMenu()
    {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-image: url('sample/Cave.jpg');-fx-background-size: cover;");
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.setVgap(8);
        grid.setHgap(10);

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

        // Grid label
        Label lblGrid = new Label("Grid Size:");
        lblGrid.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblGrid, 0, 1);
        GridPane.setHalignment(lblGrid, HPos.CENTER);

        //Grid input
        tfGrid = new TextField();
        GridPane.setConstraints(tfGrid, 1, 1);
        tfGrid.setPromptText("pick a number from 8-64");

        //Pits label
        Label lblPit = new Label("Pits");
        lblPit.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblPit, 0, 2);
        GridPane.setHalignment(lblPit, HPos.CENTER);

        //Pits input
        tfPit = new TextField();
        GridPane.setConstraints(tfPit, 0, 3);
        tfPit.setPrefSize(25, 25);

        //Pit Add Coordinate Button
        btnPitAdd = new Button("Add Pit");
        GridPane.setConstraints(btnPitAdd, 0, 4);


        //Pit Remove Coordinate Button
        btnPitRem = new Button ("Remove Pit");
        GridPane.setConstraints(btnPitRem, 0, 4);
        GridPane.setHalignment(btnPitRem, HPos.RIGHT);

        //Pit Coordinates Label
        Label lblPitXY = new Label("Pit Coordinates:");
        lblPitXY.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblPitXY, 0, 5);
        lblPitXY.setDisable(true);

        //Pit Text Area Coordinates
        taPit = new TextArea();
        taPit.setDisable(true);
        taPit.setStyle("-fx-opacity: 1;");
        GridPane.setConstraints(taPit, 0, 6);

        //Beacon label
        Label lblBeacon = new Label("Beacon");
        lblBeacon.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblBeacon, 1, 2);
        GridPane.setHalignment(lblBeacon, HPos.CENTER);

        //Beacon input
        tfBeacon = new TextField();
        GridPane.setConstraints(tfBeacon, 1, 3);
        tfBeacon.setPrefSize(25, 25);

        //Beacon Add Coordinate Button
        btnBeaconAdd = new Button("Add Beacon");
        GridPane.setConstraints(btnBeaconAdd, 1, 4);

        //Beacon Remove Coordinate Button
        btnBeaconRem = new Button ("Remove Beacon");
        GridPane.setConstraints(btnBeaconRem, 1, 4);
        GridPane.setHalignment(btnBeaconRem, HPos.RIGHT);

        //Beacon Coordinates Label
        Label lblBeaconXY = new Label("Beacon Coordinates:");
        lblBeaconXY.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblBeaconXY, 1, 5);
        lblBeaconXY.setDisable(true);

        //Beacon Text Area Coordinates
        taBeacon = new TextArea();
        taBeacon.setDisable(true);
        taBeacon.setStyle("-fx-opacity: 1;");
        GridPane.setConstraints(taBeacon, 1, 6);

        //Gold Tile label
        Label lblGold = new Label("Gold Tile");
        lblGold.setStyle("-fx-text-fill: white;");
        GridPane.setConstraints(lblGold, 2, 2);
        GridPane.setHalignment(lblGold, HPos.CENTER);

        //Gold Tile input
        tfGold = new TextField();
        tfGold.setPrefSize(25, 25);
        GridPane.setConstraints(tfGold, 2, 3);


        //Start Button
        btnStart = new Button("START!");
        GridPane.setConstraints(btnStart, 2, 6);
        btnStart.setPrefSize(150, 75);
        btnStart.setDisable(true);
        GridPane.setHalignment(btnStart, HPos.CENTER);

        grid.getChildren().addAll(lblGrid, tfGrid, lblInt, rbIntRand,
                rbIntSmart, lblPit, tfPit, lblBeacon, tfBeacon,
                lblGold, tfGold, btnStart, btnPitAdd, btnBeaconAdd,
                btnPitRem, btnBeaconRem, taBeacon, taPit, lblPitXY,
                lblBeaconXY);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d/grid.getColumnCount());

        for (int i = 0; i < grid.getColumnCount(); i ++)
            grid.getColumnConstraints().add(cc);



        Scene scene = new Scene(grid, 600, 500);
        //remove focus from any textfield
        grid.requestFocus();
        //grid.setGridLinesVisible(true);


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

        tfGrid.textProperty().addListener(cont);
        tfGold.textProperty().addListener(cont);
        tfPit.textProperty().addListener(cont);
    }
}
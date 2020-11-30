package sample;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import static javafx.scene.paint.Color.BROWN;

public class Grid
{
    public ArrayList<Rectangle> boxes = new ArrayList<>();
    public ImageView miner = new ImageView("sample/Miner.png");

    public int move;
    public int rotation;

    GridPane grid = new GridPane();

    String name = "paolo";

    //Debug Miner moves
    Button btnUp = new Button("Up");
    Button btnDown = new Button("Down");
    Button btnLeft = new Button("Left");
    Button btnRight = new Button("Right");

    Button btnMove = new Button("Move");
    Button btnAuto = new Button("Auto");

    Label lblStats;


    // Scene builder
    public Scene buildGrid(int size, ArrayList<Point> pits, ArrayList<Point> beacons, Point goldPot)
    {
        //Debug code
        GridPane.setConstraints(btnUp, 0, 1);
        GridPane.setHalignment(btnUp, HPos.CENTER);

        GridPane.setConstraints(btnAuto, 0, 1);
        GridPane.setHalignment(btnAuto, HPos.RIGHT);

        GridPane.setConstraints(btnDown, 0, 2);
        GridPane.setHalignment(btnDown, HPos.CENTER);

        GridPane.setConstraints(btnLeft, 0, 2);
        GridPane.setHalignment(btnLeft, HPos.LEFT);

        GridPane.setConstraints(btnRight, 0, 2);
        GridPane.setHalignment(btnRight, HPos.RIGHT);

        GridPane.setConstraints(btnMove, 1, 1);
        GridPane.setHalignment(btnMove, HPos.CENTER);
        grid.getChildren().addAll(btnUp, btnDown, btnLeft, btnRight, btnMove, btnAuto);

        GridPane gridBoard = new GridPane();
        ScrollPane scroll = new ScrollPane(gridBoard);

        miner.setFitHeight(50);
        miner.setFitWidth(50);

        grid.add(scroll, 1, 0);

        grid.setPadding(new Insets(10, 10, 10, 20));

        grid.setVgap(8);
        grid.setHgap(10);

        gridBoard.setVgap(3);
        gridBoard.setHgap(5);
        gridBoard.setPadding(new Insets (10, 10, 10, 10));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
            {
                Rectangle box = new Rectangle(50, 50);
                box.setFill(BROWN);
                GridPane.setConstraints(box, i, j);
                gridBoard.getChildren().add(box);
                boxes.add(box);
            }
        }

        // add pits to grid
        for (int i = 0; i < pits.size(); i++) {
            ImageView pit = new ImageView("sample/Pit.png");
            pit.setFitWidth(50);
            pit.setFitHeight(50);
            GridPane.setConstraints(pit, (int) pits.get(i).getX(), (int) pits.get(i).getY());
            gridBoard.getChildren().add(pit);
        }

        // add beacons to grid
        for (int i = 0; i < beacons.size(); i++)
        {
            ImageView beacon = new ImageView("sample/Beacon.png");
            beacon.setFitWidth(50);
            beacon.setFitHeight(50);
            GridPane.setConstraints(beacon, (int) beacons.get(i).getX(), (int) beacons.get(i).getY());
            gridBoard.getChildren().add(beacon);
        }

        // add gold to grid
        ImageView gold = new ImageView("sample/Gold.png");
        gold.setFitHeight(50);
        gold.setFitWidth(50);
        GridPane.setConstraints(gold, (int) goldPot.getX(), (int) goldPot.getY());
        gridBoard.getChildren().add(gold);


        GridPane.setConstraints(miner, 0, 0);
        gridBoard.getChildren().add(miner);


        //Stats label
        lblStats = new Label("Stats:\nMoves: " + move + "\nRotations: " + rotation + "\n");
        lblStats.setStyle("-fx-border-width: 2; -fx-border-color: black");
        grid.add(lblStats, 0, 0);

        //for row 1
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(80);
        //for row 2
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(10);
        // for column 1
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        //for column 2
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        grid.getColumnConstraints().addAll(col1, col2);
        grid.getRowConstraints().addAll(row1, row2);
        //Debug Grid
        //grid.setGridLinesVisible(true);
        Scene scene = new Scene(grid, 700, 600);

        return scene;
    }

    public void updateStats()
    {
        lblStats.setText("Stats:\nMoves: " + move + "\nRotations: " + rotation + "\n");
    }

    public void up()
    {
        if (!((miner.getScaleX() == -1 && miner.getRotate() == 90) || (miner.getScaleX() == 1 && miner.getRotate() == -90))) {
            rotation++;
            miner.setRotate(0);
            if (miner.getScaleX() == -1)
                miner.setRotate(90);
            else miner.setRotate(-90);
        }
        updateStats();
    }

    public void down()
    {
        if (!((miner.getScaleX() == -1 && miner.getRotate() == -90) || (miner.getScaleX() == 1 && miner.getRotate() == 90))) {
            rotation++;
            miner.setRotate(0);
            if (miner.getScaleX() == -1)
                miner.setRotate(-90);
            else miner.setRotate(90);
        }
        updateStats();
    }

    public void left()
    {
        if (!((miner.getRotate() == 0) && miner.getScaleX() == -1)) {
            rotation++;
            miner.setRotate(0);
            miner.setScaleX(-1);
        }
        updateStats();
    }

    public void right()
    {
        if (!((miner.getRotate() == 0) && miner.getScaleX() == 1)) {
            rotation++;
            miner.setScaleX(1);
            miner.setRotate(0);
        }
        updateStats();
    }

    public Point move(int size)
    {
        int x = GridPane.getColumnIndex(miner);
        int y = GridPane.getRowIndex(miner);
        double rotate = miner.getRotate();
        double scale = miner.getScaleX();

        // move to the right
        if (rotate == 0 && scale == 1 && x >= 0 && x < size - 1) {
            GridPane.setColumnIndex(miner, x + 1);
            x++;
            move++;
        }
        // move to the left
        else if (rotate == 0 && scale == -1 && x > 0 && x <= size) {
            GridPane.setColumnIndex(miner, x - 1);
            x--;
            move++;
        }
        // move down
        else if (((rotate == -90 && scale == -1) || (rotate == 90 && scale == 1)) && y >= 0 && y < size - 1) {
            GridPane.setRowIndex(miner, y + 1);
            y++;
            move++;
        }
        // move up
        else if (((rotate == -90 && scale == 1) || (rotate == 90 && scale == -1)) && y > 0 && y <= size) {
            GridPane.setRowIndex(miner, y - 1);
            y--;
            move++;
        }
        else System.out.println("Miner moves out of bounds!");

        updateStats();

        System.out.println("(" + GridPane.getRowIndex(miner) + ", " + GridPane.getColumnIndex(miner) + ")");

        return (new Point(x, y));
    }

    // Allows events of listed objects to be handled
    public void setEventHandlers(Controller cont)
    {
        btnUp.setOnAction((EventHandler) cont);
        btnDown.setOnAction((EventHandler) cont);
        btnLeft.setOnAction((EventHandler) cont);
        btnRight.setOnAction((EventHandler) cont);

        btnMove.setOnAction((EventHandler) cont);
        btnAuto.setOnAction((EventHandler) cont);
    }
}
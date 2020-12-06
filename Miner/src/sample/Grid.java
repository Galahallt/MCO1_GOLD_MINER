package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import static javafx.scene.paint.Color.BROWN;

public class Grid
{
    private ArrayList<Rectangle> boxes = new ArrayList<>();
    public ImageView miner = new ImageView("sample/Miner.png");

    private int size;
    private int move;
    private int rotation;
    private int scan;

    GridPane grid = new GridPane();

    //Debug Miner moves
    Button btnRotate = new Button("Rotate");
    Button btnMove = new Button("Move");
    Button btnAuto = new Button("Execute");
    Button btnScan = new Button("Scan");

    Label lblStats;

    ArrayList<Point> pits;
    ArrayList<Point> beacons;
    Point goldPot;

    // Scene builder
    public Scene buildGrid(int size, ArrayList<Point> pits, ArrayList<Point> beacons, Point goldPot)
    {
        this.size = size;
        this.pits = pits;
        this.beacons = beacons;
        this.goldPot = goldPot;

        //Debug code
        GridPane.setConstraints(btnScan, 0, 1);
        GridPane.setHalignment(btnScan, HPos.LEFT);

        GridPane.setConstraints(btnRotate, 0, 1);
        GridPane.setHalignment(btnRotate, HPos.CENTER);

        GridPane.setConstraints(btnAuto, 0, 1);
        GridPane.setHalignment(btnAuto, HPos.RIGHT);

        GridPane.setConstraints(btnMove, 1, 1);
        GridPane.setHalignment(btnMove, HPos.CENTER);
        grid.getChildren().addAll(btnScan, btnRotate, btnMove, btnAuto);

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
                gridBoard.add(box, i, j);
                boxes.add(box);
            }
        }

        // add pits to grid
        for (Point point : pits) {
            ImageView pit = new ImageView("sample/Pit.png");
            pit.setFitWidth(50);
            pit.setFitHeight(50);
            gridBoard.add(pit, (int) point.getY(), (int) point.getX());

        }

        // add beacons to grid
        for (Point point : beacons) {
            ImageView beacon = new ImageView("sample/Beacon.png");
            beacon.setFitWidth(50);
            beacon.setFitHeight(50);
            gridBoard.add(beacon, (int) point.getY(), (int) point.getX());
        }

        // add gold to grid
        ImageView gold = new ImageView("sample/Gold.png");
        gold.setFitHeight(50);
        gold.setFitWidth(50);
        //GridPane.setConstraints(gold, (int) goldPot.getX(), (int) goldPot.getY());
        gridBoard.add(gold, (int) goldPot.getY(), (int) goldPot.getX());

        gridBoard.add(miner, 0, 0);


        //Stats label
        lblStats = new Label("Stats:\nMoves:\t\t" + move + "\nRotations:\t" + rotation + "\nScans:\t\t" + scan + "\n");
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
        lblStats.setText("Stats:\nMoves:\t\t" + move + "\nRotations:\t" + rotation + "\nScans:\t\t" + scan + "\n");
    }

    public void rotate()
    {
        miner.setRotate((miner.getRotate() + 90) % 360);

        if (miner.getRotate() == 180)
            miner.setScaleY(-1);
        else if (miner.getRotate() == 0)
            miner.setScaleY(1);
        rotation++;
        updateStats();
    }

    public void move(int size)
    {
        int x = GridPane.getColumnIndex(miner);
        int y = GridPane.getRowIndex(miner);

        // move to the right
        if (miner.getRotate() == 0 && x >= 0 && x < size - 1) {
            GridPane.setColumnIndex(miner, x + 1);
            move++;
        }
        // move to the left
        else if (miner.getRotate() == 180 && x > 0 && x <= size) {
            GridPane.setColumnIndex(miner, x - 1);
            move++;
        }
        // move down
        else if (miner.getRotate() == 90 && y >= 0 && y < size - 1) {
            GridPane.setRowIndex(miner, y + 1);
            move++;

        }
        // move up
        else if (miner.getRotate() == 270 && y > 0 && y <= size) {
            GridPane.setRowIndex(miner, y - 1);
            move++;
        }
        else System.out.println("Miner moves out of bounds!");

        updateStats();
        //System.out.println("(" + GridPane.getColumnIndex(miner) + ", " + GridPane.getRowIndex(miner) + ")");
    }

    // 0empty, 1pit, 2beacon, 3gold
    public String scan(int x, int y, int orientation) {
        Point p;
        scan++;
        updateStats();

        ArrayList<Integer> result = new ArrayList<>();

        switch (orientation) {
            case 0 -> {     // Looking Right
                for (int i = y + 1; i < size; i++) {
                    p = new Point(x, i);
                    if (pits.contains(p))
                        return "p";
                    else if (beacons.contains(p))
                        return "b";

                    else if (goldPot.equals(p))
                        return "g";
                }

                return "null";
            }
            case 90 -> {    // Looking Down
                for (int i = x + 1; i < size; i++) {
                    p = new Point(i, y);
                    if (pits.contains(p))
                        return "p";
                    else if (beacons.contains(p))
                        return "b";
                    else if (goldPot.equals(p))
                        return "g";
                }
                return "null";
            }
            case 180 -> {    // Looking Left
                for (int i = y - 1; i >= 0; i--) {
                    p = new Point(x, i);
                    if (pits.contains(p))
                        if (pits.contains(p))
                            return "p";
                        else if (beacons.contains(p))
                            return "b";
                        else if (goldPot.equals(p))
                            return "g";
                }
                return "null";
            }
            case 270 -> {    // Looking Up
                for (int i = x - 1; i >= 0; i--) {
                    p = new Point(i, y);
                    if (pits.contains(p))
                        return "p";
                    else if (beacons.contains(p))
                        return "b";
                    else if (goldPot.equals(p))
                        return "g";
                }
                return "null";
            }
        }
        return "null";
    }

    // Allows events of listed objects to be handled
    public void setEventHandlers(Controller cont)
    {
        btnScan.setOnAction((EventHandler) cont);
        btnRotate.setOnAction((EventHandler) cont);

        btnMove.setOnAction((EventHandler) cont);
        btnAuto.setOnAction((EventHandler) cont);
    }
}
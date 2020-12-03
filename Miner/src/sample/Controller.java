package sample;

import javafx.event.*;
import javafx.event.Event;
import javafx.beans.value.*; // ChangeListener
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;

public class Controller implements EventHandler<Event>, ChangeListener<String>
{
    Stage window;               // primaryStage
    private final Menu menu;          // Menu layout
    private final Grid grid;          // Grid layout
    private final Over over;          // Over layout
    private final Winner winner;      // Winner layout

    private int size;           // Grid size
    private boolean smart;      // Intelligence level: Smart-true, Random-false

    ArrayList<Point> pits = new ArrayList<>();      // Arraylist of coordinates for pits
    ArrayList<Point> beacons = new ArrayList<>();   // Arraylist of coordinates for beacons

    Point gold;                 // Coordinate of gold
    Point miner;                // Coordinate of miner

    // Constructor
    public Controller(Menu menu, Stage window)
    {
        this.menu = menu;
        this.window = window;
        this.grid = new Grid();
        this.over = new Over();
        this.winner = new Winner();

        miner = new Point(1 ,1);

        grid.setEventHandlers(this);
        menu.setEventHandlers(this);
    }

    // Switches from menu window to grid window
    public void switchToGrid()
    {
        smart = menu.rbIntSmart.isSelected();

        System.out.println(gold.getX() + " " + gold.getY());
        window.setScene(grid.buildGrid(size, pits, beacons, gold));
        window.show();
    }

    // Grid size checker
    public boolean checkGrid()
    {
        try {
            String input = menu.tfGrid.getText();
            Scanner a = new Scanner(input);

            int num = a.nextInt();
            if (num >= 8 && num <= 64) {    // Is within accepted range
                size = num;
                return true;
            }
            else {
                size = 0;
                return false;
            }
        } catch (Exception e) {
            size = 0;
            return false;
        }
    }

    // Gold coordinates checker
    public boolean checkGold()
    {
        try {
            String input = menu.tfGold.getText();
            Scanner a = new Scanner(input);

            int x = a.nextInt();
            int y = a.nextInt();

            if (x == 1 && y == 1) {     // Miner's initial position
                gold = null;
                return false;
            }
            else if (x > 0 && x <= size && y > 0 && y <= size) {    // Is valid coordinate according to grid size
                gold = new Point(x - 1, y - 1);
                return !(pits.contains(gold) || beacons.contains(gold));   // If already a pit/beacon
            }
            else {
                gold = null;
                return false;
            }
        } catch (Exception e) {
            gold = null;
            return false;
        }
    }

    // Adds/Removes valid pit coordinate
    public void addRemPit(String strBtn)
    {
        try {
            String input = menu.tfPit.getText();
            Scanner a = new Scanner(input);

            int x = a.nextInt() - 1;
            int y = a.nextInt() - 1;

            Point p = new Point(x, y);

            if (strBtn == "Add Pit") {
                if (!pits.contains(p) && !beacons.contains(p)
                        && (p.getX() > 0 && p.getX() <= size)
                        && (p.getY() > 0 && p.getY() <= size)
                        && !(p.equals(miner))
                        && !p.equals(gold))
                    pits.add(p);
                else
                    System.out.println("Invalid Input! (Pit)");
            } else if (strBtn == "Remove Pit") {
                pits.remove(p);
            }

            menu.tfPit.clear();
        }
        catch (Exception e) {   // Clicked add/remove with no input, Non-numeric input, Only 1 number input
            System.out.println("Invalid Input! (Pit)");
        }
    }

    //Update View for pit
    public void updatePitView() {
        if (pits.size() > 0) {      // Prevents index out of bounds when pit is empty
            String display = "";
            for (Point pit : pits)
                display += "(" + ((int) pit.getX() + 1) + ", " + ((int) pit.getY() + 1) + ")\n";
            menu.taPit.setText(display);
        }
        else
            menu.taPit.clear();
    }

    // adds valid beacon coordinate
    public void addRemBeacon(String strBtn)
    {
        try {
            String input = menu.tfBeacon.getText();
            Scanner a = new Scanner(input);

            int x = a.nextInt() - 1;
            int y = a.nextInt() - 1;

            Point p = new Point(x, y);

            if (strBtn == "Add Beacon") {
                if (!pits.contains(p) && !beacons.contains(p)
                        && (p.getX() > 0 && p.getX() <= size)
                        && (p.getY() > 0 && p.getY() <= size)
                        && !(p.equals(miner))
                        && !p.equals(gold))
                    beacons.add(p);
                else
                    System.out.println("Invalid Input! (Beacon)");
            } else if (strBtn == "Remove Beacon") {
                beacons.remove(p);
            }
            menu.tfBeacon.clear();
        }
        catch (Exception e) {   // Clicked Add/Remove with no input, Non-numeric input, Only 1 number input
            System.out.println("Invalid Input! (Beacon)");
        }
    }

    //Update View for beacon
    public void updateBeaconView() {
        if (beacons.size() > 0) {      // Prevents index out of bounds when beacon is empty
            String display = "";
            for (Point beacon : beacons)
                display += "(" + ((int) beacon.getX() + 1) + ", " + ((int) beacon.getY() + 1) + ")\n";
            menu.taBeacon.setText(display);
        }
        else
            menu.taBeacon.clear();
    }

    public boolean ifOver()
    {
        for (Point pit : pits)
            if (pit.getX() == miner.getX() && pit.getY() == miner.getY())
                return true;
        return false;
    }

    public boolean ifWinner()
    {
        return gold.getX() == miner.getX() && gold.getY() == miner.getY();
    }

    public void rotate()
    {
        grid.rotate();
    }

    public void move()
    {
        miner = grid.move(size);
        if (ifOver()) {
            window.setScene(over.buildOver());
            window.show();
            System.out.println("Game Over!");
        }
        else if (ifWinner()) {
            window.setScene(winner.buildWinner());
            window.show();
            System.out.println("Winner!");
        }
    }

    public void auto(){
        while (miner.getY() < gold.getY()) {    // Down
            rotate();
            move();
        }
        while (miner.getX() > gold.getX()) {    // Left
            rotate();
            move();
        }
        while (miner.getY() > gold.getY()) {    // Up
            rotate();
            move();
        }
        while (miner.getX() < gold.getX()) {    // Right
            rotate();
            move();
        }
    }

    // Handles events
    @Override
    public void handle(Event e)
    {
        if (e.getSource() instanceof Button)    // If a button is pushed
        {
            handle((ActionEvent) e);
        }
        else
            System.out.println("Event");
    }

    public void handle(ActionEvent e){
        String strButton;

        if (e.getSource() instanceof Button) {
            strButton = ((Button) e.getSource()).getText();
            // if Start button is pressed
            switch (strButton) {
                case "START!" -> switchToGrid();
                case "Add Pit", "Remove Pit" -> {
                    addRemPit(strButton);
                    updatePitView();
                }
                case "Add Beacon", "Remove Beacon" -> {
                    addRemBeacon(strButton);
                    updateBeaconView();
                }
                // Debug Miner Movement
                case "Rotate" -> {
                    rotate();
                }
                case "Move" -> {
                    move();
                 }
                case "Auto" ->
                    auto();
            }
        }
    }

    // Enables Start button if inputs are valid
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {
        boolean gridSize = checkGrid();
        boolean goldCo = checkGold();

        menu.btnStart.setDisable(!gridSize || !goldCo);
    }
}

package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.*;
import javafx.event.Event;
import javafx.beans.value.*; // ChangeListener
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.*;

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
    private boolean random;     // Intelligence level: Random-true, Smart-false

    private boolean sizeSet;    // Is grid size final?
    private boolean goldSet;    // is gold coordinate final?

    private Timeline move;

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

        sizeSet = false;
        goldSet = false;

        miner = new Point(0 ,0);

        grid.setEventHandlers(this);
        menu.setEventHandlers(this);
    }

    // Switches from menu window to grid window
    public void switchToGrid()
    {
        random = menu.rbIntRand.isSelected();

        System.out.println(gold.getX() + " " + gold.getY());
        window.setScene(grid.buildGrid(size, pits, beacons, gold));
        window.show();
    }

    // Grid size checker
    public void checkGrid()
    {
        if (!sizeSet) {
            try {
                String input = menu.tfGrid.getText();
                Scanner a = new Scanner(input);

                int num = a.nextInt();
                menu.btnSize.setDisable(num < 8 || num > 64);   // Is within accepted range
            } catch (Exception e) {
                menu.btnSize.setDisable(true);
            }
        }
    }

    // Gold coordinates checker
    public void checkGold()
    {
        if (!goldSet) {
            try {
                String input = menu.tfGold.getText();
                Scanner a = new Scanner(input);

                int x = a.nextInt();
                int y = a.nextInt();

                menu.btnGold.setDisable(x <= 0 || x > size || y <= 0 || y > size);  // Is valid coordinate according to grid size
            } catch (Exception e) {
                menu.btnGold.setDisable(true);
            }
        }
    }

    public void setSize()
    {
        String input = menu.tfGrid.getText();
        Scanner a = new Scanner(input);

        size = a.nextInt();
        menu.tfGold.setDisable(false);
        menu.btnSize.setDisable(true);
        menu.tfGrid.setDisable(true);

        sizeSet = true;
    }

    public void setGold()
    {
        String input = menu.tfGold.getText();
        Scanner a = new Scanner(input);

        int x = a.nextInt();
        int y = a.nextInt();

        gold = new Point(x - 1, y - 1);
        menu.btnGold.setDisable(true);
        menu.btnPitAdd.setDisable(false);
        menu.btnPitRem.setDisable(false);
        menu.btnPitSet.setDisable(false);

        goldSet = true;

        menu.tfGold.setDisable(true);
        menu.tfPit.setDisable(false);
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
                        && (p.getX() >= 0 && p.getX() < size)
                        && (p.getY() >= 0 && p.getY() < size)
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
    //checks the tiles in between the beacon and gold along the x-coordinate
    public boolean checkBetweenBGX(Point p)
    {
        Point temp;
        if (p.getX() > gold.getX())
        {
            for (int i = (int) gold.getX() + 1; i < p.getX(); i++)
            {
                temp = new Point(i, (int) p.getY());
                if (pits.contains(temp))
                    return true; // there is an obstruction (pit)
            }
        }
        else
        {
            for (int i = (int) p.getX() + 1; i < gold.getX(); i++)
            {
                temp = new Point(i, (int) p.getY());
                if (pits.contains(temp))
                    return true; // there is an obstruction (pit)
            }
        }
        return false;
    }

    //checks the tiles in between the beacon and gold along the y-coordinate
    public boolean checkBetweenBGY(Point p)
    {
        Point temp;
        if (p.getY() > gold.getY())
        {
            for (int i = (int) gold.getY() + 1; i < p.getY(); i++)
            {
                temp = new Point((int) p.getX(), i);
                if (pits.contains(temp))
                    return true; // there is an obstruction (pit)
            }
        }
        else
        {
            for (int i = (int) p.getY() + 1; i < gold.getY(); i++)
            {
                temp = new Point((int) p.getX(), i);
                if (pits.contains(temp))
                    return true; // there is an obstruction (pit)
            }
        }
        return false;
    }



    // adds/removes valid beacon coordinate
    public void addRemBeacon(String strBtn)
    {
        try {
            String input = menu.tfBeacon.getText();
            Scanner a = new Scanner(input);
            Boolean obstruction = false;

            int x = a.nextInt() - 1;
            int y = a.nextInt() - 1;

            Point p = new Point(x, y);
            if (!p.equals(gold))
            {
                if (p.getX() == gold.getX())
                    obstruction = checkBetweenBGY(p);
                else if (p.getY() == gold.getY())
                    obstruction = checkBetweenBGX(p);
            }


            if (strBtn == "Add Beacon") {
                if (!pits.contains(p) && !beacons.contains(p)
                        && (p.getX() >= 0 && p.getX() < size)
                        && (p.getY() >= 0 && p.getY() < size)
                        && !p.equals(gold) && !obstruction)
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

    //Do this if the button for "Set Pits" is clicked
    public void setPits()
    {
        menu.btnPitSet.setDisable(true);
        menu.btnPitAdd.setDisable(true);
        menu.btnPitRem.setDisable(true);
        menu.tfPit.setDisable(true);

        menu.btnBeaconAdd.setDisable(false);
        menu.btnBeaconRem.setDisable(false);
        menu.btnBeaconSet.setDisable(false);
        menu.tfBeacon.setDisable(false);
    }

    //Do this if the button for "Set Beacons" is clicked
    public void setBeacons()
    {
        menu.btnBeaconSet.setDisable(true);
        menu.btnBeaconAdd.setDisable(true);
        menu.btnBeaconRem.setDisable(true);
        menu.tfBeacon.setDisable(true);

        menu.btnStart.setDisable(false);
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
        }
        else if (ifWinner()) {
            window.setScene(winner.buildWinner());
            window.show();
        }
    }

    public void scan()
    {
        switch (grid.scan()) {
            case 0 -> System.out.println("Empty");
            case 1 -> System.out.println("Pit");
            case 2 -> System.out.println("Beacon");
            case 3 -> System.out.println("Gold");
        }
    }

    // Random Intelligence Level
    public void randLvl()
    {
        Random rand = new Random();
        switch (rand.nextInt(2)) {
            case 0 -> rotate();
            case 1 -> move();
        }
        if (ifOver() || ifWinner())
            move.stop();
    }

    // Smart Intelligence Level
    public void smartLvl()
    {

    }

    public void animate()
    {
        if (random)
            randLvl();
        else
            smartLvl();
    }

    public void execute()
    {
        // Check if pit/gold is in starting position of miner
        if (ifOver()) {
            window.setScene(over.buildOver());
            window.show();
        }
        else if (ifWinner()) {
            window.setScene(winner.buildWinner());
            window.show();
        }

        // Start animation
        move = new Timeline(
                new KeyFrame(
                        Duration.millis(400), event -> animate()
                )
        );

        move.setCycleCount(Animation.INDEFINITE);
        move.play();
        if (ifOver() || ifWinner())
            move.stop();
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

    public void handle(ActionEvent e) {
        String strButton;

        if (e.getSource() instanceof Button) {
            strButton = ((Button) e.getSource()).getText();
            // if Start button is pressed
            switch (strButton) {
                case "START!" -> switchToGrid();
                case "Set Size" -> setSize();
                case "Set Gold" -> setGold();
                case "Add Pit", "Remove Pit" -> {
                    addRemPit(strButton);
                    updatePitView();
                }
                case "Add Beacon", "Remove Beacon" -> {
                    addRemBeacon(strButton);
                    updateBeaconView();
                }
                case "Set Pits" -> setPits();
                // Debug Miner Movement
                case "Set Beacons" -> setBeacons();
                case "Rotate" -> rotate();
                case "Move" -> move();
                case "Execute" -> execute();
                case "Scan" -> scan();
            }
        }
    }

    // Enables Start button if inputs are valid
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {
        if (!sizeSet)
            checkGrid();
        if (!goldSet)
            checkGold();
    }
}

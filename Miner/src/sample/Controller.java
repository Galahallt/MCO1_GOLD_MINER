package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.*;
import javafx.event.Event;
import javafx.beans.value.*; // ChangeListener
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Controller implements EventHandler<Event>, ChangeListener<String>
{
    Stage window;               // primaryStage
    private final Menu menu;          // Menu layout
    private final Grid grid;          // Grid layout
    private final Over over;          // Over layout
    private final Winner winner;      // Winner layout
    private final NoSol noS;

    private int size;           // Grid size
    public boolean random;     // Intelligence level: Random-true, Smart-false

    private boolean sizeSet;    // Is grid size final?
    private boolean goldSet;    // is gold coordinate final?
    private boolean noSol = false;

    public Timeline move;

    private String flow = null;

    ArrayList<Point> pits = new ArrayList<>();      // Arraylist of coordinates for pits
    ArrayList<Point> beacons = new ArrayList<>();   // Arraylist of coordinates for beacon
    ArrayList<Point> visited = new ArrayList<>();

    int smartInd = 0;

    Point gold;                 // Coordinate of gold

    // Constructor
    public Controller(Menu menu, Stage window)
    {
        this.menu = menu;
        this.window = window;
        grid = new Grid();
        over = new Over();
        winner = new Winner();
        noS = new NoSol();

        sizeSet = false;
        goldSet = false;

        grid.setEventHandlers(this);
        menu.setEventHandlers(this);
        over.setEventHandlers(this);
        winner.setEventHandlers(this);
        noS.setEventHandlers(this);
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
        menu.btnStart.setDisable(false);
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

    // Update View for pit
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
    // Checks the tiles in between the beacon and gold along the x-coordinate
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

    // Checks the tiles in between the beacon and gold along the y-coordinate
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

    // Adds/removes valid beacon coordinate
    public void addRemBeacon(String strBtn)
    {
        try {
            String input = menu.tfBeacon.getText();
            Scanner a = new Scanner(input);
            boolean obstruction = false;

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
                        && (p.getX() == gold.getX() || p.getY() == gold.getY())
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
    }

    public boolean ifOver()
    {
        Point p = new Point (GridPane.getRowIndex(grid.miner), GridPane.getColumnIndex(grid.miner));
        return pits.contains(p);
    }

    public boolean ifWinner()
    {
        Point p = new Point (GridPane.getRowIndex(grid.miner), GridPane.getColumnIndex(grid.miner));
        return gold.equals(p);
    }

    public void smartScan()
    {
        grid.scan++;
        grid.updateStats();
        Timeline delay = new Timeline(
                new KeyFrame(
                        Duration.millis(400), event -> move.play()
                )
        );
        delay.play();
    }

    public void rotate()
    {
        grid.rotate();

        if (!random) {
            move.pause();
            Timeline delay = new Timeline(
                    new KeyFrame(
                            Duration.millis(400), event -> smartScan()
                    )
            );
            delay.play();
        }
    }

    // Display credits if game is finished
    public void credits() {
        if (ifOver()) {
            window.setScene(over.buildOver());
            window.show();
            move.stop();
        }
        else if (ifWinner()) {
            window.setScene(winner.buildWinner());
            window.show();
            move.stop();
        }
    }

    public void move() {
        if (random)
            grid.move(size, this);
        else {
            int x = GridPane.getColumnIndex(grid.miner);
            int y = GridPane.getRowIndex(grid.miner);

            // move to the right
            if (grid.miner.getRotate() == 0 && x >= 0 && x < size - 1) {
                x++;
            }
            // move to the left
            else if (grid.miner.getRotate() == 180 && x > 0 && x <= size) {
                x--;
            }
            // move down
            else if (grid.miner.getRotate() == 90 && y >= 0 && y < size - 1) {
                y++;
            }
            // move up
            else {
                y--;
            }
            Point p = new Point(y, x);

            if (pits.contains(p)) { // noSol
                move.stop();
                window.setScene(noS.buildNoSol());
                window.show();
            }
            else
                grid.move(size, this);
        }
    }

    // Random Intelligence Level
    public void randLvl() {
        credits();

        Random rand = new Random();
        switch (rand.nextInt(3)) {
            case 0 -> rotate();
            case 1 -> move();
            case 2 -> grid.scan(0, 0, 0);
        }
    }

    // Determines what is in front of the miner up to the edge of the grid
    // Returns a String of additional commands
    public String scan(int size, String actions)
    {
        // initial position of miner
        int orientation = 0;
        int x = 0;
        int y = 0;
        String add = "";
        for (int i = 0; i < actions.length(); i++)
        {
            if (actions.charAt(i) == 'm' && orientation == 0)
                y++;
            else if (actions.charAt(i) == 'm' && orientation == 90)
                x++;
            else if (actions.charAt(i) == 'm' && orientation == 180)
                y--;
            else if (actions.charAt(i) == 'm' && orientation == 270)
                x--;
            else orientation = (orientation + 90) % 360;
        }
        // Checks if the string of actions is valid
        if (x >= 0 && x < size && y >= 0 && y < size && !pits.contains(new Point (x, y))) {
            if (grid.scan(x, y, orientation) == "b") {
                // move until miner is on the beacon tile
                while (!beacons.contains(new Point (x, y)))
                {
                    if (orientation == 0)
                        y++;
                    else if (orientation == 90)
                        x++;
                    else if (orientation == 180)
                        y--;
                    else if (orientation == 270)
                        x--;
                    add += 'm';
                }
                // rotate on the beacon tile until scanned is 'g'
                while (grid.scan(x, y, orientation) != "g")
                {
                    orientation = (orientation + 90) % 360;
                    add += 'r';
                }
            }
            // move until miner is on the gold tile
            if (grid.scan(x, y, orientation) == "g") {
                while (!gold.equals(new Point(x, y))) {
                    if (orientation == 0)
                        y++;
                    else if (orientation == 90)
                        x++;
                    else if (orientation == 180)
                        y--;
                    else if (orientation == 270)
                        x--;
                    add += 'm';
                }
                return add;
            }
        }
        return "" + add;
    }

    // Determines if next move of miner is valid (used in smart intelligence level)
    public boolean validMove(int size, String actions)
    {
        // initial position of miner
        int orientation = 0;
        int x = 0;
        int y = 0;
        ArrayList<Point> tiles = new ArrayList<>();
        Point p;
        tiles.add(new Point(x, y));

        for (int i = 0; i < actions.length(); i++)
        {
            if (actions.charAt(i) == 'm' && orientation == 0)
                p = new Point (x, y+1);
            else if (actions.charAt(i) == 'm' && orientation == 90)
                p = new Point(x+1, y);
            else if (actions.charAt(i) == 'm' && orientation == 180)
                p = new Point(x, y - 1);
            else
                p = new Point (x - 1, y);

            if (actions.charAt(i) == 'r')
                orientation = (orientation + 90) % 360;
            else if (!tiles.contains(p)) {
                if (actions.charAt(i) == 'm' && orientation == 0)
                    y++;
                else if (actions.charAt(i) == 'm' && orientation == 90)
                    x++;
                else if (actions.charAt(i) == 'm' && orientation == 180)
                    y--;
                else if (actions.charAt(i) == 'm' && orientation == 270)
                    x--;
                tiles.add(new Point(x, y));
            }
            else return false;
        }

        if (!(x >= 0 && x < size && y >= 0 && y < size))
            return false;
        else if (pits.contains(new Point(x, y)))
            return false;
        else if (actions.contains("rrrr"))
            return false;
        if (visited.contains(new Point(x, y)) && actions.charAt(actions.length()-1) != 'r')
            return false;
        else visited.add(new Point(x, y));

        System.out.println("VALID: " + actions + " = [" + (x + 1) + ", " + (y + 1) + "]" + " OR: " + orientation);
        return true;
    }

    // Determines if gold is found (used in smart intelligence level)
    public boolean foundGold(String actions)
    {
        // initial position of miner
        int orientation = 0;
        int x = 0;
        int y = 0;
        for (int i = 0; i < actions.length(); i++)
        {
            if (actions.charAt(i) == 'm' && orientation == 0)
                y++;
            else if (actions.charAt(i) == 'm' && orientation == 90)
                x++;
            else if (actions.charAt(i) == 'm' && orientation == 180)
                y--;
            else if (actions.charAt(i) == 'm' && orientation == 270)
                x--;
            else orientation = (orientation + 90) % 360;
        }
        if (gold.equals(new Point (x, y))) {
            System.out.println("FOUND:" + actions + " = [" + x + ", " + y + "]" + " OR: " + orientation);
            return true;
        }
        return false;
    }

    // Returns a string of characters that will be the most efficient for the miner (used in smart intelligence level)
    public String shortestPath()
    {
        Queue<String> actions = new ArrayDeque<>(); // used in smartlvl
        String add = "";                            // used in smartlvl
        String[] moves = {"m", "r"};                // used in smartlvl
        String put = "";                            // used in smartlvl
        String scan = "";
        ArrayList<String> tries = new ArrayList<>();

        while (!foundGold(add) && !noSol)
        {
            if (!actions.isEmpty())
                add = actions.remove();
            if (scan(size, put).length() > 0)
            {
                scan = put + scan(size, put);
                if (validMove(size, scan))
                    actions.add(scan);
            }
            else {
                for (String i : moves)
                {
                    put = add + i;
                    if (validMove(size, put))
                        actions.add(put);
                    else {
                        if (!tries.contains(put))
                            tries.add(put);
                        else
                            noSol = true;
                    }
                }
            }
        }
        return add;
    }

    // Returns a character in the string of actions to move gui (used in smart intelligence level)
    public Character smartMove()
    {
        if (flow == null)
            flow = shortestPath();
        Character smartMove = 's';
        if (smartInd < flow.length())
             smartMove = flow.charAt(smartInd);
        else {
            window.setScene(noS.buildNoSol());
            window.show();
            move.stop();
        }
        smartInd++;

        return smartMove;
    }

    // Smart Intelligence Level
    public void smartLvl(){
        credits();

        if (!ifOver() && !ifWinner()) {
            switch (smartMove()) {
                case 'm' -> move();
                case 'r' -> rotate();
            }
        }
    }

    public void execute() {
        // Check if pit/gold is in starting position of miner
        if (ifOver() || ifWinner() || (noSol && !random)) {
            move = new Timeline(
                    new KeyFrame(
                            Duration.millis(400), event -> {
                        credits();
                    }
                    )
            );
            move.play();
        }

        // Random Intelligence
        else if (random) {
            move = new Timeline(
                    new KeyFrame(
                            Duration.millis(400), event -> {
                        randLvl();
                    }
                    )
            );
            move.setCycleCount(Animation.INDEFINITE);
            move.play();
        }

        // Smart Intelligence
        else {
            grid.scan++;
            grid.updateStats();
            move = new Timeline(new KeyFrame(Duration.millis(400)),
                    new KeyFrame(
                            Duration.millis(400), event -> {
                        smartLvl();
                    }
                    )
            );
            move.setCycleCount(Animation.INDEFINITE);
            move.play();
        }
    }

    public void retry()
    {
        Menu next = new Menu();

        window.setScene(next.buildMenu());
        new Controller(next, window);
        window.show();
    }

    // Handles events
    @Override
    public void handle(Event e)
    {
        if (e.getSource() instanceof Button)    // If a button is pushed
        {
            try {
                handle((ActionEvent) e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else
            System.out.println("Event");
    }

    public void handle(ActionEvent e) throws IOException {
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
                case "Execute" -> execute();
                case "RETRY" -> retry();
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

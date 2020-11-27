package sample;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;

import java.awt.*;

public class Miner {
    private Point pos;

    public Miner()
    {
        pos = new Point(1, 1);
    }

    public void move(Point p)
    {
        pos = p;
    }

    public void up(Grid grid)
    {
    }

    public void down(Grid grid)
    {
    }

    public void left(Grid grid)
    {
    }

    public void right(Grid grid)
    {

    }
}
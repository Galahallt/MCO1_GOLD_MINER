package sample;

import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Winner
{
    public Scene buildWinner()
    {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.setVgap(8);
        grid.setHgap(10);

        // Background
        Image image = new Image("sample/Winner.gif", true);
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );
        grid.setBackground(new Background(bgImage));

        Scene scene = new Scene(grid, 600, 500);
        grid.requestFocus();

        return scene;
    }
}
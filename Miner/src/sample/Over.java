package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Over
{
    public Scene buildOver()
    {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-image: url('sample/Death.png');-fx-background-size: cover;");
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.setVgap(8);
        grid.setHgap(10);

        ImageView over = new ImageView("sample/Over.png");
        over.setFitHeight(200);
        over.setFitWidth(400);
        GridPane.setHalignment(over, HPos.CENTER);
        grid.add(over, 0, 1);

        ImageView ghost = new ImageView("sample/Ghost.png");
        ghost.setFitHeight(100);
        ghost.setFitWidth(100);
        GridPane.setHalignment(ghost, HPos.RIGHT);
        grid.add(ghost, 0, 0);

        Scene scene = new Scene(grid, 600, 500);
        grid.requestFocus();

        return scene;
    }
}
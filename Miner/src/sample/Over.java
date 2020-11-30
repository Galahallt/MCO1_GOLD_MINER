package sample;

import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.layout.GridPane;

public class Over
{
    public Scene buildOver()
    {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-image: url('sample/Cave.jpg');-fx-background-size: cover;");
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.setVgap(8);
        grid.setHgap(10);

        Scene scene = new Scene(grid, 600, 500);
        grid.requestFocus();

        return scene;
    }
}
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.BLACK;

public class Main extends Application {
    Stage window;
    Menu menu;
    Grid grid;

    @Override
    public void start(Stage primaryStage) throws Exception{
        menu = new Menu();
        grid = new Grid();

        window = primaryStage;
        window.setTitle("Gold Miner");

        window.setScene(menu.buildMenu());
        new Controller(menu, window, grid);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

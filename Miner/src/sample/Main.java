package sample;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    Stage window;
    Menu menu;

    @Override
    public void start(Stage primaryStage) throws Exception{
        menu = new Menu();

        window = primaryStage;
        window.setTitle("Gold Miner");
        window.getIcons().add(new Image("sample/Gold.png"));

        window.setScene(menu.buildMenu());
        new Controller(menu, window);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

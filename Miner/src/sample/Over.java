package sample;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import static javafx.scene.paint.Color.WHITE;


public class Over
{
    //Controller cont;
    Button btnRetry = new Button ("RETRY");;

    public Scene buildOver()
    {
        AnchorPane anchor = new AnchorPane();
        anchor.setPrefSize(800, 600);

        Image image = new Image("sample/Death.png", true);
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );
        anchor.setBackground(new Background(bgImage));

        btnRetry.setStyle("-fx-background-image: url('/sample/wood.png'); -fx-background-position: center; -fx-border-color: BLACK; -fx-border-width: 4;");
        btnRetry.setTextFill(WHITE);
        btnRetry.setPrefSize(200, 71);
        btnRetry.setFont(new Font("Cooper Black", 36));
        btnRetry.setLayoutX(300);
        btnRetry.setLayoutY(495);

        anchor.getChildren().add(btnRetry);

        return new Scene(anchor, 800, 600);
    }

    public void setEventHandlers(Controller cont)
    {
        btnRetry.setOnAction((EventHandler) cont);
    }
}
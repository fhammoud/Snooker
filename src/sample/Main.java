package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Snooker Stats");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("ball.png")));
        primaryStage.setScene(new Scene(root, 750, 750));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

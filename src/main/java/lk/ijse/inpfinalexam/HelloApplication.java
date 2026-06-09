package lk.ijse.inpfinalexam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("client.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        Stage stage2 = new Stage();
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("client.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load(), 800, 600);
        stage2.setTitle("Hello!");
        stage2.setScene(scene1);
        stage2.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
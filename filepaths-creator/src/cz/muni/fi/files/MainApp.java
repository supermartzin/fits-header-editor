package cz.muni.fi.files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader mainLayoutFile = new FXMLLoader(MainApp.class.getResource("view/MainLayout.fxml"));
            AnchorPane mainLayout = mainLayoutFile.load();

            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);

            // user cannot resize
            primaryStage.setResizable(false);
            primaryStage.setHeight(mainLayout.getHeight());

            primaryStage.show();
        } catch (IOException ioEx) {
            // TODO throw exception dialog
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

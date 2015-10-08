package cz.muni.fi.files;

import cz.muni.fi.files.controller.MainLayoutController;
import cz.muni.fi.files.dialog.ExceptionDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FilePath Creator 1.2
 *
 * @author Martin Vrábel
 * @version 1.2.0.RELEASE
 */
public class MainApp extends Application {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Stage _primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return _primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        _primaryStage = primaryStage;
        _primaryStage.setTitle("FilePaths Creator");
        _primaryStage.getIcons().add(new Image("icons/icon.png"));

        // user cannot resize
        _primaryStage.setResizable(false);

        initMainLayout();

        _primaryStage.show();
    }

    private void initMainLayout() {
        try {
            FXMLLoader mainLayoutFile = new FXMLLoader(
                    MainApp.class.getResource("view/MainLayout.fxml"));
            AnchorPane mainLayout = mainLayoutFile.load();

            Scene scene = new Scene(mainLayout);
            _primaryStage.sizeToScene();
            _primaryStage.setScene(scene);

            MainLayoutController controller = mainLayoutFile.getController();
            controller.setMainApp(this);
        } catch (Exception ex) {
            ExceptionDialog dialog = new ExceptionDialog();
            dialog.setTitle("Neočakávaná chyba programu");
            dialog.setHeaderText("Nastala neočakávaná chyba programu");
            dialog.setContentText("Program vyhodil neočakávanú výnimku so správou:" + LINE_SEPARATOR
                    + ex.getMessage());
            dialog.setException(ex);
            dialog.showAndWait();
            System.exit(1);
        }
    }
}

package cz.muni.fi.fits.gui;

import cz.muni.fi.fits.gui.listeners.MessageListener;
import cz.muni.fi.fits.gui.models.FileItem;
import cz.muni.fi.fits.gui.models.Preferences;
import cz.muni.fi.fits.gui.services.ResourceBundleService;
import cz.muni.fi.fits.gui.tasks.EditingTask;
import cz.muni.fi.fits.gui.utils.dialogs.ExceptionDialog;
import cz.muni.fi.fits.gui.view.controllers.FilesOverviewController;
import cz.muni.fi.fits.gui.view.controllers.OperationTabsViewController;
import cz.muni.fi.fits.gui.view.controllers.OutputViewController;
import cz.muni.fi.fits.gui.view.controllers.RootLayoutController;
import cz.muni.fi.fits.gui.view.operationtabs.utils.OperationTabsLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * TODO description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class MainApp extends Application {

    private Stage _primaryStage;
    private BorderPane _rootLayout;
    private SplitPane _centralLayout;
    private Image _appIcon;

    private OutputViewController _outputViewController;

    private ObservableList<FileItem> _files;
    private Preferences _preferences;
    private ResourceBundle _resources;

    public MainApp() {
        _files = FXCollections.observableArrayList();
        _preferences = new Preferences();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        _appIcon = new Image("graphics/icon.png");

        _primaryStage = primaryStage;
        _primaryStage.setTitle("FITS Header Visual Editor");
        _primaryStage.getIcons().add(_appIcon);

        _resources = ResourceBundleService.getBundle();

        try {
            setUnhandledExceptionHandler();

            // initialize layouts
            initRootLayout();
            initFilesOverview();
            initOperationTabsView();
            initOutputView();
        } catch (IOException ioEx) {
            ExceptionDialog.show(
                    _resources.getString("app.error.dialog.title"),
                    _resources.getString("app.error.dialog.header"),
                    _resources.getString("app.error.dialog.content.fatal"),
                    ioEx,
                    null);

            // exit application
            System.exit(0);
        }
    }

    public Stage getPrimaryStage() {
        return _primaryStage;
    }

    public Collection<FileItem> getSelectedFiles() {
        Set<FileItem> selectedFiles = new LinkedHashSet<>();

        // filter only selected files
        _files.stream()
                .filter(FileItem::isSelected)
                .forEach(selectedFiles::add);

        return selectedFiles;
    }

    public ObservableList<FileItem> getAllFiles() {
        return _files;
    }

    public void setPreferences(Preferences preferences) {
        _preferences = preferences;
    }

    public Preferences getPreferences() {
        return _preferences;
    }

    public void setEditingTask(EditingTask editingTask) {
        if (editingTask != null) {
            editingTask.setMessageListener(new MessageListener() {
                @Override
                public void onInfo(String infoMessage) {
                    Platform.runLater(() -> _outputViewController.onInfoMessage(infoMessage));
                }

                @Override
                public void onError(String errorMessage) {
                    Platform.runLater(() -> _outputViewController.onErrorMessage(errorMessage));
                }

                @Override
                public void onException(String exceptionMessage) {
                    Platform.runLater(() -> _outputViewController.onExceptionMessage(exceptionMessage));
                }
            });
        }
    }

    public Image getApplicationIcon() {
        return _appIcon;
    }

    private void setUnhandledExceptionHandler() {
        // set unhandled exception handler
        Thread.currentThread().setUncaughtExceptionHandler((thread, exception) ->
                Platform.runLater(() ->
                        ExceptionDialog.show(
                                _resources.getString("app.error.dialog.title"),
                                _resources.getString("app.error.dialog.header"),
                                _resources.getString("app.error.dialog.content.fatal"),
                                exception,
                                this)));
    }

    private void initRootLayout() throws IOException {
        FXMLLoader rootLayoutFile = new FXMLLoader(MainApp.class.getResource("view/RootLayout.fxml"));
        ResourceBundleService.setResourceBundle(rootLayoutFile);
        _rootLayout = rootLayoutFile.load();

        RootLayoutController controller = rootLayoutFile.getController();
        controller.setMainApp(this);

        _centralLayout = new SplitPane();
        _centralLayout.setOrientation(Orientation.VERTICAL);
        _rootLayout.setCenter(_centralLayout);

        Scene scene = new Scene(_rootLayout);
        _primaryStage.setScene(scene);

        _primaryStage.show();
    }

    private void initFilesOverview()
            throws IOException {
        FXMLLoader filesOverviewFile = new FXMLLoader(MainApp.class.getResource("view/FilesOverview.fxml"));
        ResourceBundleService.setResourceBundle(filesOverviewFile);
        _rootLayout.setLeft(filesOverviewFile.load());

        FilesOverviewController controller = filesOverviewFile.getController();
        controller.setMainApp(this);
        controller.setTableItemsCollection(_files);
    }

    private void initOperationTabsView()
            throws IOException {
        FXMLLoader operationTabsViewFile = new FXMLLoader(MainApp.class.getResource("view/OperationTabsView.fxml"));
        ResourceBundleService.setResourceBundle(operationTabsViewFile);
        AnchorPane anchorPane = operationTabsViewFile.load();

        OperationTabsViewController controller = operationTabsViewFile.getController();
        controller.setMainApp(this);

        // load Tabs into TabPane
        Collection<Tab> tabs = loadOperationTabs(controller);
        TabPane tabPane = extractTabPane(anchorPane);
        if (tabPane != null)
            tabPane.getTabs().addAll(tabs);

        // load Tabs into view
        _centralLayout.getItems().add(anchorPane);
    }

    private void initOutputView()
            throws IOException {
        FXMLLoader outputViewFile = new FXMLLoader(MainApp.class.getResource("view/OutputView.fxml"));
        ResourceBundleService.setResourceBundle(outputViewFile);

        _centralLayout.getItems().add(outputViewFile.load());
        _outputViewController = outputViewFile.getController();
        _outputViewController.setMainApp(this);
    }

    private Collection<Tab> loadOperationTabs(OperationTabsViewController parentController)
            throws IOException {
        Collection<Tab> tabs = new LinkedHashSet<>();

        // load Tabs
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/AddNewRecordTab.fxml", parentController, this));
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/RemoveRecordTab.fxml", parentController, this));
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/ChangeKeywordTab.fxml", parentController, this));
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/ChangeValueTab.fxml", parentController, this));
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/ChainRecordsTab.fxml", parentController, this));
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/ShiftTimeTab.fxml", parentController, this));
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/JulianDateTab.fxml", parentController, this));
        tabs.add(OperationTabsLoader.loadTab("view/operationtabs/HeliocentricJulianDateTab.fxml", parentController, this));

        return tabs;
    }

    private TabPane extractTabPane(AnchorPane anchorPane) {
        if (anchorPane != null) {
            for (Node node : anchorPane.getChildren()) {
                if (node instanceof TabPane)
                    return (TabPane) node;
            }
        }

        return null;
    }
}

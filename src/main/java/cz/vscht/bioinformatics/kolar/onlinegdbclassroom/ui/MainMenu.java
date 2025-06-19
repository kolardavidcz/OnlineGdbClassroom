/**
 * Package containing UI components for the Online GDB Classroom application.
 */
package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main application window class that initializes and displays the primary user interface.
 * Extends JavaFX Application to provide the main entry point for the UI.
 */
public class MainMenu extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        stage.getIcons().add(icon);

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 811 , 710); //Alternative resolutions 888, 828, 811 : 710
        stage.setTitle("GDBOnline exercise generator");
        stage.setScene(scene);
        stage.show();

        mainStage = stage;
    }
    // set the mainStage to by publicly visible
    public static Stage getMainStage() {
        return mainStage;
    }
}

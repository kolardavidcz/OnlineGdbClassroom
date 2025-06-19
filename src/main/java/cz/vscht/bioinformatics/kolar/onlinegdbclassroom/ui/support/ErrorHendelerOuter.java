package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.support;

import com.github.curiousoddman.rgxgen.parsing.dflt.RgxGenParseException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Outer class providing error handling functionality for the application.
 * Provides methods to display error messages both in GUI and console.
 */
public class ErrorHendelerOuter {
    public static class ErrorHendeler {
        public static String smartSpace = "=====================================================================================================================\n";

        public static void resolve(Exception error) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                if(error instanceof RgxGenParseException) {
                    alert.setTitle("REGEXP SYNTAX ERROR");
                } else {
                    alert.setTitle("ERROR");
                }

                alert.setHeaderText(null);
                alert.setContentText(error.getLocalizedMessage());
                alert.setGraphic(null);
                alert.showAndWait();
            });

            System.out.println(smartSpace + error + "\n" + smartSpace);
        }
    }
}

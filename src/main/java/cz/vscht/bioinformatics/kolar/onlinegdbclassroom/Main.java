package cz.vscht.bioinformatics.kolar.onlinegdbclassroom;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.MainMenu;
import javafx.application.Application;

/**
 * Main application class for the Online GDB Classroom system.
 * This class serves as the entry point and contains configuration constants
 * for test case visibility and output matching settings.
 */

public class Main {

    /** Options:
     *  0 - Hidden ("This test case information is not visible.")
     *  1 - Enable with Input and User Output
     *  2 - Enable with Input, User Output and Expected Output
     */
    public static final int TEST_VISIBLE = 2;


    /** Optons:
     *  S - Strict matching
     *  F - Ignores whitespaces (Flexible)
     */
    public static final String MATCHING_SENSITIVITY = "S";


    public static void main(String[] args) {
        Application.launch(MainMenu.class, args);
    }
}

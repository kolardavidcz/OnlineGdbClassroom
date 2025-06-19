package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.support;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums.EnumInputOutput;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.MainMenu;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

/** -------------------------------------------------
 * Remembering the last used folder
 * -------------------------------------------------
 * <p> 
 * the last directory used for file operations (LAST_INPUT_DIR_KEY / LAST_OUTPUT_DIR_KEY / LAST_SAVE_DIR_KEY) */

public class UIFileManager {
    
    private static final Preferences preferences = Preferences.userNodeForPackage( UIFileManager.class );

    public static List<File> addFiles(EnumInputOutput enumInputOutput, List<File> filesURL) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File lastDirectory = getLastDir(enumInputOutput);
        fileChooser.setInitialDirectory(lastDirectory);


        List<File> selectedFilesRaw = fileChooser.showOpenMultipleDialog(MainMenu.getMainStage());
        if (selectedFilesRaw != null && !selectedFilesRaw.isEmpty()) {
            filesURL.addAll(selectedFilesRaw);
            filesURL.stream()
                    .distinct()
                    .toList();
            
            File lastFolder = selectedFilesRaw.getFirst().getParentFile();
            updateLastFir(enumInputOutput, lastFolder);
        }
        return filesURL;
    }
    /* -------------- Saving final file -------------------- */
    public static File setJsonFileDestination() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory to Save JSON");
        
        File lastDirectory = getLastDir(EnumInputOutput.OUTPUT_JSON);
        directoryChooser.setInitialDirectory(lastDirectory);
        
        File selectedDirectory = directoryChooser.showDialog(MainMenu.getMainStage()); //ownerWindow

        updateLastFir(EnumInputOutput.OUTPUT_JSON, selectedDirectory);
        return selectedDirectory;
    }
    
    private static void updateLastFir(EnumInputOutput io, File lastFolder) {

        String preferenceKey = io.getDirectoryKey();
        if (lastFolder != null && lastFolder.isDirectory()) {
            String parentFilePath = lastFolder.getAbsolutePath();
            preferences.put(preferenceKey, parentFilePath);

        } else {
            System.out.println("No File Selected");
        }
    }

    
    private static File getLastDir(EnumInputOutput io) {
        
        File home =             new File(System.getProperty("user.home"));
        File desktop =          new File(System.getProperty("user.home") + File.separator + "Desktop");
        File inputFolder =      new File(preferences.get(EnumInputOutput.INPUT_TESTS.getDirectoryKey(),     desktop.getAbsolutePath()));
        File lastUsedFolder =   new File(preferences.get(io.getDirectoryKey(),                              inputFolder.getAbsolutePath()));

        return Stream.of(lastUsedFolder, 
                        inputFolder, 
                        desktop, 
                        home)
                .filter(Objects::nonNull)
                .filter(File::isDirectory)
                .findFirst()
                .orElse(home);
    }
}

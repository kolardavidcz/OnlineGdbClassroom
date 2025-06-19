package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.*;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.FileVisibilityInfo;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.web.HTMLEditor;
import javafx.stage.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;


public class MainMenuController implements Initializable {

    /**
     * General Json properties
     */
    @FXML private ChoiceBox<String> programmingLanguagesChoiceBox = new ChoiceBox<>();
    @FXML private HTMLEditor TaskHomeworkHTMLEditor = new HTMLEditor();
    @FXML private TextField NameHomeworkTextField;

    @FXML private TabPane inputTabPane;
    @FXML private Tab inputRegExpTab = new Tab(EnumTabOpened.REG_EXP.getDisplayName());
    @FXML private Tab inputTextFilesTab = new Tab(EnumTabOpened.FILES.getDisplayName());

    @FXML private TabPane outputTabPane;
    @FXML private Tab outputRegExpTab = new Tab(EnumTabOpened.REG_EXP.getDisplayName());
    @FXML private Tab outputTextFilesTab = new Tab(EnumTabOpened.FILES.getDisplayName());

    @FXML private TextArea codeTemplateTextArea = new TextArea();
    @FXML private TextArea modalSolutionTextArea = new TextArea();

    /** -------------------------------------------------
     * Making file form regEx
     * -------------------------------------------------
     */
    @FXML private TextField inputRegExTextField = new TextField();
    @FXML private TextField OutputRegExTextField = new TextField();
    @FXML private TextField inputRegExAmount = new TextField();
    @FXML private TextField OutputRegExAmount = new TextField();

    @FXML private Tooltip inputRegExTooltip = new Tooltip();
    @FXML private Tooltip outputRegExTooltip = new Tooltip();
    private String toolTipText = """
                Characters need escaping:
                Simple escape: ( ) . ?  ->  \\( \\) \\. \\?
                Double escape: { } \\   ->  \\\\{ \\\\} \\\\
                
                Special generating groups:
                {{FEMALE_FIRSTNAME}}{{FEMALE_LASTNAME}}
                {{MALE_FIRSTNAME}}  {{MALE_LASTNAME}}
                {{COUNTRY}}
                """;
    @FXML private CheckBox outputIdenticaltoInputRegExp;

    /** -------------------------------------------------
     * Making file form Files
     * -------------------------------------------------
     */
    @FXML private TableView<FileVisibilityInfo> inputTableView;
    @FXML private TableColumn<FileVisibilityInfo, String> inputFileNameTableColumn;
    @FXML private TableColumn<FileVisibilityInfo, String> inputFileLocationTableColumn;
    @FXML private TableColumn<FileVisibilityInfo, Boolean> inputFileTestVisible;
    @FXML private TableView<FileVisibilityInfo> outputTableView;
    @FXML private TableColumn<FileVisibilityInfo, String> outputFileNameTableColumn;
    @FXML private TableColumn<FileVisibilityInfo, String> outputFileLocationTableColumn;
    @FXML private TableColumn<FileVisibilityInfo, Boolean> outputFileTestVisible;

    @FXML private CheckBox AutoCleanInput;
    @FXML private CheckBox AutoCleanOutput;

    @FXML private CheckBox enableFileCustomInputSeparator;
    @FXML private CheckBox enableFileCustomOutputSeparator;

    @FXML private TextField fileCustomInputSeparator;
    @FXML private TextField fileCustomOutputSeparator;


    private final ObservableList<FileVisibilityInfo> inputTextFiles = FXCollections.observableArrayList();
    private final ObservableList<FileVisibilityInfo> outputTextFiles = FXCollections.observableArrayList();

    @FXML public void addInputFiles(ActionEvent event) {addFiles(EnumInputOutput.INPUT);}
    @FXML public void addOutputFiles(ActionEvent event) {addFiles(EnumInputOutput.OUTPUT);}

    public void addFiles(EnumInputOutput enumInputOutput) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(getLastDir());
        fileChooser.setTitle("Open Resource File");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        List<FileVisibilityInfo> newFiles = new ArrayList<>();
        for(File file : selectedFiles) {
            newFiles.add(new FileVisibilityInfo(file, true));
        }

        if (!selectedFiles.isEmpty()) {
            if (enumInputOutput == EnumInputOutput.INPUT) {
                inputTextFiles.addAll(newFiles);
            } else if (enumInputOutput == EnumInputOutput.OUTPUT) {
                outputTextFiles.addAll(newFiles);
            }

            // Saving last path
            File newLastDir = selectedFiles.getFirst().getParentFile();
            if (newLastDir != null && newLastDir.isDirectory()) {
                preferences.put(LAST_DIR_KEY, newLastDir.getAbsolutePath());
            }
        } else {
            System.out.println("No File Selected");
        }

    // Saving last path -------------------------------------------------

        File newLastDir = selectedFiles.get(0).getParentFile();
        if (newLastDir != null && newLastDir.isDirectory()) {
            preferences.put(LAST_DIR_KEY, newLastDir.getAbsolutePath());
        }
    }
    private static final String LAST_DIR_KEY = "lastDirectory";
    private static final String DEFAULT_DIR = System.getProperty("user.home") + File.separator + "Desktop";
    private final Preferences preferences = Preferences.userNodeForPackage(
            MainMenuController.class
    );

    private File getLastDir() {
        // get last Path
        String lastDirString = preferences.get(LAST_DIR_KEY, DEFAULT_DIR);
        File lastDir = new File(lastDirString);

        //Last path verification
        if (lastDir.isDirectory()) {
            return lastDir;
        } else {
            return new File(DEFAULT_DIR);
        }
    }

    /** -------------------------------------------------
     * Saving final file
     * -------------------------------------------------
     */
    public File saveJsonFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(getLastDir());
        directoryChooser.setTitle("Open Resource File");

        File initialDir = getLastDir();
        if (initialDir != null && initialDir.isDirectory()) {
            directoryChooser.setInitialDirectory(initialDir);
        } else {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        Window ownerWindow = (progressBar != null && progressBar.getScene() != null) ? progressBar.getScene().getWindow() : null;
        return directoryChooser.showDialog(ownerWindow);
    }

    /** -------------------------------------------------
     * General UI Controls
     * -------------------------------------------------
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set up language
        programmingLanguagesChoiceBox.getItems().addAll(ProgrammingLanguage.getAllDisplayNames());
        programmingLanguagesChoiceBox.getSelectionModel().select("Java");

        //tab names setUp
        inputRegExpTab.setText(EnumTabOpened.REG_EXP.getDisplayName());
        inputTextFilesTab.setText(EnumTabOpened.FILES.getDisplayName());
        outputRegExpTab.setText(EnumTabOpened.REG_EXP.getDisplayName());
        outputTextFilesTab.setText(EnumTabOpened.FILES.getDisplayName());

        //RegExp setup
        inputRegExTextField.setPromptText("/(reg)ex/");
        OutputRegExTextField.setPromptText("/(reg)ex/");
        inputRegExAmount.textProperty().bindBidirectional(OutputRegExAmount.textProperty());

        inputRegExTooltip.setText(toolTipText);
        outputRegExTooltip.setText(toolTipText);

        fileCustomInputSeparator.setDisable(false);
        outputIdenticaltoInputRegExp.selectedProperty().addListener((observable, oldValue, newValue) -> { OutputRegExTextField.setDisable(newValue);});

        inputFileNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFile().getName()));
        inputFileLocationTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFile().getAbsolutePath()));
        inputFileTestVisible.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isVisible()));
        inputFileTestVisible.setCellFactory(CheckBoxTableCell.forTableColumn(inputFileTestVisible));
        inputFileTestVisible.setEditable(true);

        inputTableView.setItems(inputTextFiles);
        inputTableView.setEditable(true);
        outputTableView.setItems(outputTextFiles);
        outputTableView.setEditable(true);

        // set custom file separator
        fileCustomInputSeparator.setPromptText("Separator \\n");
        fileCustomInputSeparator.setDisable(true);
        enableFileCustomInputSeparator.selectedProperty().addListener((observable, oldValue, newValue) -> { fileCustomInputSeparator.setDisable(!newValue);});
        fileCustomOutputSeparator.setPromptText("Separator \\n");
        fileCustomOutputSeparator.setDisable(true);
        enableFileCustomOutputSeparator.selectedProperty().addListener((observable, oldValue, newValue) -> { fileCustomOutputSeparator.setDisable(!newValue);});

    //UI adjastable size for RegExp / Files
        final double REGEXP_TAB_PANE_HEIGHT = 150;
        setupAdjustableTabPaneHeight(inputTabPane, inputRegExpTab, REGEXP_TAB_PANE_HEIGHT);
        setupAdjustableTabPaneHeight(outputTabPane, outputRegExpTab, REGEXP_TAB_PANE_HEIGHT);
    }
    private void setupAdjustableTabPaneHeight(TabPane tabPane, Tab regExpTab, double regExpHeight) {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) ->
                adjustTabPaneHeight(tabPane, newTab, regExpTab, regExpHeight) );
        adjustTabPaneHeight(tabPane, tabPane.getSelectionModel().getSelectedItem(), regExpTab, regExpHeight);
    }
    private void adjustTabPaneHeight(TabPane tabPane, Tab currentTab, Tab regExpTab, double regExpHeight) {
        if (currentTab == regExpTab) {
            tabPane.setPrefHeight(regExpHeight);
        } else {
            tabPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
        }
    }

    /** -------------------------------------------------
     * Algorithm for making final file
     * -------------------------------------------------
     */
    @FXML private ProgressBar progressBar;
    @FXML public void handleGenerateAction(ActionEvent event) {

        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        BuilderConnection builderConnection = null;

        try {
            //get input tab
            Tab selectedInputTabObject = inputTabPane.getSelectionModel().getSelectedItem();
            EnumTabOpened selectedInputTabEnum;
            if (selectedInputTabObject == inputRegExpTab) {
                selectedInputTabEnum = EnumTabOpened.REG_EXP;
            } else {
                selectedInputTabEnum = EnumTabOpened.FILES;
            }

            Tab selectedOutputTabObject = outputTabPane.getSelectionModel().getSelectedItem();
            EnumTabOpened selectedOutputTabEnum;
            if (selectedOutputTabObject == outputRegExpTab) {
                selectedOutputTabEnum = EnumTabOpened.REG_EXP;
            } else {
                selectedOutputTabEnum = EnumTabOpened.FILES;
            }



            builderConnection = new BuilderConnection(
                    NameHomeworkTextField.getText(),
                    programmingLanguagesChoiceBox.getValue(),
                    TaskHomeworkHTMLEditor.getHtmlText(),
                    selectedInputTabEnum, selectedOutputTabEnum,
                    /* Files */        inputTextFiles, outputTextFiles, AutoCleanInput.isSelected(), AutoCleanOutput.isSelected(), enableFileCustomInputSeparator.isSelected(),fileCustomInputSeparator.getText(), enableFileCustomOutputSeparator.isSelected(), fileCustomOutputSeparator.getText(),
                    /* RegEx */        inputRegExTextField.getCharacters().toString(), OutputRegExTextField.getCharacters().toString(), inputRegExAmount.getCharacters().toString(), OutputRegExAmount.getCharacters().toString(),
                    /* Code */         codeTemplateTextArea.getText(), modalSolutionTextArea.getText());

            //Create file
            FinalBuilder finalBuilder = new FinalBuilder(builderConnection);
            String jsonText = finalBuilder.process();
            finalBuilder.createJson(jsonText, saveJsonFile());
            progressBar.setProgress(100);

        } catch (FileNotFoundException e) {
            ErrorHendelerOuter.ErrorHendeler.resolve(e);
            progressBar.setProgress(0);
        } catch (RuntimeException e) {
            ErrorHendelerOuter.ErrorHendeler.resolve(e);
            progressBar.setProgress(0);
        }
    }
}
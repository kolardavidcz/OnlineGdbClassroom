package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.*;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums.*;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.enums.EnumTabOpened;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.support.ErrorHendelerOuter;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.support.UIFileManager;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.HTMLEditor;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Controller class for the main menu interface.
 * Handles all user interactions, file operations, and JSON generation for homework assignments.
 * Manages both RegExp and file-based input/output configurations.
 */
public class MainMenuController implements Initializable {

    /** -------------------------------------------------
     * UI Components for basic homework configuration
     * -------------------------------------------------
     */
    @FXML private TextField NameHomeworkTextField = new TextField();                    // Input field for homework name
    @FXML private ChoiceBox<String> programmingLanguagesChoiceBox = new ChoiceBox<>();  // Selection of programming language
    @FXML private HTMLEditor TaskHomeworkHTMLEditor = new HTMLEditor();                 // Rich text editor for homework description

    @FXML private TabPane inputTabPane = new TabPane();
    @FXML private Tab inputRegExpTab = new Tab(EnumTabOpened.REG_EXP.getDisplayName());
    @FXML private Tab inputTextFilesTab = new Tab(EnumTabOpened.FILES.getDisplayName());

    @FXML private TabPane outputTabPane = new TabPane();
    @FXML private Tab outputRegExpTab = new Tab(EnumTabOpened.REG_EXP.getDisplayName());
    @FXML private Tab outputTextFilesTab = new Tab(EnumTabOpened.FILES.getDisplayName());

    @FXML private TextArea codeTemplateTextArea = new TextArea();
    @FXML private TextArea modalSolutionTextArea = new TextArea();

    @FXML private ProgressBar progressBar; //setup later

    /** -------------------------------------------------
     * Regular Expression Input/Output Configuration
     * Includes fields for patterns, amounts, and identical output option
     * -------------------------------------------------
     */
    @FXML private TextField inputRegExTextField = new TextField();
    @FXML private TextField outputRegExTextField = new TextField();
    
    @FXML private TextField inputRegExAmount = new TextField();
    @FXML private TextField OutputRegExAmount = new TextField();

    @FXML private Tooltip regExTooltip = new Tooltip();
    @FXML private CheckBox outputIdenticalToInputRegExp = new CheckBox();

    /** -------------------------------------------------
     * Text File Input/Output Configuration
     * Includes file tables, auto-clean options, and custom separators
     * -------------------------------------------------
     */
    private ObservableList<File> inputTextFiles = FXCollections.observableArrayList();
    private ObservableList<File> outputTextFiles = FXCollections.observableArrayList();

    @FXML private TableView<File> imputTableView;
    @FXML private TableColumn<File, String> imputFileNameTableColumn;
    @FXML private TableColumn<File, String> imputFileLocationTableColumn;
    @FXML private TableView<File> outputTableView;
    @FXML private TableColumn<File, String> outputFileNameTableColumn;
    @FXML private TableColumn<File, String> outputFileLocationTableColumn;

    @FXML private CheckBox enableFileCustomInputSeparator;
    @FXML private CheckBox enableFileCustomOutputSeparator;
    @FXML private TextField fileCustomInputSeparator;
    @FXML private TextField fileCustomOutputSeparator;

    @FXML public void addInputFiles(ActionEvent event) {addFiles(EnumInputOutput.INPUT_TESTS);}
    @FXML public void addOutputFiles(ActionEvent event) {addFiles(EnumInputOutput.OUTPUT_TESTS);}
    @FXML public void removeAllInputFiles(ActionEvent event) {inputTextFiles.clear();}
    @FXML public void removeAllOutputFiles(ActionEvent event) {outputTextFiles.clear();}

    /* -------------- Saving fils with properities -------------------- */
    public void addFiles(EnumInputOutput enumInputOutput) {


        if(enumInputOutput == EnumInputOutput.INPUT_TESTS) {
            List<File> updatedList = UIFileManager.addFiles(enumInputOutput, inputTextFiles);
            ObservableList<File> observableUpdatedList = FXCollections.observableArrayList(updatedList);
            inputTextFiles.clear();
            inputTextFiles.addAll(observableUpdatedList);
        } else {
            List<File> updatedList = UIFileManager.addFiles(enumInputOutput, outputTextFiles);
            ObservableList<File> observableUpdatedList = FXCollections.observableArrayList(updatedList);
            outputTextFiles.clear();
            outputTextFiles.addAll(observableUpdatedList);
        }
    }

    /** -------------------------------------------------
     * General UI Controls
     * -------------------------------------------------
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        programmingLanguagesChoiceBox.getItems().addAll(ProgrammingLanguage.getAllDisplayNames());
        programmingLanguagesChoiceBox.getSelectionModel().select("Java");

        inputRegExpTab.setText(EnumTabOpened.REG_EXP.getDisplayName());
        inputTextFilesTab.setText(EnumTabOpened.FILES.getDisplayName());
        outputRegExpTab.setText(EnumTabOpened.REG_EXP.getDisplayName());
        outputTextFilesTab.setText(EnumTabOpened.FILES.getDisplayName());

        inputRegExTextField.setPromptText("/(reg)ex/");
        outputRegExTextField.setPromptText("/(reg)ex/");

        /**
         * Configure tooltip text with RegExp syntax help and special patterns
         * Includes examples of:
         * - Character escaping rules
         * - Special name / Geographic generation patterns
         */
        regExTooltip.setText("""
                Characters need escaping:
                Simple escape: ( ) . ?  ->  \\( \\) \\. \\?
                Double escape: { } \\   ->  \\\\{ \\\\} \\\\
                
                Special generating groups:
                {{FEMALE_FIRSTNAME}}{{FEMALE_LASTNAME}}
                {{MALE_FIRSTNAME}}  {{MALE_LASTNAME}}
                {{COUNTRY}}
                """);
        regExTooltip.setShowDelay(Duration.millis(300));
        regExTooltip.setShowDuration(Duration.millis(10_000));
        regExTooltip.setHideDelay(Duration.millis(200));
        inputRegExTextField.setTooltip(regExTooltip);
        outputRegExTextField.setTooltip(regExTooltip);

        //in case of output set to identical as input
        outputRegExTextField.setDisable(false);
        outputIdenticalToInputRegExp.selectedProperty().addListener((observable, oldValue, newValue) -> {
            outputRegExTextField.setDisable(newValue);
            OutputRegExAmount.setDisable(newValue);
        });

        //bind amout of input and output Reg Exp
        inputRegExAmount.textProperty().bindBidirectional(OutputRegExAmount.textProperty());

        //name collums in file table
        imputFileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        imputFileLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        outputFileNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        outputFileLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("path"));

        imputTableView.setItems(inputTextFiles);
        outputTableView.setItems(outputTextFiles);

        //custom file separator
        fileCustomInputSeparator.setPromptText("Separator \\n");
        fileCustomInputSeparator.setDisable(true);
        enableFileCustomInputSeparator.selectedProperty().addListener((observable, oldValue, newValue) -> fileCustomInputSeparator.setDisable(!newValue));
        fileCustomOutputSeparator.setPromptText("Separator \\n");
        fileCustomOutputSeparator.setDisable(true);
        enableFileCustomOutputSeparator.selectedProperty().addListener((observable, oldValue, newValue) -> fileCustomOutputSeparator.setDisable(!newValue));
    }

    /** -------------------------------------------------
     * Final JSON Generation and Export
     * Processes all configured settings and generates assignment JSON
     * Handles progress indication and error management
     * ------------------------------------------------- */
    @FXML public void handleGenerateAction(ActionEvent event) {

        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        BuilderConnection builderConnection;

        try {
            Tab selectedInputTabObject = inputTabPane.getSelectionModel().getSelectedItem();
            Tab selectedOutputTabObject = outputTabPane.getSelectionModel().getSelectedItem();

            builderConnection = new BuilderConnection(
            /* Basic info*/                 NameHomeworkTextField.getText(),  programmingLanguagesChoiceBox.getValue(), TaskHomeworkHTMLEditor.getHtmlText(),
            /* Selected Input/Output */     selectedInputTabObject.getText(), selectedOutputTabObject.getText(),

            /* FILES */                     inputTextFiles, outputTextFiles, /*screapted feature - auto remove empty string tests""*/ false, false,
            /* custom separator*/           enableFileCustomInputSeparator.isSelected(),fileCustomInputSeparator.getText(), enableFileCustomOutputSeparator.isSelected(), fileCustomOutputSeparator.getText(),

            /* REG_EXP */                   inputRegExTextField.getCharacters().toString(), outputRegExTextField.getCharacters().toString(), inputRegExAmount.getCharacters().toString(), OutputRegExAmount.getCharacters().toString(), outputIdenticalToInputRegExp.isSelected(),
            /* Code samples*/               codeTemplateTextArea.getText(), modalSolutionTextArea.getText());

            FinalBuilder finalBuilder = new FinalBuilder(builderConnection);
            JSONObject jsonObject = finalBuilder.process();

            File saveDirectory = UIFileManager.setJsonFileDestination();

            if (saveDirectory != null) {
                finalBuilder.createJson(jsonObject, saveDirectory, NameHomeworkTextField.getText());
                progressBar.setProgress(100);
            } else {
                System.out.println("Save operation cancelled by user.");
                progressBar.setProgress(0);
            }

        } catch (IOException | RuntimeException e) {
            ErrorHendelerOuter.ErrorHendeler.resolve(e); //my custom error pop-up
            progressBar.setProgress(0);
        }
    }
}

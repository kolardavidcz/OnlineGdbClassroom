package cz.vscht.bioinformatics.kolar.onlinegdbclassroom;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.enums.EnumTabOpened;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums.ProgrammingLanguage;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.FilesCollector;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.RegExpCollector;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.TestCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Manages the connection between UI builder components and test generation logic.
 * This class handles the creation and management of programming exercises, including
 * input/output test cases, code templates, and solutions.
 */
public class BuilderConnection {

    //  ROW
    /** Row name, purified later in MyJsonFormatBuilder*/
    public String name;
    /** Raw Enum, text is extracted from UI later in MyJsonFormatBuilder*/
    public ProgrammingLanguage programmingLanguage;
    /** Raw HTML content  - purified later in MyJsonFormatBuilder*/
    public String htmlText;
    /** Raw template code - purified later in MyJsonFormatBuilder*/
    public String codeTemplate;
    /** Raw solution code - purified later in MyJsonFormatBuilder*/
    public String modalSolution;


    private TestCollector input;
    private TestCollector outputs;

    private ArrayList<String> testInputs;
    private Optional<List<String>> testNames;
    private boolean outputsIdenticalAsInputs;

    private final String regexTab = EnumTabOpened.REG_EXP.getDisplayName();
    private final String filesTab = EnumTabOpened.FILES.getDisplayName();

    /**
     * Constructs a new BuilderConnection instance to create a programming exercise.
     * <p>
     * This constructor initializes all the exercise properties and test cases by processing
     * either file-based inputs/outputs or regex-generated test data.
     * <p>
     * Basic Info Parameters:
     *
     * @param name                          The name/title of the programming exercise (must not be empty)
     * @param programmingLanguage           Language identifier (e.g. "java", "python")
     * @param htmlText                      Exercise description/instructions in HTML format
     *                                      <p>
     *                                      Test Case Source Parameters:
     * @param UIselectedInputTab            Tab selected for inputs ("Files" or "Reg Exp")
     * @param UIselectedOutputTab           Tab selected for outputs ("Files" or "Reg Exp")
     *                                      <p>
     *                                      File Input Parameters:
     * @param UIinputTextFiles              List of files containing test input cases
     * @param UIoutputTextFiles             List of files containing expected outputs
     * @param UIAutoCleaninput              Whether to auto-clean input file contents
     * @param UIAutoCleanOutput             Whether to auto-clean output file contents
     *                                      <p>
     *                                      File Separator Parameters:
     * @param UIEnableCustomInputSeparator  Whether custom input separator is enabled
     * @param UIFileCustominputSeperator    Custom separator string for input files
     * @param UIEnableCustomOutputSeparator Whether custom output separator is enabled
     * @param UIFileCustomOutputSeperator   Custom separator string for output files
     *                                      <p>
     *                                      RegExp Parameters:
     * @param UIinputRegEx                  Regular expression pattern for generating inputs
     * @param UIOutputRegEx                 Regular expression pattern for generating outputs
     * @param UIinputRegExAmout             Number of input cases to generate from regex
     * @param UIOutputRegExAmout            Number of output cases to generate from regex
     * @param outputIdenticalToInput        Whether outputs should match inputs
     *                                      <p>
     *                                      Code Parameters:
     * @param UIcodeTemplateTextArea        Template/starter code for the exercise
     * @param UImodalSolutionTextArea       Model solution code
     * @throws FileNotFoundException    If any input/output files cannot be found
     * @throws IllegalArgumentException If exercise name is empty
     */
    public BuilderConnection(
        /* Basic info*/                 String name, String programmingLanguage, String htmlText,
        /* Selected Input/Output */     String UIselectedInputTab, String UIselectedOutputTab,
        
        /* FILES */                     List<File> UIinputTextFiles, List<File> UIoutputTextFiles, boolean UIAutoCleaninput, boolean UIAutoCleanOutput,
        /* custom separator*/           boolean UIEnableCustomInputSeparator, String UIFileCustominputSeperator, boolean UIEnableCustomOutputSeparator, String UIFileCustomOutputSeperator,
        
        /* REG_EXP */                   String UIinputRegEx, String UIOutputRegEx, String UIinputRegExAmout, String UIOutputRegExAmout, boolean outputIdenticalToInput,
        
        /* Code samples*/               String UIcodeTemplateTextArea, String UImodalSolutionTextArea)
        /* throws */                    throws FileNotFoundException {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.htmlText = htmlText;                       // Raw HTML content  - purified later in MyJsonFormatBuilder
        this.codeTemplate = UIcodeTemplateTextArea;     // Raw template code - purified later in MyJsonFormatBuilder
        if(UIcodeTemplateTextArea.isEmpty()) {
            this.codeTemplate = """
                    #include <stdio.h>
                    #include <stdlib.h>
                    
                    int main ( int argc, char * argv [] ) {
                        //TODO
                        return 0;
                    }""";
        }
        this.modalSolution = UImodalSolutionTextArea;   // Raw solution code - purified later in FormatBuilder

        testInputs = null;                              // Changed if getInputs was called
        testNames = Optional.empty();

        // Convert and validate programming language
        this.programmingLanguage = ProgrammingLanguage.getProgramingLanguageEnum(programmingLanguage);
        this.outputsIdenticalAsInputs = outputIdenticalToInput;


        // INPUT source based on selected tab
        boolean isInputRegExp = Objects.equals(UIselectedInputTab, regexTab);
        if (isInputRegExp) {
            
            int inpitRegExAmout = purifyNumbers(UIinputRegExAmout);
            input = new RegExpCollector(UIinputRegEx, inpitRegExAmout);
            
        } else /* fileTab */ {

            Optional<String> inputSeparator = getCustomSeparator(UIEnableCustomInputSeparator, UIFileCustominputSeperator);
            input = new FilesCollector(UIinputTextFiles, UIAutoCleaninput, inputSeparator);
            testNames = Optional.of(input.getFileNames()); //set test names
        }

        // OUTPUT source based on selected tab
        boolean isOutputRegExp = Objects.equals(UIselectedOutputTab, regexTab);
        if (isOutputRegExp) {
            
            if (!outputsIdenticalAsInputs) {
                
                int outputRegExAmout = purifyNumbers(UIOutputRegExAmout);
                outputs = new RegExpCollector(UIOutputRegEx, outputRegExAmout);

            } else {
                //REDUNDENT data to an output array will be copied from input array
            }
            
        } else /* fileTab */ {

            Optional<String> outputSeparator = getCustomSeparator(UIEnableCustomOutputSeparator, UIFileCustomOutputSeperator);
            outputs = new FilesCollector(UIoutputTextFiles, UIAutoCleanOutput, outputSeparator);

            if(testNames.isEmpty()) { //set test names
                testNames = Optional.of(outputs.getFileNames());
            }
        }
    }

    /**
     * Retrieves the list of test input cases.
     * @return ArrayList containing all test input strings
     */
    public ArrayList<String> getInputs() {

        testInputs = new ArrayList<>(input.makeTestList());
        return testInputs;
    }

    /**
     * Retrieves the list of expected test output cases.
     * If outputs are configured to be identical to an input list. (getInputs must be called first)
     *
     * @return ArrayList containing all test output strings
     * @throws RuntimeException If outputs are set to match inputs, but inputs haven't been called yet (getInputs)
     */
    public ArrayList<String> getOutputs() {
        
        if(!outputsIdenticalAsInputs) {
            return outputs.makeTestList();
        }

        if(testInputs == null || testInputs.isEmpty()) {
            throw new RuntimeException("You have to run inputs first (testInputs is null)");
        }
        return testInputs;
    }

    /**
     * Purifies the provided number string to ensure it is a valid integer value greater than 0.
     * @throws NumberFormatException If the provided number string is not greater then 0
     */
    private int purifyNumbers(String number) {
        try {
            int purifiedNumber = Integer.parseInt(number);
            if(purifiedNumber <= 0) {
                throw new IllegalArgumentException("Amount of RegExp to generate must by number greater then 0");
            }
            return purifiedNumber;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount of RegExp to generate must by number greater then 0");
        }
    }

    /**
     * Optional<String> is used as small class info if there even is a custom separator and in such a case, what is it.
     */
    private Optional<String> getCustomSeparator(boolean enabled, String separator) {
        if(enabled) {
            return Optional.of(separator);
        }
        return Optional.empty();
    }

    public Optional<List<String>> getTestNames() {
        if (testNames == null || testNames.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new ArrayList<String>(testNames.get()));
    }
}

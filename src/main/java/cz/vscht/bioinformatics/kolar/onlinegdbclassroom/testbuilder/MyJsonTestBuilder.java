package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.Main;
import java.util.*;

/**
 * The TestBuilder class creates test cases in JSON format for Online GDB Classroom.
 * It manages pairs of input and output strings and converts them into a list of maps,
 * suitable for serialization by Jackson. The class ensures that input and output
 * lists are properly matched and non-empty.
 */
public class MyJsonTestBuilder {

    private List<String> inputs;
    private List<String> outputs;
    private List<String> testNames;

    private int numberOfTests;

    /**
     * Creates a new TestBuilder instance with the specified input and output test cases.
     *
     * @param inputs  List of input strings for the test cases
     * @param outputs List of expected output strings for the test cases
     * @param testNames Optional list of names for the test cases.
     * @throws IllegalArgumentException if inputs is empty, outputs is empty, or if inputs and outputs sizes don't match
     */
    public MyJsonTestBuilder(List<String> inputs, List<String> outputs, Optional<List<String>> testNames) {

        if (inputs.isEmpty()){
            throw new IllegalArgumentException("inputs not set");
        }
        if (outputs.isEmpty()){
            throw new IllegalArgumentException("Outputs not set");
        }
        if(inputs.size() != outputs.size()){
            throw new IllegalArgumentException("inputs ("+ inputs.size() +") and outputs ("+ outputs.size() +") size does not match");
        }

        this.inputs = new ArrayList<>(inputs);
        this.outputs = new ArrayList<>(outputs);
        numberOfTests = inputs.size();

        if(testNames.isEmpty()){
            this.testNames = new ArrayList<>();
            for(int i = 0; i < numberOfTests; i++){
                this.testNames.add("testNumber: " + i);
            }
        } else {
            this.testNames = new ArrayList<>(testNames.get());
        }
        if(this.testNames.size() != inputs.size()){
            throw new RuntimeException("Test names size (" + this.testNames.size() + ") does not match inputs size (" + inputs.size() + ")");
        }
    }
    public MyJsonTestBuilder(List<String> inputs, List<String> outputs) {
        this(inputs, outputs, Optional.empty());
    }

    /**
     * Generates a list of maps containing all test cases, which can be easily
     * serialized into a JSON array by Jackson. Each test case is a map with
     * the following properties:
     * - input: test case input string
     * - output: expected output string
     * - name: unique test identifier
     * - match_type: strict matching algorithm (S)
     * - grade_point: test case score value
     * - visibility: test visibility level
     *
     * @return List of maps, each representing a test case.
     */
    public List<Map<String, Object>> getTests() {

        // Použijeme ArrayList pro pole a LinkedHashMap pro každý JSON objekt.
        List<Map<String, Object>> tests = new ArrayList<>();

        for(int i = 0; i < numberOfTests; i++){
            // LinkedHashMap preserves the insertion order of keys.
            Map<String, Object> testObject = new LinkedHashMap<>();
            testObject.put("input", inputs.get(i));
            testObject.put("output", outputs.get(i));
            testObject.put("name", testNames.get(i));
            testObject.put("match_type", Main.MATCHING_SENSITIVITY);       //default "S" for Strict
            testObject.put("grade_point", 1);
            testObject.put("visibility", Main.TEST_VISIBLE);               //default "2" for visible to students

            tests.add(testObject);
        }
        return tests;
    }
}
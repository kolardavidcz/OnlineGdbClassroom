package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * The TestBuilder class creates test cases in JSON format for Online GDB Classroom.
 * It manages pairs of input and output strings and converts them into a JSON array
 * suitable for automated testing purposes. The class ensures that input and output
 * lists are properly matched and non-empty.
 */
public class MyJsonTestBuilder {

    private List<String> inputs;
    private List<String> outputs;
    private int numberOfTests;

    /**
     * Creates a new TestBuilder instance with the specified input and output test cases.
     *
     * @param inputs  List of input strings for the test cases
     * @param outputs List of expected output strings for the test cases
     * @throws IllegalArgumentException if inputs is empty, outputs is empty, or if inputs and outputs sizes don't match
     */
    public MyJsonTestBuilder(List<String> inputs, List<String> outputs) {

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
    }

    /**
     * Generates a JSON array containing all test cases for Online GDB Classroom.
     * Each test case is formatted as a JSON object with the following properties:
     * - input: test case input string
     * - output: expected output string
     * - name: unique test identifier
     * - match_type: strict matching algorithm (S)
     * - grade_point: test case score value
     * - visibility: test visibility level
     *
     * @return JSONArray containing all formatted test cases
     */
    public JSONArray getTests() {

        JSONArray tests = new JSONArray();

        for(int i = 0; i < numberOfTests; i++){
            tests.put(i, new JSONObject()
                    .put("input", inputs.get(i))
                    .put("output", outputs.get(i))
                    .put("name", "testNumber: " + i)
                    .put("match_type", Main.MATCHING_SENSITIVITY)       //default "S" for Strict
                    .put("grade_point", 1)
                    .put("visibility", Main.TEST_VISIBLE)   //default "2" for visible to students
            );
        }
        return tests;
    }
}

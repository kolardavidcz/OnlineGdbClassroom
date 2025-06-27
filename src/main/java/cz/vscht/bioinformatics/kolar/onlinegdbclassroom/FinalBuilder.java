package cz.vscht.bioinformatics.kolar.onlinegdbclassroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.MyJsonFormatBuilder;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.MyJsonTestBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * FinalBuilder is responsible for creating and formatting assignment files in JSON format.
 * It combines formatting and test case information to generate complete assignment structures.
 * This version uses the Jackson library for reliable JSON processing.
 */
public class FinalBuilder {

    private final BuilderConnection builderConnection;
    private final ObjectMapper objectMapper;

    /**
     * Constructor for FinalBuilder.
     * Initializes the Jackson ObjectMapper for JSON serialization.
     *
     * @param builderConnection The connection object containing all data for the assignment.
     */
    public FinalBuilder(BuilderConnection builderConnection) {
        this.builderConnection = builderConnection;
        // Initialize the ObjectMapper and enable pretty printing
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Processes the builder connection data to create a complete assignment JSON structure.
     * Combines formatting information and test cases into a single Map object that can be
     * easily serialized to JSON by Jackson.
     *
     * @return Map<String, Object> containing the complete assignment structure.
     */
    public Map<String, Object> process() {
        // We assume MyJsonFormatBuilder and MyJsonTestBuilder are rewritten
        // to work with standard Java collections (Map, List) instead of org.json classes.
        MyJsonFormatBuilder formatBuilder = new MyJsonFormatBuilder(
                builderConnection.name,
                builderConnection.htmlText,
                builderConnection.programmingLanguage,
                builderConnection.codeTemplate,
                builderConnection.modalSolution
        );
        MyJsonTestBuilder myJsonTestBuilder = new MyJsonTestBuilder(
                builderConnection.getInputs(),
                builderConnection.getOutputs(),
                builderConnection.getTestNames()
        );

        // Use LinkedHashMap to guarantee the order of keys.
        Map<String, Object> root = new LinkedHashMap<>();

        // This assumes getIntroductionFormat() now returns a Map<String, Object>
        Map<String, Object> assignment = formatBuilder.getIntroductionFormat();

        // This assumes getTests() returns a List<Map<String, Object>>
        assignment.put("testcases", myJsonTestBuilder.getTests());

        // Place keys in the desired order
        root.put("version", 1);
        root.put("assignment", assignment);

        return root;
    }

    /**
     * Creates a JSON file from the provided JSON object (Map) at the specified path.
     *
     * @param jsonOutput The JSON object (represented as a Map) to write to file.
     * @param path       The directory path where the assignment.json file should be created.
     * @param fileName   The name of the file to create.
     * @throws RuntimeException if the file cannot be created or written to.
     */
    public void createJson(Map<String, Object> jsonOutput, File path, String fileName) {
        String name = purifyFileName(fileName);
        File assignmentFile = new File(path, name + ".json");

        if (!Files.exists(path.toPath())) {
            try {
                Files.createDirectories(path.toPath());
            } catch (IOException e) {
                throw new RuntimeException(".json file could not be created: " + e.getMessage(), e);
            }
        }

        System.out.println("Creating json file: " + assignmentFile.getPath());

        try {
            // Jackson handles writing the JSON to the file in a clean and efficient way.
            // This replaces the manual BufferedWriter and FileWriter logic.
            objectMapper.writeValue(assignmentFile, jsonOutput);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to .json file: " + e.getMessage(), e);
        }
    }

    /**
     * Purifies a file name by replacing special characters and spaces.
     * This method remains unchanged from the original.
     *
     * @param fileName The original file name.
     * @return A purified file name.
     */
    private String purifyFileName(String fileName) {
        String name = fileName
                //national alphabet
                .replaceAll("[áä]", "a").replaceAll("[č]", "c").replaceAll("[ď]", "d")
                .replaceAll("[éě]", "e").replaceAll("[í]", "i").replaceAll("[ň]", "n")
                .replaceAll("[óö]", "o").replaceAll("[ř]", "r").replaceAll("[š]", "s")
                .replaceAll("[ť]", "t").replaceAll("[úůü]", "u").replaceAll("[ý]", "y")
                .replaceAll("[ž]", "z")
                .replaceAll("[ÁÄ]", "A").replaceAll("[Č]", "C").replaceAll("[Ď]", "D")
                .replaceAll("[ÉĚ]", "E").replaceAll("[Í]", "I").replaceAll("[Ň]", "N")
                .replaceAll("[ÓÖ]", "O").replaceAll("[Ř]", "R").replaceAll("[Š]", "S")
                .replaceAll("[Ť]", "T").replaceAll("[ÚŮÜ]", "U").replaceAll("[Ý]", "Y")
                .replaceAll("[Ž]", "Z")
                .replaceAll(" ", "_")
                .replaceAll("[^a-zA-Z0-9_-]", "");
        if (name.isEmpty()) {
            name = "assignment";
        }
        return name;
    }
}
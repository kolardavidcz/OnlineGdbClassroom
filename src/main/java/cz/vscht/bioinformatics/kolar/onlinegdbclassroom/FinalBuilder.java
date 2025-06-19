package cz.vscht.bioinformatics.kolar.onlinegdbclassroom;


import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.MyJsonFormatBuilder;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.MyJsonTestBuilder;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * FinalBuilder is responsible for creating and formatting assignment files in JSON format.
 * It combines formatting and test case information to generate complete assignment structures.
 */
public class FinalBuilder {

    BuilderConnection builderConnection;
    public FinalBuilder(BuilderConnection builderConnection) {
        this.builderConnection = builderConnection;
    }

    /**
     * Processes the builder connection data to create a complete assignment JSON structure.
     * Combines formatting information and test cases into a single JSON object.
     *
     * @return JSONObject containing the complete assignment structure
     */
    public JSONObject process() {

        //general info
        MyJsonFormatBuilder formatBuilder = new MyJsonFormatBuilder(
                builderConnection.name,
                builderConnection.htmlText,
                builderConnection.programmingLanguage,
                builderConnection.codeTemplate, builderConnection.modalSolution
        );
        //individual test cases
        MyJsonTestBuilder myJsonTestBuilder = new MyJsonTestBuilder(
                builderConnection.getInputs(),
                builderConnection.getOutputs()
        );

        JSONObject root = new JSONObject();
        JSONObject assignment = formatBuilder.getIntroductionFormat();
        assignment.put("testcases", myJsonTestBuilder.getTests());

        root.put("assignment", assignment);
        root.put("version", 1);
        return root;
    }

    /**
     * Creates a JSON file from the provided JSON object at the specified path.
     *
     * @param jsonOutput The JSON object to write to file
     * @param path       The directory path where the assignment.json file should be created
     * @throws RuntimeException if the file cannot be created or written to
     */
    public void createJson(JSONObject jsonOutput, File path, String fileName) {

        String name = purifyFileName(fileName);
        File assignment = new File(path.getPath() + File.separator + name + ".json");

        if (!Files.exists(path.toPath())) {
            try {
                Files.createDirectories(path.toPath());
            } catch (IOException e) {
                throw new RuntimeException(".json file cound not be created: " + e);
            }
        }

        System.out.println("Creating json file: " + path.getPath() + File.separator + name + ".json");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(assignment))) {
            assignment.createNewFile(); //ignoring if the file exist or not -> overriding original file
            bufferedWriter.write(jsonOutput.toString(2));

        } catch (IOException e) {
            throw new RuntimeException("couldn't write to .json file: " + e);
        }
    }

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
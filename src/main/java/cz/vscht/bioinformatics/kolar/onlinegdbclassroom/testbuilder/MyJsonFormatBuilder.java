package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums.ProgrammingLanguage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;


/**
 * Builder class for creating formatted JSON assignments for online GDB classroom.
 * Handles the creation of programming assignments with specific formatting requirements.
 */
public class MyJsonFormatBuilder {
    private String title;
    private String instructions;
    private ProgrammingLanguage programmingLanguage;
    private String codeTemplate;
    private String modelSolution;

    /**
     * Constructs a new FormatBuilder with the specified parameters.
     *
     * @param title         The title of the programming assignment
     * @param instructions  The HTML-formatted instructions for the assignment (purified)
     * @param language      The programming language for the assignment
     * @param codeTemplate  The template code provided to students (not purified)
     * @param modelSolution The model solution for the assignment (not purified)
     */
    public MyJsonFormatBuilder(String title, String instructions, ProgrammingLanguage language, String codeTemplate, String modelSolution) {
        this.title = title;
        this.instructions = Jsoup.parseBodyFragment(instructions).body().html();
        this.programmingLanguage = language;
        this.codeTemplate = codeTemplate;   //still not purified
        this.modelSolution = modelSolution; //still not purified
    }

    /**
     * Generates a JSON format for the assignment introduction.
     * Creates a structured JSON object containing all necessary assignment details
     * including title, instructions, programming language, and code templates.
     *
     * @return JSONObject containing the formatted assignment details
     */
    public JSONObject getIntroductionFormat() {
       JSONObject assignment = new JSONObject();

        //NOTED: Input form REQUIRES specific order
        String tamplate = "[{\"name\":\"main" + programmingLanguage.getFileExtension() + "\",\"content\":" + clean(codeTemplate) + ",\"readonly_ranges\":[]}]";
        String solution = "[{\"name\":\"main" + programmingLanguage.getFileExtension() + "\",\"content\":" + clean(modelSolution) + ",\"readonly_ranges\":[]}]";

        assignment.put("title", title)
        .put("content", instructions)
        .put("language", programmingLanguage.getApiValue())
        .put("code_template", tamplate)
        .put("modal_solution", solution)
        .put("eval_method", "auto_grade")
        .put("enable_grade", 1)
        .put("enable_late_submission", 1)
        .put("enable_auto_mark_complete", JSONObject.NULL)
        .put("editor_copy_paste", 1)
        .put("editor_file_upload", 1)
        .put("autolock", 0)
        .put("testcase_visible", 0)
        .put("code_template_readonly_ranges", JSONObject.NULL)
        .put("testcases", new JSONArray());

        return assignment;
    }

    /**
     * @param toClean The string to be cleaned and quoted
     * @return A JSON-safe quoted string
     */
    public String clean (String toClean) {
        return JSONObject.quote(toClean);
    }
}

/** @tamplate,solution REQUIRES specific order, so puting them in through JSONObject is REDUNDANT
 *
 *  In case of refactoring to different Json generator:

        JSONArray codeTemplateArray = new JSONArray();
        JSONObject codeTemplateObject = new JSONObject();
        codeTemplateObject.put("name", "main" + programmingLanguage.getFileExtension());
        codeTemplateObject.put("content", clean(codeTemplate));
        codeTemplateObject.put("readonly_ranges", new JSONArray());
        codeTemplateArray.put(codeTemplateObject);

        JSONArray modelSolutionArray = new JSONArray();
        JSONObject modelSolutionObject = new JSONStringer();
        modelSolutionObject.put("name", "main" + programmingLanguage.getFileExtension());
        modelSolutionObject.put("content", modelSolution);  // JSONObject will handle escaping
        modelSolutionObject.put("readonly_ranges", new JSONArray());
        modelSolutionArray.put(modelSolutionObject);

*/
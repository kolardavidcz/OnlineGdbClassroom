package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums.ProgrammingLanguage;
import org.jsoup.Jsoup;

import java.util.*;


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
    public Map<String, Object> getIntroductionFormat() {

        Map<String, Object> assignment = new LinkedHashMap<>();


        // Vytvoření pole pro šablonu kódu
        List<Map<String, Object>> codeTemplateArray = new ArrayList<>();
        Map<String, Object> codeTemplateObject = new LinkedHashMap<>();
        codeTemplateObject.put("name", "main" + programmingLanguage.getFileExtension());
        // Zde použijte vaši metodu 'clean()', pokud je nutná nějaká úprava řetězce
        // před vložením. Jackson automaticky ošetří uvozovky a speciální znaky,
        // takže byste ho již neměl obalovat stringy JSONu.
        codeTemplateObject.put("content", codeTemplate);
        codeTemplateObject.put("readonly_ranges", new ArrayList<>());
        codeTemplateArray.add(codeTemplateObject);

        // Vytvoření pole pro referenční řešení
        List<Map<String, Object>> modelSolutionArray = new ArrayList<>();
        Map<String, Object> modelSolutionObject = new LinkedHashMap<>();
        modelSolutionObject.put("name", "main" + programmingLanguage.getFileExtension());
        // Stejně jako výše, neobalujte stringem JSONu. Jackson to udělá za vás.
        modelSolutionObject.put("content", modelSolution);
        modelSolutionObject.put("readonly_ranges", new ArrayList<>());
        modelSolutionArray.add(modelSolutionObject);
        //purification of codeTemplate and modelSolution is not needed here, as they are already strings.
        ObjectMapper tempObjectMapper = new ObjectMapper();
        String codeTemplateAsString = "";
        String modelSolutionAsString = "";
        try {
            codeTemplateAsString = tempObjectMapper.writeValueAsString(codeTemplateArray);
            modelSolutionAsString = tempObjectMapper.writeValueAsString(modelSolutionArray);
        } catch (Exception e) {
            System.err.println("Chyba při serializaci pole na string: " + e.getMessage());
        }

        assignment.put("title", title);
        assignment.put("content", instructions);
        assignment.put("language", programmingLanguage.getApiValue());
        // Zde už přidáváme List a Mapy, ne stringy s JSONem, Jackson je zserializuje správně.
        assignment.put("code_template", codeTemplateAsString);
        assignment.put("modal_solution", modelSolutionAsString);
        assignment.put("eval_method", "auto_grade");
        assignment.put("enable_grade", 1);
        assignment.put("enable_late_submission", 1);
        // Pro 'null' hodnoty použijte v Javě 'null'.
        assignment.put("enable_auto_mark_complete", null);
        assignment.put("editor_copy_paste", 1);
        assignment.put("editor_file_upload", 1);
        assignment.put("autolock", null);
        assignment.put("testcase_visible", 0);
        assignment.put("code_template_readonly_ranges", null);
        assignment.put("testcases", new ArrayList<>()); // Prázdné pole testovacích případů.



        return assignment;
    }
}
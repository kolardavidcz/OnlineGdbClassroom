package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass;

import com.github.curiousoddman.rgxgen.RgxGen;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.regExpSpecialGroups.SpecialGroups;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * A class for handling smart regular expressions that can generate test strings matching the pattern.
 * Supports special group substitutions from predefined text files.
 */
public class RegExpCollector extends TestCollector {
    private String regExp;

    //custom RegExp
    private final Path currentPath;
    /** K: RegExp Special Group Name, V: List of possible values in the group */
    private Map<String, List<String>> specialGroups;

    private static final Random random = new Random();

    /**
     * Creates a new SmartRegExp instance.
     * @param regExp           The regular expression pattern
     * @param inputsToGenerate Number of test strings to generate
     * @throws IllegalArgumentException if regExp is empty or inputsToGenerate is <= 0
     */
    public RegExpCollector(String regExp, int inputsToGenerate) {
        this.regExp = regExp;
        if(regExp == null || regExp.isEmpty()) {
            throw new IllegalArgumentException("Regular expression can not be empty");
        }

        if(inputsToGenerate <= 0) {
            throw new IllegalArgumentException("Amount of RegExp to generate must by number greater then 0");
        }
        this.numberOfTests = inputsToGenerate;

        //initialization
        specialGroups = new HashMap<>();
        currentPath = Path.of(System.getProperty("user.dir")).resolve("src").resolve("main").resolve("java").resolve("cz").resolve("vscht").resolve("bioinformatics").resolve("kolar").resolve("onlinegdbclassroom").resolve("smartClass").resolve("regExpSpecialGroups");
    }

    public String getRegExp() { return regExp; }

    /**
     * Generates a list of test strings matching the regular expression pattern.
     * Handles special group substitutions from predefined files.
     *
     * @return ArrayList of generated test strings
     */
    @Override
    public ArrayList<String> makeTestList() {
        String customRegExp = "\\{\\{" + SpecialGroups.getSpecialGroupsRegExp() + "\\}\\}";
        Pattern pattern =  Pattern.compile(customRegExp);

        regExp = regExp.replaceAll(customRegExp, "(\\\\{\\\\{$1\\\\}\\\\})");

        return RgxGen.parse(regExp) // RgxGen makes stream of matching strings
                .stream()
                .limit(numberOfTests)
                .map(text -> {
                    Matcher macher = pattern.matcher(text);
                    while(macher.find()) {
                        text = text.replace(macher.group(), getTextToReplaceSpecialGroups(macher.group()));
                        macher = pattern.matcher(text);
                    }
                    return text;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<String> getFileNames() {
        throw new IllegalArgumentException("RegExpCollector does not support getFileNames() method.");
    }

    /**
     * Replaces special group placeholders with random values from corresponding files.
     *
     * @param groupID The name of the special group to replace
     * @return A randomly selected value from the group's possible values
     * @throws IllegalArgumentException if the group file cannot be read
     */
    private String getTextToReplaceSpecialGroups(String groupID) {
        groupID = groupID   .replace("{{", "")
                            .replace("}}", "");

        //Load list from files if needed (eg. Names, Countries, etc.)
        boolean groupIDExist = specialGroups.containsKey(groupID);
        String groupFileName = groupID + ".txt";
        if (!groupIDExist) {
            try(Stream<String> linesStream = Files.lines(currentPath.resolve(groupFileName))) {
                specialGroups.put(groupID, linesStream.toList());

            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to read or process file for group: " + groupID + ".txt\n Error: " + e.getMessage());
            }
        }

        int specialGroupsSize = specialGroups.get(groupID).size();
        return specialGroups.get(groupID)                   //get the right List from Map
                .get(random.nextInt(specialGroupsSize));    // get random String
    }
}

package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents supported programming languages in the Online GDB Classroom system.
 * Each enum constant contains:
 * - apiValue: The identifier used in API communications
 * - displayName: The human-readable name shown in the UI
 * - fileExtension: The associated file extension for source files
 * <p>
 * The enum provides methods to:
 * - Convert between display names and enum constants
 * - Get file extensions for languages
 * - Retrieve API-compatible language identifiers
 * - List all available programming languages
 */
public enum ProgrammingLanguage {

    C("c", "C", ".c"),
    CPP("c++", "C++", ".cpp"),
    CPP14("c++14", "C++ 14", ".cpp"),
    CPP17("c++17", "C++ 17", ".cpp"),
    JAVA("java", "Java", ".java"),
    PYTHON_3("python", "Python 3", ".py"),
    KOTLIN("kotlin", "Kotlin", ".kt"),
    PHP("php", "PHP", ".php"),
    CSHARP("c#", "C#", ".cs"),
    OCAML("ocaml", "OCaml", ".ml"),
    VB("vb", "VB", ".vb"),
    HTML_JS_CSS("html", "HTML,JS,CSS", ".html"),
    RUBY("ruby", "Ruby", ".rb"),
    PERL("perl", "Perl", ".pl"),
    PASCAL("pascal", "Pascal", ".pas"),
    COBOL("cobol", "Cobol", ".cbl"),
    R("r", "R", ".R"),
    FORTRAN("fortran", "Fortran", ".f90"),
    HASKELL("haskell", "Haskell", ".hs"),
    ASSEMBLY_GCC("asm_gcc", "Assembly(GCC)", ".asm"),
    OBJECTIVE_C("objc", "Objective C", ".m"),
    SQLITE3("sqlite3", "SQLite", ".sql"),
    JAVASCRIPT_RHINO("js_rhino", "Javascript(Rhino)", ".js"),
    JAVASCRIPT_NODE("js_node", "Javascript(Node)", ".js"),
    PROLOG("prolog", "Prolog", ".pl"),
    SWIFT("swift", "Swift", ".swift"),
    RUST("rust", "Rust", ".rs"),
    GO("go", "Go", ".go"),
    BASH("bash", "Bash", ".sh");

    private final String apiValue;
    private final String displayName;
    private final String fileExtension;

    ProgrammingLanguage(String apiValue, String displayName, String fileExtension) {
        this.apiValue = apiValue;
        this.displayName = displayName;
        this.fileExtension = fileExtension;
    }

    public String getApiValue() {
        return apiValue;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Converts a programming language name or API value to its corresponding enum constant.
     * Default value is JAVA if null is provided.
     *
     * @param name The display name or API value of the programming language
     * @return The corresponding ProgrammingLanguage enum constant
     * @throws IllegalArgumentException if the provided name does not match any known programming language
     */
    public static ProgrammingLanguage getProgramingLanguageEnum(String name) {
        /*redundant: Java is set as a default in javaFX MainMenuController.java*/
        if (name == null) {
            return ProgrammingLanguage.JAVA;
        }
        return Stream.of(ProgrammingLanguage.values())
                .filter(language -> name.equals(language.displayName) || name.equals(language.apiValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown programming language: " + name));
    }

    /**
     * Returns an alphabetically sorted list of display names for all supported programming languages.
     * For example: ["C", "C++", "C++ 14", "Java", "Python 3", ...]
     *
     * @return List of display names for all programming languages
     */
    public static List<String> getAllDisplayNames() {
        return Stream.of(ProgrammingLanguage.values())
                .map(ProgrammingLanguage::getDisplayName)
                .collect(Collectors.toList());

    }
}


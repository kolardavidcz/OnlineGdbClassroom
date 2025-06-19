package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.support;

import java.nio.file.Path;

/**
 * Utility class providing global path constants used throughout the test suite.
 * Contains paths relative to the project's test directory structure.
 */
public class GlobalPath {
    /**
     * Inner class holding static path variables used for test file operations.
     * All paths are resolved relative to the current project directory.
     */
    public static class GlobalPathVar {
        /**
         * Base path to the test package directory structure.
         */
        private static final Path currentPath = Path.of(System.getProperty("user.dir")).resolve("src").resolve("test").resolve("java").resolve("cz").resolve("vscht").resolve("bioinformatics").resolve("kolar").resolve("onlinegdbclassroom");

        /**
         * Path to the directory containing test files for the test builder.
         */
        public static final Path testFilesPath = currentPath.resolve("testbuilder").resolve("testFiles");

        /**
         * Path to the directory containing test files for the final builder.
         */
        public static final Path finalBuilder = currentPath.resolve("finalBuilder").resolve("testFiles");;
    }
}
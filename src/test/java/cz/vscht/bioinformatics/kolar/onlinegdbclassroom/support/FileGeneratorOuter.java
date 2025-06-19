package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.support;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.io.File.separator;

/**
 * Utility class for generating test files during test execution.
 * Provides functionality to create and manage temporary test files in specified directories.
 */

public class FileGeneratorOuter {

    /**
     * Inner class that handles the actual file generation operations.
     * Maintains a counter to ensure unique file names for each generated test file.
     */
    public static class FileGenerator {

        private static int fileCounter = 0;

        /**
         * Creates a new test file with the specified content in the given directory.
         *
         * @param input      The content to write into the test file
         * @param folderPath The directory path where the file should be created
         * @return The created File object
         * @throws RuntimeException If file creation or writing operations fail
         */
        public static File makeTestFile(String input, Path folderPath) throws RuntimeException {

            //chacking if the folder we want to create files in exist, and creats it if needed
            folderIntegrityChack(folderPath);

            //create file
            File testFile = new File(folderPath.toString() + separator + "test" + (fileCounter++) + ".txt");
            try {
                if (!testFile.createNewFile()) {
                    System.out.println("TESTS ERROR File not created / already exist\n" + testFile.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("TEST ERROR Create file: " + e);
            }

            //writing in the file
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testFile))) {
                bufferedWriter.write(input);
            } catch (IOException e) {
                throw new RuntimeException("TEST ERROR Write file: : " + e);
            }
            testFile.deleteOnExit();
            return testFile;
        }
    }

    /**
     * Verifies the existence of the specified directory and creates it if it doesn't exist.
     *
     * @param path The directory path to check and potentially create
     * @throws RuntimeException If directory creation fails
     */
    private static void folderIntegrityChack(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("TEST FILE ERRORS: " + e);
            }
        }
    }
}
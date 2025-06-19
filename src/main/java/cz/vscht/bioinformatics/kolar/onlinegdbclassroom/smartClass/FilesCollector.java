package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that handles reading and processing of text files.
 * Extends Source class and provides functionality to read multiple files,
 * split their content based on a separator, and create a list of text entries.
 */
public class FilesCollector extends TestCollector {
    private List<File> files;

    private boolean doCleanFiles;
    private Optional<String> separator;

    /**
     * Creates a new SmartFiles instance for processing multiple files.
     *
     * @param files     List of files to process
     * @param clean     Flag indicating whether to remove empty entries
     * @param separator Optional separator for splitting file content
     * @throws FileNotFoundException if the file list is empty or any file doesn't exist
     * @throws SecurityException     if read permission is denied for any file
     */
    public FilesCollector(List<File> files, boolean clean, Optional<String> separator) throws FileNotFoundException {

        if(files == null || files.isEmpty()) {
            throw new FileNotFoundException("List of files is empty");
        }
        for(File file : files) {
            if(!file.exists()) {
                throw new FileNotFoundException("File " + file + " does not exist");
            } else if(!file.canRead()) {
                throw new SecurityException("Does not have permission for " + file + "file");
            }
        }
        this.files = files;
        doCleanFiles = clean;
        this.separator = separator;
    }

    /**
     * Creates a new SmartFiles instance for processing a single file.
     *
     * @param files     Single file to process
     * @param clean     Flag indicating whether to remove empty entries
     * @param separator Optional separator for splitting file content
     * @throws FileNotFoundException if the file doesn't exist
     * @throws SecurityException     if read permission is denied
     */
    public FilesCollector(File files, boolean clean, Optional<String> separator) throws FileNotFoundException {
        this(Arrays.asList(files), clean, separator);
    }


    /**
     * Creates a list of strings from the file contents.
     * If a separator is provided, splits the content using it.
     * Can optionally filter out empty entries based on the clean flag.
     *
     * @return ArrayList containing the processed text entries
     */
    @Override
    public ArrayList<String> makeTestList() {

        return files.stream()
                /*make text from files*/
                .map(file -> {
                    try {
                        return Files.readString(file.toPath());
                    } catch (IOException e) {
                        //REDUNDANT: we checked the files in constructor (but required)
                        throw new RuntimeException("Error reading file: " + file.getPath(), e);
                    }
                })
                /*split each string to sub-string if needed*/
                .flatMap(text -> {
                    if (separator.isEmpty()) {
                        return Stream.of(text);
                    } else {
                        return Arrays.stream(text.split(separator.get(), -1));
                    }
                })
                .filter(text -> !doCleanFiles || !text.isEmpty())
                //Escpaing is done in TestBuilder class
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public List<File> getFiles() {
        return files;
    }
    public boolean doCleanFiles() {
        return doCleanFiles;
    }
}

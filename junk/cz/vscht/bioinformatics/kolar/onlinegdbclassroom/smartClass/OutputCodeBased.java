package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass; /**
 * Currently on hold
 */

/*package cz.vscht.bioinformatics.kolar.testbuilder.textGenerators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class OutputCodeBased {

    public static ArrayList<String> getOutput(Path outputfile, String separator) {

        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(outputfile.toFile()))) {

            Files.lines(outputfile).forEach(result::add);
            return result;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error: " + e + "\nFile not found at: " + outputfile.toString());
        } catch (SecurityException e) {
            throw new RuntimeException("Error: " + e + "\nNot enough privilege to access: " + outputfile.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e);
        }
    }
}
*/
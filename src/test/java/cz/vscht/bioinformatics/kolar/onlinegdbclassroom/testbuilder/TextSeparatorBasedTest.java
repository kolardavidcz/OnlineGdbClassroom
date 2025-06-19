package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.FilesCollector;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.support.FileGeneratorOuter;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.support.GlobalPath;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TextSeparatorBasedTest {

    @Test
    void singleLine() throws FileNotFoundException {
        File testFile1 = makeTestFile("Toto je řádek textu.");
        testFile1.deleteOnExit();
        List<String> testList1 = List.of("Toto", "je", "řádek", "textu.");
        assertIterableEquals(testList1, new FilesCollector(testFile1, false, Optional.of(" ")).makeTestList());

        File testFile2 = makeTestFile("Toto je řádek textu.");
        testFile2.deleteOnExit();
        List<String> testList2 = List.of("Toto je řádek textu.");
        assertIterableEquals(testList2, new FilesCollector(testFile2, false, Optional.empty()).makeTestList());
    }

    @Test
    void multipleLines() throws FileNotFoundException {
        File testFile1 = makeTestFile("aaa\n" + "bbb\n" + "ccc");
        testFile1.deleteOnExit();
        List<String> testList1 = List.of("aaa", "bbb", "ccc");
        assertIterableEquals(testList1, new FilesCollector(testFile1, false, Optional.of("\n")).makeTestList());

        File testFile2 = makeTestFile("ml' nob:\n" +
                                       "Qapla'\n" +
                                       "bortaS bIr jablu'DI' reH QaQqu' nay'.");
        testFile2.deleteOnExit();
        List<String> testList2 = List.of("ml' nob:", "Qapla'", "bortaS bIr jablu'DI' reH QaQqu' nay'.");
        assertIterableEquals(testList2, new FilesCollector(testFile2, false, Optional.of("\n")).makeTestList());

        File testFile3 = makeTestFile("aaa \n" + " bbb");
        testFile3.deleteOnExit();
        List<String> testList3 = List.of("aaa", "bbb");
        List<String> testOut3 = new FilesCollector(testFile3, false, Optional.of("\n")).makeTestList();

        for( int i = 0; i < testList3.size(); i++ ) {
            assertNotEquals(testList3.get(i), testOut3.get(i));
        }

        File testFile4 = makeTestFile("\n\n\n\n\n ");
        testFile4.deleteOnExit();
        List<String> testList4 = List.of("", "", "", "", "", " ");
        assertIterableEquals(testList4, new FilesCollector(testFile4, false, Optional.of("\n")).makeTestList());

        File testFile5 = makeTestFile("\n\n\n\n\n ");
        testFile5.deleteOnExit();
        List<String> testList5 = List.of(" ");
        assertIterableEquals(testList5, new FilesCollector(testFile5, true, Optional.of("\n")).makeTestList());
    }

    @Test
    void multipleFiles() throws FileNotFoundException {
        List<String> testStrings1 = List.of("[-498.309,261.471]", "[-498.309,261.471]", "[135.555,136.477]", "[20637.394,23090.989]", "[18950.164,21203.689]", "[-3.053,3.019]", "[18438.880,22258.580]", "[2.774,-4.271]", "[26251.020,31692.270]", "[-364.945e-59,276.003e-59]", "[277.888e-59,359.884e-59]", "[-1.588e-59,-3.473e-59]");
        ArrayList<File> testFiles1 = new ArrayList<>();

        for (String testString : testStrings1) {
            File file = makeTestFile(testString);
            file.deleteOnExit();
            testFiles1.add(file);
        }
        assertIterableEquals(testStrings1, new FilesCollector(testFiles1, false, Optional.empty()).makeTestList());

        /*testing file deletion (file number 2)*/
        ArrayList<File> testFiles2 = new ArrayList<>(testFiles1);
        testFiles2.get(2).delete();
        //testFiles2.remove(2); NOT removing here
        assertThrows(FileNotFoundException.class, () -> new FilesCollector(testFiles2, false, Optional.empty()).makeTestList());
    }


    private File makeTestFile(String input) {
        return FileGeneratorOuter.FileGenerator.makeTestFile(input, GlobalPath.GlobalPathVar.testFilesPath);
    }
}
package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.finalBuilder;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.BuilderConnection;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.support.FileGeneratorOuter;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.support.GlobalPath;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.MyJsonTestBuilder;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums.ProgrammingLanguage;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.enums.EnumTabOpened;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class builderTest {

    //ProgrammingLanguage.JAVA.getDisplayName() == "Java"

    @Test
    void regExpCombo() throws FileNotFoundException {
        BuilderConnection test1 = new BuilderConnection("test1",  "Java", "", EnumTabOpened.REG_EXP.getDisplayName(), EnumTabOpened.REG_EXP.getDisplayName(), List.of(), List.of(), false, false, false, "", false, "",
                "aa", "bb", "10", "10", false ,"", "");
        assertDoesNotThrow(() -> new MyJsonTestBuilder(test1.getInputs(), test1.getOutputs()));

        //invalid size of RegExAmounts (should never happen)
        BuilderConnection test2 =  new BuilderConnection("test2", ProgrammingLanguage.JAVA.getDisplayName(), "", EnumTabOpened.REG_EXP.getDisplayName(), EnumTabOpened.REG_EXP.getDisplayName(), List.of(), List.of(), false, false, false, "", false, "",
                "aa", "bb", "10", "5", false,"", "");
        assertThrows(IllegalArgumentException.class, () -> new MyJsonTestBuilder(test2.getInputs(), test2.getOutputs()));

        //outputIdenticalToInputRegExp = true
        BuilderConnection test3 = new BuilderConnection("test3",  ProgrammingLanguage.JAVA.getDisplayName(), "", EnumTabOpened.REG_EXP.getDisplayName(), EnumTabOpened.REG_EXP.getDisplayName(), List.of(), List.of(), false, false, false, "", false, "",
                "aa", "", "10", "", true ,"", "");
        List<String> out3 = List.of("aa", "aa", "aa", "aa", "aa", "aa", "aa", "aa", "aa", "aa");
        assertIterableEquals(test3.getInputs(), test3.getOutputs());
        assertIterableEquals(out3, test3.getOutputs());
    }

    @Test
    void regExpFile() throws FileNotFoundException {
        List<File> testFiles1 = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            testFiles1.add(makeTestFile("regExpFile" + i));
        }

        BuilderConnection test1 = new BuilderConnection("test1",  ProgrammingLanguage.JAVA.getDisplayName(), "", EnumTabOpened.FILES.getDisplayName(), EnumTabOpened.REG_EXP.getDisplayName(), testFiles1, List.of(), false, false, false, "", false,"",
                "", "bb", "", "10", false, "", "");
        assertDoesNotThrow(() -> new MyJsonTestBuilder(test1.getInputs(), test1.getOutputs()));
        List<String> in1 = List.of("regExpFile0", "regExpFile1", "regExpFile2", "regExpFile3", "regExpFile4", "regExpFile5", "regExpFile6", "regExpFile7", "regExpFile8", "regExpFile9");
        List<String> out1 = List.of("bb", "bb", "bb", "bb", "bb", "bb", "bb", "bb", "bb", "bb");
        assertIterableEquals(in1, test1.getInputs());
        assertIterableEquals(out1, test1.getOutputs());

        BuilderConnection test2 = new BuilderConnection("test2",  ProgrammingLanguage.JAVA.getDisplayName(), "", EnumTabOpened.FILES.getDisplayName(), EnumTabOpened.REG_EXP.getDisplayName(), testFiles1, List.of(), false, false, true,"\n", false, "",
                "", "bb", "", "10", false,"", "");
        assertDoesNotThrow(() -> new MyJsonTestBuilder(test2.getInputs(), test2.getOutputs()));
        assertIterableEquals(in1, test2.getInputs());
    }

    @Test
    void FileCombo() throws FileNotFoundException {
        List<File> testFiles1 = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            testFiles1.add(makeTestFile("fileCombo" + i + "\nfileCombo" + i ));
        }
        BuilderConnection test2 = new BuilderConnection("test2", ProgrammingLanguage.JAVA.getDisplayName(), "", EnumTabOpened.FILES.getDisplayName(), EnumTabOpened.FILES.getDisplayName(), testFiles1, testFiles1, false, false, true,"\n", true, "\n",
                "", "", "", "", false,"", "");
        assertDoesNotThrow(() -> new MyJsonTestBuilder(test2.getInputs(), test2.getOutputs()));
        List<String> in1 = List.of("fileCombo0", "fileCombo0", "fileCombo1", "fileCombo1", "fileCombo2", "fileCombo2", "fileCombo3", "fileCombo3", "fileCombo4", "fileCombo4", "fileCombo5", "fileCombo5", "fileCombo6", "fileCombo6", "fileCombo7", "fileCombo7", "fileCombo8", "fileCombo8", "fileCombo9", "fileCombo9");
        assertIterableEquals(in1, test2.getInputs());
        assertIterableEquals(in1, test2.getOutputs());
        assertIterableEquals(test2.getInputs(), test2.getOutputs());
    }

    private File makeTestFile(String input) {
        return FileGeneratorOuter.FileGenerator.makeTestFile(input, GlobalPath.GlobalPathVar.finalBuilder);
    }
}
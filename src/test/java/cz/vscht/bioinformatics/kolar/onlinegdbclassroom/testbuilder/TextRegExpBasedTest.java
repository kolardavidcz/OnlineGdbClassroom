package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.RegExpCollector;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.regExpSpecialGroups.SpecialGroups;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.support.ErrorHendelerOuter.ErrorHendeler.smartSpace;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TextRegExpBasedTest {

    @Test
    void simple() {
        List<String> test1 = List.of("a");
        assertIterableEquals(test1, new RegExpCollector("a", 1).makeTestList());

        List<String> test2 = List.of("a", "a", "a");
        assertIterableEquals(test2, new RegExpCollector("a", 3).makeTestList());

        List<String> test3 = List.of("今具", "雇");
        List<String> out3 = new RegExpCollector("\\u4eca\\u5177", 1).makeTestList();
        RegExpCollector regExp3b = new RegExpCollector("\\u96c7", 1);
        out3.addAll(regExp3b.makeTestList());

        assertIterableEquals(test3, out3);
    }

    @Test
    void moreComplex() {

        List<String> out4 = new RegExpCollector("^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$", 10).makeTestList();
        for (String outString : out4) {
            assertTrue(Pattern.matches("^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$", outString));
        }

        RegExpCollector regExp5 = new RegExpCollector("[a-zA-Z]{5,10}", 10);
        List<String> out5 = regExp5.makeTestList();
        for (String outString : out5) {
            assertTrue(Pattern.matches("[a-zA-Z]{5,10}", outString));
        }
    }

    @Test
    void specailGroups() {
        assertDoesNotThrow(() -> new RegExpCollector("\\{\\{aa\\}\\}", 9).makeTestList());
        List<String> out1 = List.of("{{aa}}", "{{aa}}", "{{aa}}", "{{aa}}", "{{aa}}", "{{aa}}", "{{aa}}", "{{aa}}", "{{aa}}");
        assertIterableEquals(out1, new RegExpCollector("\\{\\{aa\\}\\}", 9).makeTestList());

        assertDoesNotThrow(() -> new RegExpCollector("{{COUNTRY}}", 9).makeTestList());
        System.out.println(smartSpace +"{{COUNTRY}}\n" + new RegExpCollector("{{COUNTRY}}", 9).makeTestList());
        assertDoesNotThrow(() -> new RegExpCollector("{{COUNTRY}}?", 9).makeTestList());
        System.out.println(smartSpace +"{{COUNTRY}}?\n" + new RegExpCollector("{{COUNTRY}}?", 9).makeTestList());

        assertDoesNotThrow(() -> new RegExpCollector("{{" + SpecialGroups.FEMALE_FIRSTNAME.getGroup() + "}} {{"+ SpecialGroups.FEMALE_LASTNAME.getGroup() + "}} ze státu {{" + SpecialGroups.COUNTRY.getGroup() + "}}", 5).makeTestList());
        System.out.println(smartSpace + "{{FEMALE_FIRSTNAME}} {{FEMALE_LASTNAME}} ze státu {{COUNTRY}}" + "\n" +
                            new RegExpCollector("{{" + SpecialGroups.FEMALE_FIRSTNAME.getGroup() + "}} {{"+ SpecialGroups.FEMALE_LASTNAME.getGroup() + "}} ze státu {{" + SpecialGroups.COUNTRY.getGroup() + "}}", 5).makeTestList());

        System.out.println(smartSpace);
    }
}
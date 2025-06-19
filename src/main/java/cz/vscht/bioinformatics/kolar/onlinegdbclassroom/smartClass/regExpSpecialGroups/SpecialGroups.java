package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.smartClass.regExpSpecialGroups;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enumeration representing special groups used for pattern substitution in regular expressions.
 * Each group corresponds to a specific category (e.g., first names, last names, countries)
 * that can be used as placeholders in regular expression patterns.
 * These placeholders are replaced with actual values from corresponding text files during processing.
 */
public enum SpecialGroups {
    FEMALE_FIRSTNAME(true,  "FEMALE_FIRSTNAME"),
    FEMALE_LASTNAME(true, "FEMALE_LASTNAME"),
    MALE_FIRSTNAME(true, "MALE_FIRSTNAME"),
    MALE_LASTNAME(true, "MALE_LASTNAME"),
    COUNTRY(true, "COUNTRY");


    private final boolean hasFile;
    /** The string identifier for this special group */
    private final String group;

    /** @return The group identifier string */
    public String getGroup() {
        return group;
    }

    /**
     * Checks if this group has an associated data file.
     *
     * @return true if the group has an associated file, false otherwise
     */
    public boolean hasFile() {
        return hasFile;
    }

    SpecialGroups(boolean hasFile, String group) {
        this.hasFile = hasFile;
        this.group = group;
    }

    /**
     * Returns a list of all special group identifiers.
     *
     * @return List of special group identifier strings
     */
    public static List<String> getSpecialGroups() {
        return Stream.of(SpecialGroups.values())
                .map(SpecialGroups::getGroup)
                .toList();
    }

    /**
     * Generates a regular expression pattern that matches any special group identifier.
     * The pattern is formed as "(group1|group2|...)" where groups are all possible values.
     *
     * @return A regular expression string matching any special group
     */
    public static String getSpecialGroupsRegExp() {
        return Stream.of(SpecialGroups.values())
                .map(SpecialGroups::getGroup)
                .collect(Collectors.joining("|", "(", ")"));
    }

    /**
     * Retrieves the SpecialGroups enum constant corresponding to the given group name.
     *
     * @param groupName The name of the special group to look up
     * @return The matching SpecialGroups enum constant
     * @throws NullPointerException     if groupName is null
     * @throws IllegalArgumentException if no matching group is found
     */
    public static SpecialGroups getGroupForName(String groupName) {
        if (groupName == null) {
            throw new NullPointerException("ProgrammingLanguage string to getGroupForName is null");
        }
        for(SpecialGroups group : SpecialGroups.values()) {
            if (group.getGroup().equals(groupName)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Unknown special group name: " + groupName);
    }
}

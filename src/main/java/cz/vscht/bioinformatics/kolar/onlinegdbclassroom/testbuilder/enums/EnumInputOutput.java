package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder.enums;

public enum EnumInputOutput {
    INPUT_TESTS("lastInputDirectory"),
    OUTPUT_TESTS("lastOutputDirectory"),
    OUTPUT_JSON("lastSaveDirectory");

    private final String directoryKey;

    EnumInputOutput(String directoryKey) {
        this.directoryKey = directoryKey;
    }
    public String getDirectoryKey() {
        return directoryKey;
    }

}

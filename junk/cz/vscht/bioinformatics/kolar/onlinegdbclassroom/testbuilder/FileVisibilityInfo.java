package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.testbuilder;

import java.io.File;

public class FileVisibilityInfo {
    private final File file;
    private boolean visibleToStudent;

    public FileVisibilityInfo(File file, boolean visibleToStudent) {
        this.file = file;
        this.visibleToStudent = visibleToStudent;
    }

    public File getFile() { return file;}
    public boolean isVisible() { return visibleToStudent;}
    public void setVisible(boolean visible) { this.visibleToStudent = visible; }

    @Override
    public String toString() {
        return file.getName() + (visibleToStudent ? "" : " (Hidden)");
    }
}

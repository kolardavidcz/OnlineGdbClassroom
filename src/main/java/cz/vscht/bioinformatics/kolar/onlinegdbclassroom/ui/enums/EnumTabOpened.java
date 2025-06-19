package cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.enums;

import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.BuilderConnection;
import cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.MainMenuController;

/**
 * Enum representing the different tabs that can be opened in the application's user interface.
 * Each enum is used in {@link MainMenuController} to set Tab name and also in {@link BuilderConnection} to MATCH STRING value
 */
public enum EnumTabOpened {
    REG_EXP("Reg Exp"),
    FILES ("Files");

    private final String displayName;

    EnumTabOpened(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

module cz.vscht.bioinformatics.kolar.onlinegdbclassroom {
    requires javafx.fxml;
    requires java.prefs;
    requires org.jsoup;
    requires javafx.web;
    requires rgxgen;
    requires com.fasterxml.jackson.databind;

    exports cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui;
    opens cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui to javafx.fxml;
    exports cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.enums;
    opens cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.enums to javafx.fxml;
    exports cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.support;
    opens cz.vscht.bioinformatics.kolar.onlinegdbclassroom.ui.support to javafx.fxml;
}
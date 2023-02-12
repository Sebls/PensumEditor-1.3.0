module com.pensumeditor.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.desktop;
    requires javafx.swing;
    requires org.apache.pdfbox;

    requires org.controlsfx.controls;
    requires org.jsoup;

    opens com.pensumeditor.main to javafx.fxml;
    opens com.pensumeditor.main.editor to javafx.fxml;
    opens com.pensumeditor.data to javafx.fxml;

    exports com.pensumeditor.main;
    exports com.pensumeditor.main.editor;
    exports com.pensumeditor.data;
    opens com.pensumeditor.main.editor.subjects.iqstyle to javafx.fxml;
    opens com.pensumeditor.main.editor.subjects.classic to javafx.fxml;
}
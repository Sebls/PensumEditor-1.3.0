package com.pensumeditor.main;

import com.pensumeditor.data.Pensum;

import com.pensumeditor.main.editor.EditorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ProjectItemController {

    @FXML
    Label nameLabel;
    @FXML
    Label previewLabel;
    @FXML
    Rectangle rectangle;
    @FXML
    AnchorPane area;

    private String path;
    private Pensum pensum;

    public void setProjectName(String name) {
        nameLabel.setText(name);
    }
    public void setPreviewLabel(String preview) {
        previewLabel.setText(preview);
    }
    public void loadProjectPensum(Pensum pensum, String path) {
        this.pensum = pensum;
        this.path = path;
        setProjectName(pensum.getName());
    }

    @FXML
    public void onMouseClicked() throws IOException {
        if (Objects.nonNull(pensum)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("editor/Editor.fxml"));
            Scene scene = (Scene) area.getScene();
            Parent editor = (Parent) fxmlLoader.load();
            EditorController ec = fxmlLoader.getController();
            ec.loadPath(path);
            ec.start(pensum);
            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Pensum Editor - " + pensum.getName());
            scene.setRoot(editor);
        }
    }

    @FXML
    protected void onMouseEntered() {
        area.setStyle("-fx-background-color: cdcdcd;");
    }
    @FXML
    protected void onMouseExit() {
        area.setStyle("-fx-background-color: f4f4f4;");
    }
}

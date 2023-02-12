package com.pensumeditor.main;

import com.pensumeditor.data.Pensum;

import com.pensumeditor.main.editor.EditorController;
import com.pensumeditor.serializacion.Serialization;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    HBox ProjectsPanel;
    @FXML
    Group addProject;
    @FXML
    Group openProject;

    @FXML
    protected void onMouseEntered(MouseEvent e) {
        Group buttom = (Group) e.getSource();
        buttom.getChildren().get(0).getTransforms().add(new Scale(1.05, 1.05));
    }
    @FXML
    protected void onMouseExit(MouseEvent e) {
        Group buttom = (Group) e.getSource();
        buttom.getChildren().get(0).getTransforms().clear();
    }

    @FXML
    public void openProject() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir proyecto");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = (Stage) ProjectsPanel.getScene().getWindow();
        File project = fileChooser.showOpenDialog(stage);
        String projectPath = project.getAbsolutePath();
        Pensum pensum = Serialization.ObjectDeserialization(projectPath, Pensum.class);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editor/Editor.fxml"));
        Scene scene = (Scene) ProjectsPanel.getScene();
        Parent editor = (Parent) fxmlLoader.load();
        EditorController ec = fxmlLoader.getController();
        ec.loadPath(projectPath);
        ec.start(pensum);
        scene.setRoot(editor);
    }
    @FXML
    public void newProject() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ProjectCreator.fxml"));
        Scene scene = (Scene) ProjectsPanel.getScene();
        Parent newProject = (Parent) fxmlLoader.load();
        scene.setRoot(newProject);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*CodeSource codeSource = App.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String path = (jarFile.getParentFile().getPath() + "\\projects\\").replace("\\", "/");*/
        String path = "src/main/resources/com/pensumeditor/projects";
        File dir = new File(path);
        File[] directoryFiles = dir.listFiles();
        if (Objects.nonNull(directoryFiles)) {
            for (int i = 0; i < directoryFiles.length; i++) {
                if (directoryFiles[i].isFile() && directoryFiles[i].getName().contains(".pns")) {
                    addProjectPanel(Serialization.ObjectDeserialization(directoryFiles[i].getPath(), Pensum.class), directoryFiles[i].getPath());
                }
            }
        }
    }

    private void addProjectPanel(String name, String preview) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ProjectItem.fxml"));
            AnchorPane project = fxmlLoader.load();
            ProjectItemController pc = fxmlLoader.getController();
            pc.setProjectName(name);
            pc.setPreviewLabel(preview);
            ProjectsPanel.getChildren().add(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addProjectPanel(Pensum pensum, String path) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ProjectItem.fxml"));
            AnchorPane project = fxmlLoader.load();
            ProjectItemController pc = fxmlLoader.getController();
            pc.loadProjectPensum(pensum, path);
            ProjectsPanel.getChildren().add(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
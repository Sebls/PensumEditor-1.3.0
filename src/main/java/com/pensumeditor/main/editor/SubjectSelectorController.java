package com.pensumeditor.main.editor;

import com.pensumeditor.data.Subject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

public class SubjectSelectorController {

    @FXML
    private TableColumn Codigo;
    @FXML
    private TableColumn Nombre;
    @FXML
    private TableColumn Creditos;
    @FXML
    private TableColumn Grupo;
    @FXML
    private TableColumn Prerequisitos;
    @FXML
    private TableColumn Componente;
    @FXML
    private TableView SubjectTable;
    @FXML
    private Hyperlink addSubjectLink;
    
    public Object selectedSubject;
    public int[] position;

    // State == 0 -> Used for show and select subjects
    // State == 1 -> Used for show subjects
    private int state = 0;

    private ArrayList<Subject> subjects;
    private ArrayList<String> groups = new ArrayList<>();
    public ArrayList<Subject[]> SubjectsMatrix;
    
    public void loadSubjectsMatrix(ArrayList<Subject[]> subjectsMatrix) {
        this.SubjectsMatrix = subjectsMatrix;
    }
    
    @FXML
    public void SelectedSubject(MouseEvent event) {
        if(this.state == 0 && event.getClickCount() == 2) {
            this.selectedSubject = SubjectTable.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(this.selectedSubject)) {
                Stage stage = (Stage) SubjectTable.getScene().getWindow();
                stage.close();
            }
        } 
    }

    @FXML
    public void addSubject() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SubjectCreator.fxml"));
        Parent subjectCreator = fxmlLoader.load();
        SubjectCreatorController scc = fxmlLoader.getController();
        scc.start(subjects, groups);
        scene = new Scene(subjectCreator, 437, 642);
        Stage stage = new Stage();
        stage.setTitle("Subject Creator");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("pensum_editor_icon.png")));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        subjects.add(scc.getSubject());
    }
    
    public int[] getPosition() {
        return this.position;
    }
    
    private Scene scene;
    
    public void openPositionSelector() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("PositionSelector.fxml"));
            AnchorPane positionSelector = fxmlLoader.load();
            PositionSelectorController psc = fxmlLoader.getController();
            psc.loadSubjectsMatrix(SubjectsMatrix);
            psc.loadFreeGridPane();
            scene = new Scene(positionSelector, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Position Selector");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("pensum_editor_icon.png")));
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
            this.position = psc.getPosition();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Subject> getSubjects() {
        return this.subjects;
    }
    public Object getSelectedSubject() {
        return this.selectedSubject;
    }
    
    public void loadSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
        SubjectTable.getItems().clear();

        Codigo.setCellValueFactory(new PropertyValueFactory<>("code"));
        Nombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        Creditos.setCellValueFactory(new PropertyValueFactory<>("credits"));
        Grupo.setCellValueFactory(new PropertyValueFactory<>("group"));
        Prerequisitos.setCellValueFactory(new PropertyValueFactory<>("prerequisite"));
        Componente.setCellValueFactory(new PropertyValueFactory<>("component"));

        HashSet<String> groupSet = new HashSet<>();
        Comparator<String> spanishAlphabeticSorter = new Comparator<String>() {
            Collator collator = Collator.getInstance();

            public int compare(String o1, String o2) {
                return collator.compare(o1, o2);
            }
        };
        Comparator<Subject> compareSubject = Comparator.comparing(Subject::getComponent).thenComparing(Subject::getGroup, spanishAlphabeticSorter).thenComparing(Subject::getName, spanishAlphabeticSorter);
        List<Subject> subjectsSorted = subjects.stream().sorted(compareSubject).collect(Collectors.toList());
        for (Subject subject : subjectsSorted) {
            SubjectTable.getItems().add(subject);
            groupSet.add(subject.getGroup());
        }

        groups.addAll(groupSet);
    }

    public void hideAddSubjectOption() {
        addSubjectLink.setVisible(false);
    }

    public void setState(int state) {
        this.state = state;
    }
}

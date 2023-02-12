package com.pensumeditor.main.editor;

import com.pensumeditor.data.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.Collator;
import java.util.*;

public class SubjectCreatorController {

    @FXML
    private ImageView groupOption;
    @FXML
    private ImageView prerequisiteOption;
    @FXML
    private ImageView addPrerequisite;
    @FXML
    private TextField code;
    @FXML
    private ComboBox<String> component;
    @FXML
    private Spinner<Integer> credits;
    @FXML
    private ComboBox<String> groupComboBox;
    @FXML
    private TextField groupField;
    @FXML
    private TextField name;
    @FXML
    private ComboBox<String> prerequisiteComboBox;
    @FXML
    private TextField prerequisiteField;
    @FXML
    private ListView<String> prerequisites;

    // groupState == True -> Combobox used for select group will be active
    // groupState == False -> Combobox used for select group will be deactivated and the user will use TextField
    private boolean groupState = true;
    @FXML
    private void alternateGroup() {
        if (groupState) {
            groupState = false;
            groupComboBox.setVisible(false);
            groupField.setVisible(true);
            groupOption.setImage(new Image(getClass().getResourceAsStream("select_option.png")));
        } else {
            groupState = true;
            groupComboBox.setVisible(true);
            groupField.setVisible(false);
            groupOption.setImage(new Image(getClass().getResourceAsStream("text_option.png")));
        }

    }

    // prerequisiteState == True -> Combobox used for select prerequisite will be active
    // prerequisiteState == False -> Combobox used for select prerequisite will be deactivated and the user will use TextField
    private boolean prerequisiteState = true;
    @FXML
    private void alternatePrerequisite() {
        if (prerequisiteState) {
            prerequisiteState = false;
            prerequisiteComboBox.setVisible(false);
            prerequisiteField.setVisible(true);
            addPrerequisite.setVisible(true);
            prerequisiteOption.setImage(new Image(getClass().getResourceAsStream("select_option.png")));
        } else {
            prerequisiteState = true;
            prerequisiteComboBox.setVisible(true);
            prerequisiteField.setVisible(false);
            addPrerequisite.setVisible(false);
            prerequisiteOption.setImage(new Image(getClass().getResourceAsStream("text_option.png")));
        }

    }

    @FXML
    public void onPrerequisiteSelected() {
        String value;
        if (prerequisiteState) {
            value = prerequisiteComboBox.getValue();
        } else {
            value = prerequisiteField.getText();
            prerequisiteField.setText("");
        }
        if (Objects.nonNull(value) && !prerequisites.getItems().contains(value)) {
            prerequisites.getItems().add(value);
        }
    }

    @FXML
    public void eliminatePrerequisite(javafx.scene.input.MouseEvent mouseEvent) {
        String selection = prerequisites.getSelectionModel().getSelectedItem();
        if (mouseEvent.getClickCount() == 2 && Objects.nonNull(selection)) {
            prerequisites.getItems().remove(selection);
        }
    }

    private Subject Subject;

    public void start(ArrayList<Subject> subjects, ArrayList<String> groupList) {

        credits.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 2));

        ArrayList<String> componentList = new ArrayList<>();
        componentList.add("Componente de Fundamentación");
        componentList.add("Componente de Formación Disciplinar o Profesional");
        componentList.add("Trabajo de Grado");
        componentList.add("Componente de Libre Elección");

        ArrayList<String> componentSortList = organizedAlphabeticList(componentList);

        ObservableList<String> components = FXCollections.observableList(componentSortList);
        component.setItems(components);

        ArrayList<String> groupSortList = organizedAlphabeticList(groupList);

        ObservableList<String> groups = FXCollections.observableList(groupSortList);
        groupComboBox.setItems(groups);

        ArrayList<String> prerequisiteList = new ArrayList<>();

        for (Subject subject : subjects) {
            prerequisiteList.add(subject.getName());
        }

        ArrayList<String> prerequisiteSortList = organizedAlphabeticList(prerequisiteList);

        ObservableList<String> prerequisites = FXCollections.observableList(prerequisiteSortList);
        prerequisiteComboBox.setItems(prerequisites);
    }

    @FXML
    private void createSubject() {
        int Code = Integer.valueOf(code.getText());
        String Name = name.getText();
        int Credits = credits.getValue();
        String Prerequisites = "";
        for (String prerequisite : prerequisites.getItems()) {
            if (!Prerequisites.equals("")) {
                Prerequisites += ", " + prerequisite;
                continue;
            }
            Prerequisites = prerequisite;
        }
        String Group;
        if (groupState) {
            Group = groupComboBox.getValue();
        } else {
            Group = groupField.getText();
        }
        String Component = component.getValue();

        Subject = new Subject(Code, Name, Credits, Group, Prerequisites, Component);

        Stage stage = (Stage) code.getScene().getWindow();
        stage.close();
    }

    public Subject getSubject() {
        return this.Subject;
    }

    // Spanish Alphabetic Sort - From: "https://es.stackoverflow.com/questions/126937/como-ordenar-los-nombres-de-contactos-alfab%C3%A9ticamente-incluyendo-los-que-contien"
    public static ArrayList<String> organizedAlphabeticList(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            Collator collator = Collator.getInstance();

            public int compare(String o1, String o2) {
                return collator.compare(o1, o2);
            }
        });
        return list;
    }

}


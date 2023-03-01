package com.pensumeditor.main;

import com.pensumeditor.data.AcademicProgram;
import com.pensumeditor.data.Pensum;
import com.pensumeditor.data.Subject;
import com.pensumeditor.main.editor.EditorController;
import com.pensumeditor.main.editor.SubjectItemController;
import com.pensumeditor.main.editor.SubjectSelectorController;
import com.pensumeditor.webscraping.SubjectScraper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.CodeSource;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.nio.file.Paths;

import static javafx.scene.input.KeyCode.BACK_SPACE;

public class ProjectCreatorController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField author;
    @FXML
    private TextField search;
    @FXML
    private TableView<AcademicProgram> APTable;
    @FXML
    private TableColumn Nombre;
    @FXML
    private TableColumn Sede;
    @FXML
    private TableColumn Tipo;
    @FXML
    private ListView<String> APView;
    @FXML
    private Button ShowAllSubjects;
    @FXML
    private Button ShowSubjects;
    @FXML
    private Button DeleteAP;
    @FXML
    private ComboBox<String> style;
    @FXML
    private ComboBox<String> colorOption;
    @FXML
    private ListView<HBox> colorList;
    @FXML
    private Rectangle grupoRectangle;
    @FXML
    private AnchorPane previewPane;

    private final SubjectScraper subjectScraper = new SubjectScraper();

    private ArrayList<AcademicProgram> AcademicPrograms;
    private HashMap<String, AcademicProgram> APMap = new HashMap<>();
    private Set<Subject> subjectSet = new LinkedHashSet<>();
    private HashMap<String, String> componentsMap = new HashMap();
    private HashMap<String, String> groupsMap = new HashMap<>();
    private Set<String> groupsSet = new LinkedHashSet<>();
    @FXML
    public void Main() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Main.fxml"));
        Scene scene = (Scene) name.getScene();
        Parent main = (Parent) fxmlLoader.load();
        scene.setRoot(main);
    }

    @FXML
    public void onSearch(KeyEvent e) {
        if (!search.getText().equals("")) {
            APTable.getItems().clear();
            for (AcademicProgram academicProgram : AcademicPrograms) {
                if (academicProgram.getName().toLowerCase().contains(search.getText().toLowerCase())) {
                    APTable.getItems().add(academicProgram);
                }
            }
        } else if (e.getCode().equals(BACK_SPACE) && search.getText().equals("")) {
            APTable.getItems().clear();
            for (AcademicProgram academicProgram : AcademicPrograms) {
                APTable.getItems().add(academicProgram);
            }
        }
    }

    @FXML
    public void SelectedAcademicProgram(MouseEvent event) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        if(event.getClickCount() == 2) {
            AcademicProgram academicProgram = APTable.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(academicProgram)) {
                ArrayList<Subject> subjects = null;
                ArrayList<String> groups = null;
                if (Objects.isNull(academicProgram.getSubjects())) {
                    String URL = academicProgram.getURL();
                    academicProgram.setSubjects(new ArrayList<>(subjectScraper.webScraping(URL)));
                    academicProgram.setGroups(new ArrayList<>(subjectScraper.getGroups()));
                } else {
                    subjects = academicProgram.getSubjects();
                    groups = academicProgram.getGroups();
                }
                if (Objects.nonNull(subjects) && Objects.nonNull(groups)) {

                    subjectSet.addAll(subjects);
                    groupsSet.addAll(groups);
                    for (String group : groups) {
                        if (!groupsMap.containsKey(group)) {
                            groupsMap.put(group, "#FFFFFF");
                        }
                    }

                    style.setDisable(false);
                    colorOption.setDisable(false);
                    colorList.setDisable(false);

                    String listViewItem = academicProgram.getName() + " - " + academicProgram.getType();
                    APMap.put(listViewItem, academicProgram);
                    ArrayList<String> APArrayList = new ArrayList<>();
                    APArrayList.addAll(APMap.keySet());
                    ObservableList<String> academicProgramsList = FXCollections.observableList(APArrayList);
                    APView.setItems(academicProgramsList);

                    ShowAllSubjects.setDisable(false);
                    ShowSubjects.setDisable(false);
                    DeleteAP.setDisable(false);
                }
            }
        }
    }
    @FXML
    private void openWebsite() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://programasacademicos.unal.edu.co/"));
    }

    public static Scene scene;
    @FXML
    private void showAllSubjects() throws IOException {
        String key = APView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editor/SubjectSelector.fxml"));
        VBox subjectSelector = fxmlLoader.load();
        SubjectSelectorController ssc = fxmlLoader.getController();
        ssc.hideAddSubjectOption();
        ssc.setState(1);
        ArrayList<Subject> subjects = new ArrayList<>();
        subjects.addAll(subjectSet);
        ssc.loadSubjects(subjects);
        scene = new Scene(subjectSelector, 1100, 600);
        Stage stage = new Stage();
        stage.setTitle("Asignaturas cargadas");
        stage.getIcons().add(new Image("https://img.icons8.com/offices/512/map-editing.png"));
        stage.setScene(scene);
        stage.showAndWait();
        ssc.setState(0);
    }
    @FXML
    private void showSubjects() throws IOException {
        String key = APView.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(key)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("editor/SubjectSelector.fxml"));
            VBox subjectSelector = fxmlLoader.load();
            SubjectSelectorController ssc = fxmlLoader.getController();
            ssc.hideAddSubjectOption();
            ssc.setState(1);
            ssc.loadSubjects(APMap.get(key).getSubjects());
            scene = new Scene(subjectSelector, 1100, 600);
            Stage stage = new Stage();
            stage.setTitle("Asignaturas cargadas - " + key);
            stage.getIcons().add(new Image("https://img.icons8.com/offices/512/map-editing.png"));
            stage.setScene(scene);
            stage.showAndWait();
            ssc.setState(0);
        }
    }
    @FXML
    private void deleteAPSelected() {
        String selection = APView.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selection)) {
            // Removing subjects using the complement of n sets
            subjectSet.removeAll(APMap.get(selection).getSubjects());
            for (String key : APMap.keySet()) {
                if (!key.equals(selection)) {
                    Set<Subject> intersection = new HashSet<>(subjectSet);
                    intersection.retainAll(APMap.get(key).getSubjects());
                    subjectSet.addAll(intersection);
                }
            }

            APView.getItems().remove(selection);
            APMap.remove(selection);
        }
    }

    private SubjectItemController controller;
    @FXML
    private void onStyleSelect() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String styleSelection = style.getSelectionModel().getSelectedItem();
        previewPane.getChildren().clear();
        loadStylePreviewFXML(styleSelection, "SubjectItem");
        loadStylePreviewFXML(styleSelection, "Arrow");
        loadStylePreviewFXML(styleSelection, "Prerequisitos");

        switch (styleSelection) {
            case "classic":
                previewPane.setStyle("-fx-background-color: #dddad3;");
                break;
            case "iqstyle":
                previewPane.setStyle("-fx-background-color: #e6e6e6;");
                break;
            default:
                break;
        }
    }

    private void loadStylePreviewFXML(String style, String type) throws IOException {
        FXMLLoader fxmlL = new FXMLLoader();
        fxmlL.setLocation(getClass().getResource("editor/subjects/" + style + "/" + type + ".fxml"));
        Node preview = fxmlL.load();
        previewPane.getChildren().add(preview);
        int x = 0;
        int y = 0;
        int i = -1;
        switch (type) {
            case "SubjectItem":
                controller = fxmlL.getController();
                controller.setStyle(style);
                controller.previewData();
                x = 136;
                y = 83;
                i = 0;
                break;
            case "Arrow":
                x = 311;
                y = 115;
                i = 1;
                break;
            case "Prerequisitos":
                x = 45;
                y = 148;
                i = 2;
                break;
        }
        previewPane.getChildren().get(i).setLayoutX(x);
        previewPane.getChildren().get(i).setLayoutY(y);
    }

    @FXML
    private void onColorOptionSelect() {
        if (colorOption.getSelectionModel().getSelectedItem().equals("Componente")) {
            colorList.setItems(generateColorsItems(componentsMap));
        } else if (colorOption.getSelectionModel().getSelectedItem().equals("Grupo")) {
            colorList.setItems(generateColorsItems(groupsMap));
        }
    }

    private ObservableList<HBox> generateColorsItems(HashMap<String, String> itemsMap) {
        ArrayList<HBox> itemList = new ArrayList<>();
        for (String itemText : itemsMap.keySet()) {
                Label label = new Label(itemText);
                ColorPicker colorPicker = new ColorPicker(Color.web(itemsMap.get(itemText)));
                colorPicker.setOnAction(e -> {
                    String option = colorOption.getSelectionModel().getSelectedItem();
                    Color color = colorPicker.getValue();
                    String hex = String.format( "#%02X%02X%02X",
                            (int)( color.getRed() * 255 ),
                            (int)( color.getGreen() * 255 ),
                            (int)( color.getBlue() * 255 ) );
                    if (option.equals("Componente")) {
                        componentsMap.replace(itemText, hex);
                    } else if (option.equals("Grupo")) {
                        groupsMap.replace(itemText, hex);
                    }
                    if (Objects.nonNull(controller)) {
                        controller.setSubjectColor(hex);
                    }
                });
                HBox item = new HBox();

                label.setMaxWidth(Double.MAX_VALUE);
                item.setHgrow(label, Priority.ALWAYS);

                item.getChildren().addAll(label, colorPicker);

                itemList.add(item);
        }
        return FXCollections.observableList(itemList);
    }

    @FXML
    private void createProject() throws IOException {
        Color backgroundColor = (Color) previewPane.getBackground().getFills().get(0).getFill();
        String backgroundhHex = String.format( "#%02X%02X%02X",
                (int)( backgroundColor.getRed() * 255 ),
                (int)( backgroundColor.getGreen() * 255 ),
                (int)( backgroundColor.getBlue() * 255 ) );
        String option = colorOption.getSelectionModel().getSelectedItem();
        HashMap<String, String> colorMap = new HashMap<>();
        int optionNumber = 0;
        if (option.equals("Grupo")) {
            optionNumber = 1;
            colorMap = groupsMap;
        } else {
            colorMap = componentsMap;
        }
        Pensum pensum = new Pensum(name.getText(), author.getText(), AcademicPrograms, new ArrayList<>(subjectSet), new ArrayList<>(groupsSet), style.getSelectionModel().getSelectedItem(), backgroundhHex, colorMap, optionNumber);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editor/Editor.fxml"));
        Scene scene = (Scene) previewPane.getScene();
        Parent editor = (Parent) fxmlLoader.load();
        EditorController ec = fxmlLoader.getController();
        ec.start(pensum);
        Stage stage = (Stage) scene.getWindow();
        stage.setTitle("Pensum Editor - " + pensum.getName());
        scene.setRoot(editor);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> colorOptions = new ArrayList<>();
        colorOptions.add("Componente");
        colorOptions.add("Grupo");
        ObservableList<String> colors = FXCollections.observableList(colorOptions);
        colorOption.setItems(colors);



        ArrayList<String> styleOption = new ArrayList<>();

        /*CodeSource codeSource = App.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String path = (jarFile.getParentFile().getPath() + "\\styles\\").replace("\\", "/");*/
        String path = "src/main/resources/com/pensumeditor/main/editor/subjects";
        File dir = new File(path);
        File[] directoryFiles = dir.listFiles();
        for (int i = 0; i < directoryFiles.length; i++) {
            if (directoryFiles[i].isDirectory()) {
                styleOption.add(directoryFiles[i].getName());
            }
        }
        ObservableList<String> styles = FXCollections.observableList(styleOption);
        style.setItems(styles);

        try {
            AcademicPrograms = subjectScraper.getAcademicPrograms();
            Nombre.setCellValueFactory(new PropertyValueFactory<>("name"));
            Tipo.setCellValueFactory(new PropertyValueFactory<>("type"));
            Sede.setCellValueFactory(new PropertyValueFactory<>("location"));
            //Codigo.setCellValueFactory(new PropertyValueFactory<>("code"));
            for (AcademicProgram academicProgram : AcademicPrograms) {
                APTable.getItems().add(academicProgram);
            }
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        componentsMap.put("Componente de Fundamentación", "#fbb03f");
        componentsMap.put("Componente de Formación Disciplinar o Profesional", "#1074bc");
        componentsMap.put("Trabajo de Grado", "#90268f");
        componentsMap.put("Componente de Libre Elección", "#0d9344");
    }
}

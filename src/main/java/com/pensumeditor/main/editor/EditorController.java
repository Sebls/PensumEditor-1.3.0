package com.pensumeditor.main.editor;

import com.pensumeditor.data.Pensum;
import com.pensumeditor.data.Subject;
import com.pensumeditor.main.App;
import com.pensumeditor.serializacion.Serialization;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class EditorController implements Serializable {
    
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Group Options;
    @FXML
    private ScrollPane topScrollPane;
    @FXML
    private AnchorPane semestreTopBar;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox scrollVBox;
    @FXML
    private AnchorPane topBarExport; 
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane bottomBarCredits; 
    @FXML
    private AnchorPane info;
    @FXML
    private Text watermark;
    @FXML
    private javafx.scene.control.MenuItem saveItem;
    
    private ArrayList<Subject[]> SubjectsMatrix = new ArrayList<>();
    private final ArrayList<Group> groups = new ArrayList<>();
    private final ArrayList<SubjectItemController> controllers = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    private final ArrayList<ImageView> imgsArray = new ArrayList<>();
    
    private int semestres;
    private int subjectCount = 0;

    private String projectPath;
    private Pensum pensum;
    private String style;
    private ArrayList<Subject> subjectsList;
    private HashMap<String, String> colorMap;
    private int colorOption;

    private final DraggableMaker draggableMaker = new DraggableMaker();  

    public void start(Pensum pensum) throws IOException {
        this.pensum = pensum;
        this.style = pensum.getStyle();
        this.SubjectsMatrix = pensum.getSubjectMatrix();
        this.colorMap = pensum.getColors();
        this.colorOption = pensum.getColorOption();
        this.subjectsList = pensum.getSubjects();

        this.subjects = subjects();
        this.semestres = SubjectsMatrix.size();

        if (Objects.nonNull(projectPath)) {
            saveItem.setDisable(false);
        }

        topBarExport.setStyle("-fx-background-color: #dddad3;");
        anchorPane.setStyle("-fx-background-color: #dddad3;");
        bottomBarCredits.setStyle("-fx-background-color: #dddad3;");
        info.setStyle("-fx-background-color: #dddad3;");

        // Zoom bar
        /*
        double defaultTopPrefHeight = topScrollPane.getPrefHeight();
        
        zoomSlider.valueProperty().addListener( (o, oldV, newV)->{
        scrollVBox.getTransforms().clear();
        semestreTopBar.getTransforms().clear();
        
        double zoom_fac = newV.doubleValue()/50;
        Scale scaleTransform = new Scale(scrollVBox.getScaleX() * zoom_fac, scrollVBox.getScaleY() * zoom_fac, 0, 0);
        Scale scaleTransformTop = new Scale(semestreTopBar.getScaleX() * zoom_fac, semestreTopBar.getScaleY() * zoom_fac, 0, 0);
        
        scrollVBox.getTransforms().add(scaleTransform);
        semestreTopBar.getTransforms().add(scaleTransformTop);
        topScrollPane.setMinHeight(defaultTopPrefHeight * zoom_fac);
        topScrollPane.setMaxHeight(defaultTopPrefHeight * zoom_fac);
        topScrollPane.setPrefHeight(defaultTopPrefHeight * zoom_fac);
        zoomLabel.setText(Double.toString(zoom_fac*100));
        });
        */
        // Vincular ScrollsBar Horizontales

        vinculeScrolls(topScrollPane, scrollPane);

        updateSubjects();

        for (int i = 0; i < SubjectsMatrix.size(); i++) {
            generarSemestre(semestreTopBar, i, 0);
        }

        createLines(bottomBarCredits, 30);

        draggableMaker.loadImgs(imgsArray);

    }

    public void loadPath(String path) {
        this.projectPath = path;
    }

    private void vinculeScrolls(ScrollPane f1, ScrollPane f2) {
        f1.hminProperty().bindBidirectional(f2.hminProperty());
        f1.hmaxProperty().bindBidirectional(f2.hmaxProperty());
        f1.hvalueProperty().bindBidirectional(f2.hvalueProperty());
    }
    
    private void setPrerequisitoArrow(ArrayList<ImageView> imgsArray, AnchorPane anchorPane, int subjectsNumber, int row, int column) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("subjects/" + style + "/Arrow.fxml"));
        try {
            ImageView img = fxmlLoader.load();
            imgsArray.add(img);
            anchorPane.getChildren().add(img);
            anchorPane.getChildren().get(subjectsNumber + imgsArray.size() - 1).setLayoutX(295+318*(row-1));
            anchorPane.getChildren().get(subjectsNumber + imgsArray.size() - 1).setLayoutY(100+200*column);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }
    
    private void setPrerequisito(ArrayList<ImageView> imgsArray, AnchorPane anchorPane, int subjectsNumber, int row, int column, String prerequisites) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("subjects/" + style + "/Prerequisitos.fxml"));
        try {
            ImageView img = fxmlLoader.load();
            imgsArray.add(img);
            anchorPane.getChildren().add(img);
            anchorPane.getChildren().get(subjectsNumber + imgsArray.size() - 1).setLayoutX(30+318*row);
            anchorPane.getChildren().get(subjectsNumber + imgsArray.size() - 1).setLayoutY(140+200*column);
            Tooltip t = new Tooltip(prerequisites);
            Tooltip.install(anchorPane.getChildren().get(subjectsNumber + imgsArray.size() - 1), t);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }
    
    private void generarSemestre(AnchorPane anchorPane, int i, int vertical) {
        Label semestreLabel = new Label("Semestre " + RomanNumerals(i+1));
        anchorPane.getChildren().add(semestreLabel);
        Label label = (Label) anchorPane.getChildren().get(i);
        label.setFont(Font.font("System", 14));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.web("3d3e3b"));
        label.setPrefSize(176, 30);
        label.setLayoutX(119+318*i);
        label.setLayoutY(5 + vertical);
    }
    
    private void generarCreditos(AnchorPane anchorPane, int creditos, int i) {
        Label creditosLabel = new Label("Creditos: " + String.valueOf(creditos));
        anchorPane.getChildren().add(creditosLabel);
        Label label = (Label) anchorPane.getChildren().get(i);
        label.setFont(Font.font("System", 14));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.web("3d3e3b"));
        label.setPrefSize(176, 37);
        label.setLayoutX(119+318*i);
        label.setLayoutY(30);
    }
    
    private void createLines(AnchorPane pane, double y) {
        Line upperLine = new Line();
        upperLine.setStartX(50);
        upperLine.setStartY(y);
        upperLine.setEndX(pane.getPrefWidth()-50);
        upperLine.setEndY(y);
        upperLine.setStrokeWidth(1.2);
        upperLine.setFill(Color.web("#626260"));
        pane.getChildren().add(upperLine);
        Line bottomline = new Line();
        bottomline.setStartX(50);
        bottomline.setStartY(y+40);
        bottomline.setEndX(pane.getPrefWidth()-50);
        bottomline.setEndY(y+40);
        bottomline.setStrokeWidth(1.2);
        bottomline.setFill(Color.web("#626260"));
        pane.getChildren().add(bottomline);
    }
    
    private void updatePrerequisites() {
        bottomBarCredits.getChildren().clear();
        for (int j = 0; j < SubjectsMatrix.size(); j++) {
            int creditos = 0;
            for (int k = 0; k < 6; k++) {
                if (Objects.nonNull(SubjectsMatrix.get(j)[k])) {
                    String prerequisites = SubjectsMatrix.get(j)[k].getPrerequisite();
                    String copy = prerequisites;
                    
                    creditos += SubjectsMatrix.get(j)[k].getCredits();
                    // Prerequisitos inmediatos
                    if (j > 0 && Objects.nonNull(SubjectsMatrix.get(j-1)[k])) {
                        String previous = SubjectsMatrix.get(j-1)[k].getName();
                        if (prerequisites.contains(previous)) {
                            setPrerequisitoArrow(imgsArray, anchorPane, groups.size(), j, k);
                        }
                        prerequisites = prerequisites.replace(previous, "");
                    }
                    // Prerequisitos
                    if (prerequisites.length()>4) {
                        setPrerequisito(imgsArray, anchorPane, groups.size(), j, k, copy);
                    }
                }
            }
            generarCreditos(bottomBarCredits, creditos, j);
        }
        createLines(bottomBarCredits, 30);
    }
    
    @FXML
    public void exportAsPNG() {
        showMessage("Exportando como PNG...");
        // Zoom-in the panel with the subjects
        Scale scaleTransform = new Scale(scrollVBox.getScaleX() * 2, scrollVBox.getScaleY() * 2, 0, 0);
        scrollVBox.getTransforms().add(scaleTransform);
        
        // Generate the semester labels and the watermark
        for (int i =  0; i < semestres; i++) {
            generarSemestre(topBarExport, i, 5);
        }
        watermark.setVisible(true);
        createLines(topBarExport, 5);
        
        // Take the screenshot
        WritableImage nodeshot = scrollVBox.snapshot(new SnapshotParameters(), null);
        
        // Delete the semester labels and hide the watermark
        topBarExport.getChildren().clear();
        watermark.setVisible(false);
        scrollVBox.getTransforms().remove(scaleTransform);
        
        // Get the path where the user want to save the file
        DirectoryChooser chooser = new DirectoryChooser();
        
        chooser.setTitle("Selecciona la ubicación donde deseas guardar el pensum");
        
        File defaultDirectory = new File("c:/");
        chooser.setInitialDirectory(defaultDirectory);
        
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        File choosed = chooser.showDialog(stage);
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");  
        LocalDateTime now = LocalDateTime.now();  
        
        // Save the file
        File file = new File(choosed.getAbsolutePath() + "/Pensum_"+ dtf.format(now) +".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
            
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        hideMessage();
    }
    
    @FXML
    public void exportAsPDF() {
        showMessage("Exportando como PDF...");
        // Zoom-in the panel with the subjects
        Scale scaleTransform = new Scale(scrollVBox.getScaleX() * 2, scrollVBox.getScaleY() * 2, 0, 0);
        scrollVBox.getTransforms().add(scaleTransform);
        
        double weidgt = scrollVBox.getWidth() * 2;
        double height = 2750;
        
        // Generate the semester labels and the watermark
        for (int i =  0; i < semestres; i++) {
            generarSemestre(topBarExport, i, 5);
        }
        createLines(topBarExport, 5);
        watermark.setVisible(true);
        
        // Take the screenshot
        WritableImage nodeshot = scrollVBox.snapshot(new SnapshotParameters(), null);
        
        // Delete the semester labels and hide the watermark
        topBarExport.getChildren().clear();
        watermark.setVisible(false);
        scrollVBox.getTransforms().remove(scaleTransform);
        
        // Create a file for save the screenshot
        File file = new File("pdf_export.png");
        
        // Save the screenshot
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
            
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        
        // Get the path where the user want to save the file
        DirectoryChooser chooser = new DirectoryChooser();
        
        chooser.setTitle("Selecciona la ubicación donde deseas guardar el pensum");
        
        File defaultDirectory = new File("c:/");
        chooser.setInitialDirectory(defaultDirectory);
        
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        File choosed = chooser.showDialog(stage);
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");  
        LocalDateTime now = LocalDateTime.now();  
        String path = choosed.getAbsolutePath() + "/Pensum_"+ dtf.format(now) + ".pdf";
        // Create a PDF File
        PDDocument pensum = new PDDocument();
        System.out.println(Math.round(weidgt));
        System.out.println(Math.round(height));
        PDPage page = new PDPage(new PDRectangle(Math.round(weidgt), Math.round(height)));
        PDImageXObject pdimage;
        PDPageContentStream content;
        try {
            pdimage = PDImageXObject.createFromFileByContent(file, pensum);
            content = new PDPageContentStream(pensum, page);
            content.drawImage(pdimage, 0, 0);
            content.close();
            pensum.addPage(page);
            // Save the PDF
            pensum.save(path);
            pensum.close();
            // Delete the PNG
            file.delete();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        // Open file
        try {
            File pdf = new File(path);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(pdf);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        hideMessage();
    }
    
    public void readjustPanels() {
        topScrollPane.setPrefSize(300*(1 + semestres), 36);
        scrollVBox.setPrefSize(300*(1 + semestres), 1300);
        semestreTopBar.setPrefSize(300*(1 + semestres), 36);
        topBarExport.setPrefSize(300*(1 + semestres), 36);
        anchorPane.setPrefSize(300*(1 + semestres), 1300);
        bottomBarCredits.setPrefSize(300*(1 + semestres), 60);
        info.setPrefSize(300*(1 + semestres), 30);
    }
    
    @FXML
    public void addSemester() {
        for (ImageView img : imgsArray) {
            anchorPane.getChildren().remove(img);
        }
        imgsArray.clear();
        
        semestres += 1;
        
        readjustPanels();
        
        SubjectsMatrix.add(new Subject[6]);
        subjects = subjects();
        
        generarSemestre(semestreTopBar, semestres - 1, 0);
        
        for (int i = 0; i < 6; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("subjects/" + style + "/SubjectItem.fxml"));
            try {
                Group group = fxmlLoader.load();
                SubjectItemController sic = fxmlLoader.getController();
                sic.setSubjectData(subjects.get((semestres - 1)*6 + i), colorMap, colorOption);
                groups.add(group);
                controllers.add(sic);
                anchorPane.getChildren().add(group);
                anchorPane.getChildren().get((semestres - 1)*6 + i).setLayoutX(120+318*(semestres - 1));
                anchorPane.getChildren().get((semestres - 1)*6 + i).setLayoutY(50+200*i);
                draggableMaker.makeDraggable(groups.get((semestres - 1)*6 + i), controllers.get((semestres - 1)*6 + i));
                
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }
        draggableMaker.loadPanels(anchorPane, scrollPane, bottomBarCredits, style);
        updatePrerequisites();

    }
    
    @FXML
    public void removeSemester() {
        for (ImageView img : imgsArray) {
            anchorPane.getChildren().remove(img);
        }
        imgsArray.clear();
        
        semestreTopBar.getChildren().remove(semestreTopBar.getChildren().size() - 1);
        
        semestres -= 1;
        readjustPanels();
        
        SubjectsMatrix.remove(SubjectsMatrix.size() - 1);
        subjects = subjects();
        
        for (int i = 0; i < 6; i++) {
            anchorPane.getChildren().remove(groups.get(groups.size() - 1));
            groups.remove(groups.size() - 1);
            controllers.remove(controllers.size() - 1);
        }
        
        updatePrerequisites();
        
    }

    private void checkSubjectCount() {
        if (this.subjectCount == 0) {
            Options.setDisable(true);
            Options.setOpacity(0.5);
        } else {
            Options.setDisable(false);
            Options.setOpacity(1);
        }
    }

    public void updateSubjects()  {
        checkSubjectCount();

        groups.clear();
        controllers.clear();
        anchorPane.getChildren().clear();
        imgsArray.clear();

        for (int i=0; i<subjects.size(); i++) {
            FXMLLoader fxmlL = new FXMLLoader();
            fxmlL.setLocation(getClass().getResource("subjects/" + style + "/SubjectItem.fxml"));
            try {
                Group group = fxmlL.load();
                SubjectItemController sic = fxmlL.getController();
                sic.setStyle(style);
                sic.setSubjectData(subjects.get(i), colorMap, colorOption);
                groups.add(group);
                controllers.add(sic);
            } catch (IOException ex) {
                //ex.printStackTrace();
            }

        }
        draggableMaker.loadPanels(anchorPane, scrollPane, bottomBarCredits, style);
        draggableMaker.loadGroups(groups);
        draggableMaker.loadSubjectsMatrix(SubjectsMatrix);
        anchorPane.getChildren().addAll(groups);

        readjustPanels();

        bottomBarCredits.getChildren().clear();

        int index = 0;
        for (int i = 0; i < SubjectsMatrix.size(); i++) {
            int creditos = 0;
            for (int e = 0; e < 6; e++) {

                // Asignaturas
                anchorPane.getChildren().get(index).setLayoutX(120+318*i);
                anchorPane.getChildren().get(index).setLayoutY(50+200*e);

                draggableMaker.makeDraggable(groups.get(index), controllers.get(index));
                if (Objects.nonNull(SubjectsMatrix.get(i)[e])) {
                    Subject actualSubject = SubjectsMatrix.get(i)[e];
                    String prerequisites = actualSubject.getPrerequisite();
                    String copy = prerequisites;
                    // Prerequisitos inmediatos
                    if (i > 0 && Objects.nonNull(SubjectsMatrix.get(i-1)[e])) {
                        String previous = SubjectsMatrix.get(i-1)[e].getName();
                        if (prerequisites.contains(previous)) {
                            setPrerequisitoArrow(imgsArray, anchorPane, subjects.size(), i, e);
                        }
                        prerequisites = prerequisites.replace(previous, "");
                    }

                    // Prerequisitos
                    if (prerequisites.length()>4) {
                        setPrerequisito(imgsArray, anchorPane, subjects.size(), i, e, copy);
                    }

                    // Contar creditos
                    creditos += actualSubject.getCredits();
                }
                index ++;
            }
            // Generar creditos
            generarCreditos(bottomBarCredits, creditos, i);
        }
        
        createLines(bottomBarCredits, 30);
        
        draggableMaker.loadImgs(imgsArray);
    }
    
    public static Scene scene;
    
    @FXML
    public void addSubject() throws IOException {
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SubjectSelector.fxml"));
        VBox selector = fxmlLoader.load();
        SubjectSelectorController ssc = fxmlLoader.getController();
        ssc.loadSubjects(subjectsList);
        ssc.loadSubjectsMatrix(SubjectsMatrix);
        scene = new Scene(selector, 1100, 600);
        Stage stage = new Stage();
        stage.setTitle("Subject Selector");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("pensum_editor_icon.png")));
        stage.setScene(scene);
        stage.showAndWait();

        this.subjectsList = ssc.getSubjects();
        Subject subject = (Subject) ssc.getSelectedSubject();
        if (Objects.nonNull(subject)) {
            ssc.openPositionSelector();

            int[] position = ssc.getPosition();

            SubjectsMatrix.get(position[0])[position[1]] = subject;
            this.subjects = subjects();

            updateSubjects();
        }
    }
    
    private Boolean state = false;
    private String option = "";
    
    public void setState(Boolean state, String option) {
        this.state = true;
        this.option = option;
    }
    
    @FXML
    public void clickedSubject(MouseEvent e) throws IOException {
        if (state) {
            for (Node node :anchorPane.getChildren()) {
                if (node.isVisible() && node.getBoundsInParent().contains(e.getX(), e.getY())) {
                    int i = groups.indexOf(node);
                    if (i != -1) {
                        Long x = Math.round((node.getBoundsInParent().getMinX() - 120)/318);
                        Long y = Math.round((node.getBoundsInParent().getMinY() - 50)/200);
                        switch (option) {
                            case "remove":
                                SubjectsMatrix.get(x.intValue())[y.intValue()] = null;
                                this.subjects = subjects();
                                updateSubjects();
                                setState(false, "");
                                hideMessage();
                                break;
                            case "edit":
                                SubjectsMatrix.get(x.intValue())[y.intValue()] = controllers.get(i).subjectSelector(subjectsList);
                                this.subjects = subjects();
                                setState(false, "");
                                hideMessage();
                                break;
                            case "changeColor":
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("ColorModifier.fxml"));
                                AnchorPane colorModifier = fxmlLoader.load();
                                ColorModifierController cmc = fxmlLoader.getController();
                                scene = new Scene(colorModifier, 490, 290);
                                Stage stage = new Stage();
                                stage.setTitle("Color Modifier");
                                stage.getIcons().add(new Image(getClass().getResourceAsStream("pensum_editor_icon.png")));
                                stage.setResizable(false);
                                stage.setScene(scene);
                                cmc.loadColor(controllers.get(i).getActualColor(), colorOption);
                                cmc.updateRectangleColor();
                                stage.showAndWait();
                                Color backgroundColor = cmc.getColor();
                                if (backgroundColor != null) {
                                    String hexBackground = String.format( "#%02X%02X%02X", (int)( backgroundColor.getRed() * 255 ), (int)( backgroundColor.getGreen() * 255 ), (int)( backgroundColor.getBlue() * 255 ) );
                                    if (cmc.getCheckBoxChoose()) {
                                        if (colorOption == 0) {
                                            colorMap.replace(controllers.get(i).getComponent(), hexBackground);
                                        } else if (colorOption == 1) {
                                            colorMap.replace(controllers.get(i).getGroup(), hexBackground);
                                        }
                                        for (SubjectItemController controller : controllers) {
                                            controller.updateColorMap(colorMap);
                                            if ((!controller.equals(controllers.get(i)))) {
                                                if (colorOption == 0 && controller.getComponent().equals(controllers.get(i).getComponent())) {
                                                    controller.setSubjectColor(hexBackground);
                                                } else if (colorOption == 1 && controller.getGroup().equals(controllers.get(i).getGroup())) {
                                                    controller.setSubjectColor(hexBackground);
                                                }
                                            }
                                        }
                                    }
                                    controllers.get(i).setSubjectColor(hexBackground);
                                }
                                setState(false, "");
                                hideMessage();
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    Label label;
    
    private void showMessage(String message) {
        menuVisible(false);
        label = new Label(message);
        label.setPrefWidth(menuPane.getWidth());
        label.setPrefHeight(menuPane.getHeight());
        label.setFont(Font.font("ROBOTO", FontWeight.BOLD, 19));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.web("626260"));
        menuPane.getChildren().add(label);
    }
    
    private void hideMessage() {
        menuPane.getChildren().remove(label);
        menuVisible(true);
    }
    
    private void menuVisible(Boolean mode) {
        for (Node menuItem : menuPane.getChildren()) {
            menuItem.setVisible(mode);
        }
    }
    
    @FXML
    public void editSubject() {
        setState(true, "edit");
        showMessage("Selecciona la asignatura que quieres reemplazar");
    }
    
    @FXML
    public void deleteSubject() {
        setState(true, "remove");
        showMessage("Selecciona la asignatura que quieres remover");
    }
    
    @FXML
    public void editBackgroundColor() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ColorPicker.fxml"));
        AnchorPane colorPicker = fxmlLoader.load();
        ColorPickerController cpc = fxmlLoader.getController();
        scene = new Scene(colorPicker, 330, 150);
        Stage stage = new Stage();
        stage.setTitle("Color Picker");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("pensum_editor_icon.png")));
        stage.setResizable(false);
        stage.setScene(scene);
        if (!anchorPane.getStyle().equals("")) {
            cpc.loadColor(anchorPane.getStyle().substring(22, 29));
        }
        stage.showAndWait();

        Color color = cpc.getColor();
        if (color != null) {
            String hex = String.format( "#%02X%02X%02X", (int)( color.getRed() * 255 ), (int)( color.getGreen() * 255 ), (int)( color.getBlue() * 255 ) );
            topBarExport.setStyle("-fx-background-color: " + hex + ";");
            anchorPane.setStyle("-fx-background-color: " + hex + ";");
            bottomBarCredits.setStyle("-fx-background-color: " + hex + ";");
            info.setStyle("-fx-background-color: " + hex + ";");
        }
    }
    
    @FXML
    public void editSubjectsColor() {
        setState(true, "changeColor");
        showMessage("Selecciona la asignatura a la cual le quieres modificar el color");
    }

    @FXML
    public void openMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Main.fxml"));
        Scene scene = (Scene) anchorPane.getScene();
        scene.setRoot(fxmlLoader.load());
    }
    @FXML
    public void openProject() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir proyecto");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        File project = fileChooser.showOpenDialog(stage);
        if (Objects.nonNull(project) && Objects.nonNull(project.getAbsolutePath())) {
            String projectPath = project.getAbsolutePath();
            Pensum pensum = Serialization.ObjectDeserialization(projectPath, Pensum.class);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Editor.fxml"));
            Scene scene = (Scene) anchorPane.getScene();
            Parent editor = (Parent) fxmlLoader.load();
            EditorController ec = fxmlLoader.getController();
            ec.loadPath(projectPath);
            ec.start(pensum);
            scene.setRoot(editor);
        }
    }
    @FXML
    public void saveAs() {
        pensum.setSubjectMatrix(this.SubjectsMatrix);
        pensum.setSubjects(this.subjectsList);
        pensum.setColors(this.colorMap);
        pensum.setColorOption(this.colorOption);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar proyecto");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        File project = fileChooser.showSaveDialog(stage);

        if (Objects.nonNull(project.getAbsolutePath())) {
            this.projectPath = project.getAbsolutePath();
            Serialization.ObjectSerialization(project.getAbsolutePath() + ".pns", pensum);
        }
    }
    @FXML
    public void save() {
        pensum.setSubjectMatrix(this.SubjectsMatrix);
        pensum.setSubjects(this.subjectsList);
        pensum.setColors(this.colorMap);
        pensum.setColorOption(this.colorOption);

        Serialization.ObjectSerialization(projectPath, pensum);
    }

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

    // Load Subjects Info
    private List<Subject> subjects() {
        List<Subject> sj = new ArrayList<>();
        this.subjectCount = 0;

        ArrayList<Subject[]> matrix = SubjectsMatrix;
        
        for (Subject[] Subjects : matrix) {
            for (Subject s : Subjects) {
                if (Objects.nonNull(s)) {       
                    sj.add(s);
                    this.subjectCount ++;
                    continue;
                }
                sj.add(new Subject(0));
            }
        }
        return sj;
    }
    
    // Roman Numerals - From: https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java
    
    public static String RomanNumerals(int Int) {
        LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
        roman_numerals.put("M", 1000);
        roman_numerals.put("CM", 900);
        roman_numerals.put("D", 500);
        roman_numerals.put("CD", 400);
        roman_numerals.put("C", 100);
        roman_numerals.put("XC", 90);
        roman_numerals.put("L", 50);
        roman_numerals.put("XL", 40);
        roman_numerals.put("X", 10);
        roman_numerals.put("IX", 9);
        roman_numerals.put("V", 5);
        roman_numerals.put("IV", 4);
        roman_numerals.put("I", 1);
        String res = "";
        for(Map.Entry<String, Integer> entry : roman_numerals.entrySet()){
          int matches = Int/entry.getValue();
          res += repeat(entry.getKey(), matches);
          Int = Int % entry.getValue();
        }
        return res;
      }
    public static String repeat(String s, int n) {
        if(s == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
      }
    
    
}

    

package com.pensumeditor.main.editor;

import com.pensumeditor.data.Subject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class SubjectItemController implements Initializable {

    @FXML
    private Group grupo;

    @FXML
    private Label codigoLabel;

    @FXML
    private Label compoLabel;

    @FXML
    private Label grupoLabel;

    @FXML
    private Rectangle colorRectangle;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label creditosLabel;

    private String style;
    private Label styleLabel;

    public void previewData() {
        codigoLabel.setText("120120");
        nombreLabel.setText("Nombre");
        creditosLabel.setText("4");
        grupoLabel.setText("Grupo");
        compoLabel.setText("Comp");
    }

    public void setStyle(String style) {
        this.style = style;
        switch (style) {
            case "classic":
                styleLabel = grupoLabel;
                break;

            case "iqstyle":
                styleLabel = creditosLabel;
                break;

            default:
                break;
        }
    }

    private static Scene scene;
    
    public void setSubjectColor(String hex_color) {
        colorRectangle.setFill(Color.web(hex_color));
        Color colorFill = (Color) colorRectangle.getFill();
        double L = (colorFill.getRed()*299 +   colorFill.getGreen()*587 + colorFill.getBlue()*114) * 255 / 1000;
        if (L >= 128) {
            styleLabel.setTextFill(Color.web("#3d3e3b"));
        } else {
            styleLabel.setTextFill(Color.web("#F5F5F5"));
        }
    }
    
    private Subject subject;
    private  HashMap<String, String> colorMap = new HashMap<>();
    private int colorOption;
    
    public void setSubjectData(Subject subject, HashMap<String, String> colorMap, int colorOption) {
        this.subject = subject;
        this.colorMap = colorMap;
        this.colorOption = colorOption;
        if (subject.getCode() != 0 && subject.getCode() != 1) {
            creditosLabel.setText(String.valueOf(subject.getCredits()));
            nombreLabel.setText(subject.getName());
            codigoLabel.setText(String.valueOf(subject.getCode()));
            grupoLabel.setText(subject.getGroup());
            
            if (Objects.nonNull(subject.getComponent())) {
                switch (subject.getComponent()) {
                    case "COMPONENTE DE FUNDAMENTACIÓN":
                        compoLabel.setText("B");
                        break;

                    case "COMPONENTE DE FORMACIÓN DISCIPLINAR O PROFESIONAL":
                        compoLabel.setText("C");
                        break;

                    case "COMPONENTE DE LIBRE ELECCIÓN":
                        compoLabel.setText("L");
                        break;

                    default:
                        break;
                }
            }
            if (colorOption == 0 && colorMap.containsKey(subject.getComponent())) {
                String hex = colorMap.get(subject.getComponent());
                setSubjectColor(hex);
            } else if (colorOption == 1 && colorMap.containsKey(subject.getGroup())) {
                String hex = colorMap.get(subject.getGroup());
                setSubjectColor(hex);
            }
            /*else {
                System.out.println("Invalid color option: " + Integer.toString(colorOption));
                System.out.println(colorMap.keySet());
                System.out.println(subject.getComponent());
                System.out.println(subject.getGroup());
            }*/

        } else if (subject.getCode() == 1) {
            creditosLabel.setText(String.valueOf(subject.getCredits()));
            codigoLabel.setText("");
            nombreLabel.setText("");
            compoLabel.setText("L");
            grupoLabel.setText(subject.getGroup());
            setSubjectColor("e4e4e4");
        } else {
            grupo.setVisible(false);
        }

    }

    @FXML
    public Subject subjectSelector(ArrayList<Subject> subjects) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SubjectSelector.fxml"));
            VBox selector = fxmlLoader.load();
            SubjectSelectorController ssc = fxmlLoader.getController();
            ssc.loadSubjects(subjects);
            scene = new Scene(selector, 1100, 600);
            Stage stage = new Stage();
            stage.setTitle("Subject Selector");
            stage.setScene(scene);
            stage.showAndWait();
            if ((Subject) ssc.getSelectedSubject() != null) {
                setSubjectData((Subject) ssc.getSelectedSubject(), this.colorMap, this.colorOption);
            }
            return (Subject) ssc.getSelectedSubject();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        return this.subject;
    }
    
    public Subject getSubject() {
        return this.subject;
    }
    
    public String getActualColor() {
        Color rectangleColor = (Color) colorRectangle.getFill();
        return String.format( "#%02X%02X%02X",
            (int)( rectangleColor.getRed() * 255 ),
            (int)( rectangleColor.getGreen() * 255 ),
            (int)( rectangleColor.getBlue() * 255 ) );
    }
    
    public String getGroup() {
        if (subject.getGroup() != null) {
            return subject.getGroup();
        }
        return "";
    }

    public String getComponent() {
        if (subject.getComponent() != null) {
            return subject.getComponent();
        }
        return "";
    }

    public void loadColorMap(HashMap<String, String> colorMap, int colorOption) {

    }
    public void updateColorMap(HashMap<String,String> colorMap) {
        this.colorMap = colorMap;
    }
    public HashMap<String,String> getColorMap() {
        return colorMap;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
}
package com.pensumeditor.main.editor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ColorModifierController {
    
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Rectangle grupoRectangle;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Label grupoLabel;
    
    private Color color;
    
    @FXML
    public void setColorPicked() {
        this.color = (Color) grupoRectangle.getFill();

        Stage stage = (Stage) colorPicker.getScene().getWindow();
        stage.close();
    }
    
    public Boolean getCheckBoxChoose() {
        return checkBox.isSelected();
    }
    
    public Color getColor() {
        return color;
    }
    
    public void loadColor(String color_hex, int colorOption) {
        if (colorOption == 0) {
            checkBox.setText("Modificar todas las asignaturas que compartan este componente");
        } else if (colorOption == 1) {
            checkBox.setText("Modificar todas las asignaturas que compartan este grupo");
        }
        colorPicker.setValue(Color.web(color_hex));
    }
    
    @FXML
    public void updateRectangleColor() {
        grupoRectangle.setFill(colorPicker.getValue());
        double L = (colorPicker.getValue().getRed()*299 +   colorPicker.getValue().getGreen()*587 + colorPicker.getValue().getBlue()*114) * 255 / 1000;
        if (L >= 128) {
            grupoLabel.setTextFill(Color.web("#3d3e3b"));
        } else {
            grupoLabel.setTextFill(Color.web("#F5F5F5"));
        }
    }
}

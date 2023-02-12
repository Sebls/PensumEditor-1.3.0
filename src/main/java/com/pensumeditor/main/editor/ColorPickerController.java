package com.pensumeditor.main.editor;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ColorPickerController {
    
    @FXML
    private ColorPicker colorPicker;
    
    private Color color;
    
    @FXML
    public void setColorPicked() {
        this.color = colorPicker.getValue();
        Stage stage = (Stage) colorPicker.getScene().getWindow();
        stage.close();
    }
    
    public Color getColor() {
        return color;
    }
    
    public void loadColor(String color_hex) {
        colorPicker.setValue(Color.web(color_hex));
    }
    
}

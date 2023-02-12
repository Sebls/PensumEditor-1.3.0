package com.pensumeditor.main.editor;

import com.pensumeditor.data.Subject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class PositionSelectorController implements Initializable{

    @FXML
    private GridPane gridPane;
    
    private ArrayList<Subject[]> SubjectsMatrix;
    private int[] position;
    
    public void loadSubjectsMatrix(ArrayList<Subject[]> subjectsMatrix) {
        this.SubjectsMatrix = subjectsMatrix;
    }
    
    public int[] getPosition() {
        return this.position;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void loadFreeGridPane() {
        // Positionate free places
        for (int i = 0; i < SubjectsMatrix.size(); i++) {
            for (int e = 0; e < 6; e++) {
                if (Objects.isNull(SubjectsMatrix.get(i)[e])) {
                    Rectangle subject = new Rectangle(30, 30, Color.web("e4e4e4"));
                    subject.setStroke(Color.web("3d3e3b"));
                    gridPane.add(subject, i, e);
                    gridPane.setHalignment(subject, javafx.geometry.HPos.CENTER);
                    gridPane.setValignment(subject, javafx.geometry.VPos.CENTER);
                    subject.setOnMouseEntered(mouseEvent -> {
                        if (!mouseEvent.isPrimaryButtonDown()) {
                            subject.getScene().setCursor(Cursor.HAND);
                        }
                    });
                    subject.setOnMouseExited(mouseEvent -> {
                        if (!mouseEvent.isPrimaryButtonDown()) {
                            subject.getScene().setCursor(Cursor.DEFAULT);
                        }
                    });
                    subject.setOnMousePressed(mouseEvent -> {
                        position = new int[2];
                        position[1] = gridPane.getRowIndex(subject);
                        position[0] = gridPane.getColumnIndex(subject);
                        Stage stage = (Stage) gridPane.getScene().getWindow();
                        stage.close();
                    });
                }
            }
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100/SubjectsMatrix.size());
            gridPane.getColumnConstraints().add(column);
        }
    }
}

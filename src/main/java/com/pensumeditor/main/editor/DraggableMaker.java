package com.pensumeditor.main.editor;

import com.pensumeditor.data.Subject;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DraggableMaker {

    private double mouseAnchorX;
    private double mouseAnchorY;
    private Bounds initialBounds;
    private Group groupSelected;
    
    public AnchorPane anchorPane;
    public ScrollPane scrollPane;
    public AnchorPane bottomBarCredits;

    public String style;

    public ArrayList<Group> groups = new ArrayList<>();
    public ArrayList<ImageView> imgsArray = new ArrayList<>();
    public ArrayList<Subject[]> subjectsMatrix = new ArrayList<>();
    

    public void makeDraggable(Group group, SubjectItemController subjectItemController){

        group.setOnMouseEntered(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                group.getScene().setCursor(Cursor.HAND);
            }
        });
        
        group.setOnMouseExited(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                group.getScene().setCursor(Cursor.DEFAULT);
                group.setScaleX(1);
                group.setScaleY(1);
                group.setOpacity(1);
                groupSelected = new Group();
            }
        });
        
        group.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
            groupSelected = group;
            groupSelected.toFront();
            initialBounds = groupSelected.getBoundsInParent();
            draggable(anchorPane);
        });
        
        group.setOnMouseReleased(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                //int groupSelectedIndex = groups.indexOf(groupSelected);
                //System.out.println(groupSelectedIndex);
                bottomBarCredits.getChildren().clear();
                for (int i = 0; i < groups.size(); i++) {
                    if (!groupSelected.equals(groups.get(i))) {
                        Bounds finalBounds = groups.get(i).getBoundsInParent();
                        Bounds actualBounds = groupSelected.getBoundsInParent();
                        if (finalBounds.intersects(actualBounds)) {
                            int add = 0;
                            if (style.equals("classic")) {
                                add = 1;
                            }
                            groupSelected.setLayoutX(finalBounds.getMinX()+add);
                            groupSelected.setLayoutY(finalBounds.getMinY()+add);
                            groups.get(i).setLayoutX(initialBounds.getMinX()+add);
                            groups.get(i).setLayoutY(initialBounds.getMinY()+add);
                            
                            int initial_i = (int) Math.round((initialBounds.getMinX()-120)/318);
                            int initial_e = (int) Math.round((initialBounds.getMinY()-50)/200);
                            int final_i = (int) Math.round((finalBounds.getMinX()-120)/318);
                            int final_e = (int) Math.round((finalBounds.getMinY()-50)/200);
                            
                            Subject copy = subjectsMatrix.get(initial_i)[initial_e];
                            subjectsMatrix.get(initial_i)[initial_e] = subjectsMatrix.get(final_i)[final_e];
                            subjectsMatrix.get(final_i)[final_e] = copy;

                            break;
                        } else if (i == groups.size() - 1) {
                            groupSelected.setLayoutX(initialBounds.getMinX() + 1);
                            groupSelected.setLayoutY(initialBounds.getMinY() + 1);
                        }
                    } 
                }
                
                for (int j = 0; j < subjectsMatrix.size(); j++) {
                    int creditos = 0;
                    for (int k = 0; k < 6; k++) {
                        if (Objects.nonNull(subjectsMatrix.get(j)[k])) {
                            String prerequisites = subjectsMatrix.get(j)[k].getPrerequisite();
                            String copy = prerequisites;
                            
                            creditos += subjectsMatrix.get(j)[k].getCredits();
                            // Prerequisitos inmediatos
                            if (j > 0 && Objects.nonNull(subjectsMatrix.get(j-1)[k])) {
                                String previous = subjectsMatrix.get(j-1)[k].getName();
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
                createLines();
                
                groupSelected.setScaleX(1);
                groupSelected.setScaleY(1);
                group.setOpacity(1);
                groupSelected = new Group();
            }
        });

    }
    
    public void draggable(AnchorPane anchorPane) {
        anchorPane.setOnMouseDragged(mouseEvent -> {
            groupSelected.setLayoutX(mouseEvent.getX() - mouseAnchorX);
            groupSelected.setLayoutY(mouseEvent.getY() - mouseAnchorY);
            for (ImageView img : imgsArray) {
                anchorPane.getChildren().remove(img);
            }
            imgsArray.clear();
            groupSelected.setScaleX(0.9);
            groupSelected.setScaleY(0.9);
            groupSelected.setOpacity(0.6);
        });
    }
    
    public void loadPanels(AnchorPane anchorPane, ScrollPane scrollPane, AnchorPane bottomBarCredits, String style) {
        this.anchorPane = anchorPane;
        this.scrollPane = scrollPane;
        this.bottomBarCredits = bottomBarCredits;
        this.style = style;
    }
    
    public void loadGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    
    public void loadImgs(ArrayList<ImageView> imgsArray) {
        this.imgsArray = imgsArray;
    }
    
    public void loadSubjectsMatrix(ArrayList<Subject[]> SubjectsMatrix) {
        this.subjectsMatrix = SubjectsMatrix;
    }
    
    public ArrayList<Subject[]> getSubjectsMatrix() {
        return this.subjectsMatrix;
    }
    
    public ArrayList<Group> getGroups() {
        return this.groups;
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
    
    private void createLines() {
        Line upperLine = new Line();
        upperLine.setStartX(50);
        upperLine.setStartY(30);
        upperLine.setEndX(bottomBarCredits.getPrefWidth()-50);
        upperLine.setEndY(30);
        bottomBarCredits.getChildren().add(upperLine);
        Line bottomline = new Line();
        bottomline.setStartX(50);
        bottomline.setStartY(70);
        bottomline.setEndX(bottomBarCredits.getPrefWidth()-50);
        bottomline.setEndY(70);
        bottomBarCredits.getChildren().add(bottomline);
    }
    
}
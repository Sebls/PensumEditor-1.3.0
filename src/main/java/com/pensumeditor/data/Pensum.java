package com.pensumeditor.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Pensum implements Serializable {
    private String name;
    private String author;
    private ArrayList<AcademicProgram> academicPrograms = new ArrayList<>();
    private ArrayList<Subject[]> subjectMatrix = new ArrayList<>();
    private ArrayList<Subject> subjects;
    private ArrayList<String> groups;
    private String style;
    private String backgroundColor;

    // colorOption values
    // 0 -> Color by Component
    // 1 -> Color by Group
    private HashMap<String,String> colors = new HashMap<>();
    private int colorOption;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<Subject[]> getSubjectMatrix() {
        return subjectMatrix;
    }

    public void setSubjectMatrix(ArrayList<Subject[]> subjectMatrix) {
        this.subjectMatrix = subjectMatrix;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public HashMap<String, String> getColors() {
        return colors;
    }

    public void setColors(HashMap<String, String> colors) {
        this.colors = colors;
    }

    public int getColorOption() {
        return colorOption;
    }

    public void setColorOption(int colorOption) {
        this.colorOption = colorOption;
    }

    public ArrayList<AcademicProgram> getAcademicPrograms() {
        return academicPrograms;
    }

    public void setAcademicPrograms(ArrayList<AcademicProgram> academicPrograms) {
        this.academicPrograms = academicPrograms;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Pensum(String name, String author, ArrayList<AcademicProgram> academicPrograms, ArrayList<Subject[]> subjectMatrix, ArrayList<Subject> subjects, ArrayList<String> groups, String style, String backgroundColor, HashMap<String,String> colors, int colorOption) {
        this.name = name;
        this.author = author;
        this.academicPrograms = academicPrograms;
        this.subjectMatrix = subjectMatrix;
        this.subjects = subjects;
        this.groups = groups;
        this.style = style;
        this.backgroundColor = backgroundColor;
        this.colors = colors;
        this.colorOption = colorOption;
    }

    public Pensum(String name, String author, ArrayList<AcademicProgram> academicPrograms, ArrayList<Subject[]> subjectMatrix, ArrayList<Subject> subjects, ArrayList<String> groups) {
        this.name = name;
        this.author = author;
        this.academicPrograms = academicPrograms;
        this.subjectMatrix = subjectMatrix;
        this.subjects = subjects;
        this.groups = groups;

        style = "classic";
        backgroundColor = "#dddad3";
        colors.put("COMPONENTE DE FUNDAMENTACIÓN", "#fdcc09");
        colors.put("COMPONENTE DE FORMACIÓN DISCIPLINAR O PROFESIONAL", "#518a7b");
        colors.put("COMPONENTE DE LIBRE ELECCIÓN", "#4968b1");
        colorOption = 0;
    }

    public Pensum(String name, String author, ArrayList<AcademicProgram> academicPrograms, ArrayList<Subject> subjects, ArrayList<String> groups, String style, String backgroundColor, HashMap<String,String> colors, int colorOption) {
        this.name = name;
        this.author = author;
        this.academicPrograms = academicPrograms;
        this.subjects = subjects;
        this.groups = groups;
        this.style = style;
        this.backgroundColor = backgroundColor;
        this.colors = colors;
        this.colorOption = colorOption;

        for (int i = 0; i < 10; i++) {
            subjectMatrix.add((Subject[]) new Subject[6]);
        }
    }

    public Pensum(String name, String author, ArrayList<AcademicProgram> academicPrograms, ArrayList<Subject> subjects, ArrayList<String> groups) {
        this.name = name;
        this.author = author;
        this.academicPrograms = academicPrograms;
        this.subjects = subjects;
        this.groups = groups;

        style = "classic";
        backgroundColor = "#dddad3";
        colors.put("COMPONENTE DE FUNDAMENTACIÓN", "#fdcc09");
        colors.put("COMPONENTE DE FORMACIÓN DISCIPLINAR O PROFESIONAL", "#518a7b");
        colors.put("COMPONENTE DE LIBRE ELECCIÓN", "#4968b1");
        colorOption = 0;

        for (int i = 0; i < 10; i++) {
            subjectMatrix.add((Subject[]) new Subject[6]);
        }
    }
}

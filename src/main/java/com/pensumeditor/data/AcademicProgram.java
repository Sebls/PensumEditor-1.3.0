package com.pensumeditor.data;

import java.io.Serializable;
import java.util.ArrayList;

public class AcademicProgram implements Serializable {
    private String code;
    private String name;
    private String type;
    private String location;
    private String URL;
    private ArrayList<Subject> subjects;
    private ArrayList<String> groups;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public AcademicProgram(String code, String name, String type, String location, String URL) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.location = location;
        this.URL = "https://programasacademicos.unal.edu.co/" + URL;
    }

}

package com.pensumeditor.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Subject implements Serializable {

    private int code;
    private String name;
    private int credits;
    private String prerequisite;
    private String group;
    private String component;

    public int getCode() {
        return code;
    }

    public int getCredits() {
        return credits;
    }

    public String getName() {
        return name;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public String getGroup() {
        return group;
    }

    public String getComponent() {
        return component;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Subject(int code, String name, int credits, String group, String prerequisite, String component) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.group = group;
        this.prerequisite = prerequisite;
        this.component = component;
    }

    public Subject(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, credits, group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return code == subject.code &&
                name.equals(subject.name) &&
                credits == subject.credits &&
                group.equals(subject.group);
    }
}

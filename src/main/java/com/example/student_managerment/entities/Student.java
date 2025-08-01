package com.example.student_managerment.entities;

import java.util.List;
import java.util.stream.Collectors;

public class Student {
    private int id;
    private String name;
    private int gender;
    private String email;
    private String phone;
    private Group group;
    private List<Subject> subjects;
    private String subjectNames;

    public Student(int id, String name, int gender, String email, String phone, Group group) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.group = group;
    }

    public Student(String name, int gender, String email, String phone, Group group) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.group = group;
    }

    public Student(String name, int gender, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public Student(int id, String name, int gender, String email, String phone) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Group getGroups() {
        return group;
    }

    public void setGroups(Group groups) {
        this.group = groups;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getSubjectNames() {
        if (subjectNames != null) return subjectNames;
        if (subjects == null || subjects.isEmpty()) {
            return "Chưa có môn học";
        }
        return subjects.stream().map(Subject::getName).collect(Collectors.joining(", "));
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

}

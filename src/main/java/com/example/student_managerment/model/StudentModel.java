package com.example.student_managerment.model;

import com.example.student_managerment.entities.Group;
import com.example.student_managerment.entities.Student;
import com.example.student_managerment.entities.Subject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentModel extends BaseModel {

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT s.id, s.name, s.gender, s.email, s.phone, " +
                "c.id as class_id, c.name AS class_name, " +
                "GROUP_CONCAT(sub.name SEPARATOR ', ') AS subject_names " +
                "FROM students s " +
                "LEFT JOIN class c ON s.class_id = c.id " +
                "LEFT JOIN subject_student ss ON s.id = ss.student_id " +
                "LEFT JOIN subjects sub ON ss.subject_id = sub.id " +
                "GROUP BY s.id" +
                " ORDER BY c.name ASC";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();

        List<Student> list = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int gender = rs.getInt("gender");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            int classId = rs.getInt("class_id");

            String className = rs.getString("class_name");
            if (className == null) className = "Chưa có lớp";

            String subjectNames = rs.getString("subject_names");

            Group group = new Group(classId, className);
            Student student = new Student(id, name, gender, email, phone, group);
            List<Subject> subjects = getSubjectsByStudentId(id);
            student.setSubjects(subjects);
            student.setSubjectNames(subjectNames != null ? subjectNames : "Chưa có môn học");
            list.add(student);
        }
        return list;
    }

    public Student findById(int id) throws SQLException {
        String sql = "SELECT students.*, class.name as class_name FROM students " +
                "LEFT JOIN class ON students.class_id = class.id WHERE students.id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String name = rs.getString("name");
            int gender = rs.getInt("gender");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            int classId = rs.getInt("class_id");
            String className = rs.getString("class_name");

            Group group = new Group(classId, className);
            Student s = new Student(id, name, gender, email, phone);
            s.setGroups(group);
            return s;
        }
        return null;
    }

    public void insert(Student student) throws SQLException {
        String sql = "INSERT INTO students(name, gender, email, phone) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, student.getName());
        ps.setInt(2, student.getGender());
        ps.setString(3, student.getEmail());
        ps.setString(4, student.getPhone());
        ps.execute();
    }

    public void update(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, gender = ?, email = ?, phone = ?, class_id = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, student.getName());
        ps.setInt(2, student.getGender());
        ps.setString(3, student.getEmail());
        ps.setString(4, student.getPhone());
        ps.setInt(5, student.getGroups().getId());
        ps.setInt(6, student.getId());
        ps.executeUpdate();
    }

    public void updateClass(int studentId, int classId) throws SQLException {
        String sql = "UPDATE students SET class_id = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, classId);
        ps.setInt(2, studentId);
        ps.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        String sql1 = "DELETE FROM subject_student WHERE student_id = ?";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps1.setInt(1, id);
        ps1.execute();

        String sql2 = "DELETE FROM students WHERE id = ?";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.setInt(1, id);
        ps2.execute();
    }

    public List<Group> getAllGroup() throws SQLException {
        List<Group> list = new ArrayList<>();
        String sql = "SELECT * FROM class";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            list.add(new Group(id, name));
        }
        return list;
    }

    public List<Student> searchByName(String keyword) throws SQLException {
        List<Student> result = new ArrayList<>();
        String sql = "SELECT students.*, class.name AS class_name FROM students " +
                "LEFT JOIN class ON students.class_id = class.id " +
                "WHERE students.name LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int gender = rs.getInt("gender");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            int classId = rs.getInt("class_id");
            String className = rs.getString("class_name");

            Group group = new Group(classId, className);
            Student student = new Student(id, name, gender, email, phone);
            student.setGroups(group);
            result.add(student);
        }
        return result;
    }

    public List<Subject> getAllSubjects() throws SQLException {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            list.add(new Subject(id, name));
        }
        return list;
    }

    public void assignSubjects(int studentId, String[] subjectIds) throws SQLException {
        String deleteSql = "DELETE FROM subject_student WHERE student_id = ?";
        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
        deleteStmt.setInt(1, studentId);
        deleteStmt.executeUpdate();

        String insertSql = "INSERT INTO subject_student (student_id, subject_id) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);

        for (String subjectId : subjectIds) {
            insertStmt.setInt(1, studentId);
            insertStmt.setInt(2, Integer.parseInt(subjectId));
            insertStmt.addBatch();
        }
        insertStmt.executeBatch();
    }

    public List<Subject> getSubjectsByStudentId(int studentId) throws SQLException {

        List<Subject> list = new ArrayList<>();
        String sql = "SELECT sub.id, sub.name FROM subject_student ss " +
                "JOIN subjects sub ON ss.subject_id = sub.id " +
                "WHERE ss.student_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, studentId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            list.add(new Subject(id, name));
        }
        return list;
    }

}

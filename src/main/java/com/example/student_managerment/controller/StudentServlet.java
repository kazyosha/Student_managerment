package com.example.student_managerment.controller;

import com.example.student_managerment.entities.Group;
import com.example.student_managerment.entities.Student;
import com.example.student_managerment.model.StudentModel;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "StudentServlet", urlPatterns = {"/student/*"})
public class StudentServlet extends HttpServlet {

    private StudentModel studentModel;

    @Override
    public void init() {
        studentModel = new StudentModel();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getPathInfo();
        if (uri == null) {
            uri = "";
        }
        switch (uri) {
            case "/":
            case "":
                ShowListStudent(req, resp);
                break;
            case "/add":
                showAddForm(req, resp);
                break;
            case "/edit":
                showEditForm(req, resp);
                break;
            case "/setclass":
                showAssignClassForm(req, resp);
                break;
            case "/delete":
                deleteStudent(req, resp);
                break;
            case "/search":
                searchStudent(req, resp);
                break;
            case "/assign-subject":
                showAssignSubjectForm(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getPathInfo();
        if (uri == null) {
            uri = "";
        }
        switch (uri) {
            case "/add":
                addStudent(req, resp);
                break;
            case "/edit":
                editStudent(req, resp);
                break;
            case "/setclass":
                assignClassToStudent(req, resp);
                break;
            case "/assign-subject":
                assignSubjectsToStudent(req, resp);
                break;
        }
    }

    public void ShowListStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Student> listStudent = studentModel.findAll();
            req.setAttribute("listStudent", listStudent);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/student/list.jsp");
            dispatcher.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void showAddForm(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/student/add.jsp");
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addStudent(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String name = req.getParameter("name");
            int gender = Integer.parseInt(req.getParameter("gender"));
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            Student newStudent = new Student(name, gender, email, phone);

            studentModel.insert(newStudent);

            resp.sendRedirect("/student");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showEditForm(HttpServletRequest req, HttpServletResponse resp) {

        try {
            int id = Integer.parseInt(req.getParameter("id"));


            Student studentEdit = studentModel.findById(id);
            List<Group> listGroup = studentModel.getAllGroup();

            req.setAttribute("studentEdit", studentEdit);
            req.setAttribute("listGroup", listGroup);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/student/edit.jsp");
            requestDispatcher.forward(req, resp);
        } catch (SQLException | ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editStudent(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            Student student = studentModel.findById(id);
            if (student == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String name = request.getParameter("name");
            int gender = Integer.parseInt(request.getParameter("gender"));
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String groupId = request.getParameter("class_id");
            Group group = new Group(Integer.parseInt(groupId), "");
            Student newStudent = new Student(id, name, gender, email, phone, group);

            studentModel.update(newStudent);
            response.sendRedirect("/student");

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAssignClassForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            int id = Integer.parseInt(req.getParameter("id"));

            Student student = studentModel.findById(id);
            if (student == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sinh viên.");
                return;
            }

            List<Group> listGroup = studentModel.getAllGroup();

            // Gửi dữ liệu sang JSP
            req.setAttribute("student", student);
            req.setAttribute("listGroup", listGroup);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/student/setClass.jsp");
            dispatcher.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tham số id không hợp lệ.");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi hiển thị form gán lớp", e);
        }
    }

    public void assignClassToStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Lấy dữ liệu từ form
            int studentId = Integer.parseInt(req.getParameter("id"));
            int classId = Integer.parseInt(req.getParameter("class_id"));

            // Kiểm tra sinh viên tồn tại
            Student student = studentModel.findById(studentId);
            if (student == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sinh viên.");
                return;
            }
            studentModel.updateClass(studentId, classId);
            resp.sendRedirect(req.getContextPath() + "/student");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tham số không hợp lệ.");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gán lớp cho sinh viên", e);
        }
    }

    public void deleteStudent(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");

        try {
            studentModel.delete(Integer.parseInt(id));
            response.sendRedirect("/student");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        try {
            List<Student> listStudent = studentModel.searchByName(keyword);
            req.setAttribute("listStudent", listStudent);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/student/list.jsp");
            dispatcher.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm sinh viên", e);
        }
    }

    public void showAssignSubjectForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int studentId = Integer.parseInt(req.getParameter("id"));

            Student student = studentModel.findById(studentId);
            if (student == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            req.setAttribute("student", student);
            req.setAttribute("subjectList", studentModel.getAllSubjects());

            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/student/assignSubject.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi hiển thị form phân môn học", e);
        }
    }

    public void assignSubjectsToStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int studentId = Integer.parseInt(req.getParameter("studentId"));
            String[] subjectIds = req.getParameterValues("subjectIds");

            studentModel.assignSubjects(studentId, subjectIds);
            resp.sendRedirect("/student");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi phân môn học", e);
        }
    }


}


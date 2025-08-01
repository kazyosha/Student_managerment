package com.example.student_managerment.controller;

import com.example.student_managerment.database.DBConnect;
import com.example.student_managerment.entities.Group;
import com.example.student_managerment.entities.Subject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SubjectServlet", urlPatterns = {"/subject/*"})
public class SubjectServlet extends HttpServlet {

    Connection conn = null;

    @Override
    public void init() throws ServletException {
        DBConnect dbConnect = new DBConnect();
        conn = dbConnect.getConnect();
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
               showSubjectPage(req,resp);
               break;
               case "/add":
                   showAddSubjectPage(req,resp);
                   break;
                   default:
                       resp.sendError(HttpServletResponse.SC_NOT_FOUND);
       }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getPathInfo();
        if (uri == null){
            uri = "";
        }

        switch (uri){
            case "/add":
                AddSubject(req,resp);
                break;
        }
    }

    public void showSubjectPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String sql = "SELECT * FROM subjects";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            List<Subject> listSub = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                Subject subject = new Subject(id, name);
                listSub.add(subject);
                req.setAttribute("listGroup", listSub);
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/subject/list.jsp");
            dispatcher.forward(req, resp);
        } catch (SQLException | ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showAddSubjectPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/subject/add.jsp");
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
        }
    }
    public void AddSubject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            Subject newSub = new Subject(name);

            String sql = "INSERT INTO subjects(name) VALUE (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newSub.getName());
            preparedStatement.execute();
            resp.sendRedirect("/student");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


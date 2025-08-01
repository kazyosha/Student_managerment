package com.example.student_managerment.controller;

import com.example.student_managerment.database.DBConnect;
import com.example.student_managerment.entities.Group;
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

@WebServlet(name = "GroupServlet", urlPatterns = {"/group/*"})
public class GroupServlet extends HttpServlet {
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
                showGroupPage(req, resp);
                break;
            case "/add":
                showListAdd(req, resp);
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
                addGroupPage(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void showGroupPage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String sql = "SELECT * FROM class";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            List<Group> listGroup = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                Group group = new Group(id, name);
                listGroup.add(group);
                req.setAttribute("listGroup", listGroup);
            }
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/group/list.jsp");
            dispatcher.forward(req, resp);
        } catch (SQLException | ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showListAdd(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/group/add.jsp");
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
        }
    }

    public void addGroupPage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String name = req.getParameter("name");
            Group newGroup = new Group(name);

            String sql = "INSERT INTO class(name) VALUE (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newGroup.getName());
            preparedStatement.execute();
            resp.sendRedirect("/group");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.student_managerment.model;

import com.example.student_managerment.database.DBConnect;

import java.sql.Connection;

public class BaseModel {

    protected Connection conn;

    public BaseModel() {
        DBConnect dbConnect = new DBConnect();
        conn = dbConnect.getConnect();
    }


}

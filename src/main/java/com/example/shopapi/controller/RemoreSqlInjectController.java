package com.example.shopapi.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RemoreSqlInjectController {

    @Autowired
    private DataSource dataSource;

    @PostMapping("/remoreSqlInject")
    public String remoreSqlInject(@RequestBody String sql) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }
    
}

package com.airport.db;

import com.airport.config.DBSecrets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DBSecrets.getUrl(),DBSecrets.getUsername(),DBSecrets.getPassword()
        );
    }
}
package com.airport.util;

import com.airport.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class UserSeeder {

    public static void main(String[] args) {
        String deleteSql = "DELETE FROM users";

        String insertSql = """
                INSERT INTO users (username, password_hash, role)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement deleteStmt = conn.createStatement();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {

            deleteStmt.executeUpdate(deleteSql);

            insertUser(ps, "saketh", "ram", "ADMIN");
            insertUser(ps, "spiderman", "spidey", "PASSENGER");
            insertUser(ps, "mj", "mj", "STAFF");

            System.out.println("Users inserted successfully with BCrypt hashed passwords.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertUser(PreparedStatement ps, String username, String plainPassword, String role) throws Exception {
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        ps.setString(1, username);
        ps.setString(2, hashedPassword);
        ps.setString(3, role);

        ps.executeUpdate();
    }
}
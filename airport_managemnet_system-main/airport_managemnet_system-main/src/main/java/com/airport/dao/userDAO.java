package com.airport.dao;

import com.airport.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.airport.util.*;
public class userDAO {
    public String authenticate(String username,String password){
        String sql="SELECT * FROM users WHERE username=?";
        try(Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

                ps.setString(1, username);

                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next()){
                        String storedHash = rs.getString("password_hash");
                        String role = rs.getString("role");

                        if(PasswordUtil.checkPassword(password, storedHash)){
                            return role;
                        }
                    }
                } 
        } catch(Exception e) {
                e.printStackTrace();
        }

        return null;
    }
}

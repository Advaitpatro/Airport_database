package com.airport.dao;

import com.airport.db.DBConnection;
import com.airport.model.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }

    public void addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff (full_name, role, phone, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getFullName().trim());
            ps.setString(2, staff.getRole().trim());
            setNullableString(ps, 3, staff.getPhone());
            setNullableString(ps, 4, staff.getEmail());

            ps.executeUpdate();
        }
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();

        String sql = "SELECT * FROM staff ORDER BY staff_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Staff staff = new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("email")
                );

                staffList.add(staff);
            }
        }

        return staffList;
    }

    public void updateStaff(Staff staff) throws SQLException {
        String sql = "UPDATE staff SET full_name = ?, role = ?, phone = ?, email = ? WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getFullName().trim());
            ps.setString(2, staff.getRole().trim());
            setNullableString(ps, 3, staff.getPhone());
            setNullableString(ps, 4, staff.getEmail());
            ps.setInt(5, staff.getStaffId());

            ps.executeUpdate();
        }
    }

    public void deleteStaff(int staffId) throws SQLException {
        String sql = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffId);
            ps.executeUpdate();
        }
    }
}
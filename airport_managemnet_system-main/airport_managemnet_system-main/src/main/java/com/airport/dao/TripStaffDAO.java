package com.airport.dao;

import com.airport.db.DBConnection;
import com.airport.model.TripStaff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripStaffDAO {

    public void assignStaffToTrip(TripStaff tripStaff) throws SQLException {
        String sql = "INSERT INTO trip_staff (trip_id, staff_id, assigned_role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tripStaff.getTripId());
            ps.setInt(2, tripStaff.getStaffId());

            if (tripStaff.getAssignedRole() == null || tripStaff.getAssignedRole().trim().isEmpty()) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, tripStaff.getAssignedRole().trim());
            }

            ps.executeUpdate();
        }
    }

    public List<TripStaff> getAllAssignments() throws SQLException {
        List<TripStaff> assignments = new ArrayList<>();

        String sql = """
                SELECT
                    ts.trip_id,
                    ts.staff_id,
                    f.flight_number,
                    CONCAT(t.source_airport, ' to ', t.destination_airport) AS route,
                    s.full_name AS staff_name,
                    s.role AS staff_role,
                    ts.assigned_role
                FROM trip_staff ts
                JOIN trips t ON ts.trip_id = t.trip_id
                JOIN flights f ON t.flight_id = f.flight_id
                JOIN staff s ON ts.staff_id = s.staff_id
                ORDER BY ts.trip_id, ts.staff_id
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TripStaff assignment = new TripStaff(
                        rs.getInt("trip_id"),
                        rs.getInt("staff_id"),
                        rs.getString("flight_number"),
                        rs.getString("route"),
                        rs.getString("staff_name"),
                        rs.getString("staff_role"),
                        rs.getString("assigned_role")
                );

                assignments.add(assignment);
            }
        }

        return assignments;
    }

    public void updateAssignmentRole(TripStaff tripStaff) throws SQLException {
        String sql = "UPDATE trip_staff SET assigned_role = ? WHERE trip_id = ? AND staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (tripStaff.getAssignedRole() == null || tripStaff.getAssignedRole().trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, tripStaff.getAssignedRole().trim());
            }

            ps.setInt(2, tripStaff.getTripId());
            ps.setInt(3, tripStaff.getStaffId());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Assignment not found.");
            }
        }
    }

    public void removeAssignment(int tripId, int staffId) throws SQLException {
        String sql = "DELETE FROM trip_staff WHERE trip_id = ? AND staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tripId);
            ps.setInt(2, staffId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Assignment not found.");
            }
        }
    }
}
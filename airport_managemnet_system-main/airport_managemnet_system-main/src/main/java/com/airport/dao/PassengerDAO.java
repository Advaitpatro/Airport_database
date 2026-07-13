package com.airport.dao;

import com.airport.db.DBConnection;
import com.airport.model.Passenger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDAO {

    private void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }

    public void addPassenger(Passenger passenger) throws SQLException {
        String sql = "INSERT INTO passengers (full_name, email, phone, passport_number, nationality) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passenger.getFullName());
            setNullableString(ps, 2, passenger.getEmail());
            setNullableString(ps, 3, passenger.getPhone());
            setNullableString(ps, 4, passenger.getPassportNumber());
            setNullableString(ps, 5, passenger.getNationality());

            ps.executeUpdate();
        }
    }

    public List<Passenger> getAllPassengers() throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM passengers ORDER BY passenger_id";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Passenger p = new Passenger(
                        rs.getInt("passenger_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("passport_number"),
                        rs.getString("nationality")
                );
                passengers.add(p);
            }
        }
        return passengers;
    }

    public void updatePassenger(Passenger passenger) throws SQLException {
        String sql = "UPDATE passengers SET full_name = ?, email = ?, phone = ?, passport_number = ?, nationality = ? WHERE passenger_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passenger.getFullName());
            setNullableString(ps, 2, passenger.getEmail());
            setNullableString(ps, 3, passenger.getPhone());
            setNullableString(ps, 4, passenger.getPassportNumber());
            setNullableString(ps, 5, passenger.getNationality());
            ps.setInt(6, passenger.getPassengerId());
            ps.executeUpdate();
        }
    }

    public void deletePassenger(int passengerId) throws SQLException {
        String sql = "DELETE FROM passengers WHERE passenger_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, passengerId);
            ps.executeUpdate();
        }
    }
}
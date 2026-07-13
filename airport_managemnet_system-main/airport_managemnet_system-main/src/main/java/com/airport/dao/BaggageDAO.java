package com.airport.dao;

import com.airport.db.DBConnection;
import com.airport.model.Baggage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaggageDAO {

    public void addBaggage(Baggage baggage) throws SQLException {
        if (!bookingExists(baggage.getBookingId())) {
            throw new SQLException("Booking ID does not exist.");
        }

        String sql = "INSERT INTO baggage (booking_id, weight, baggage_status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, baggage.getBookingId());
            ps.setDouble(2, baggage.getWeight());
            ps.setString(3, baggage.getBaggageStatus());

            ps.executeUpdate();
        }
    }

    public List<Baggage> getAllBaggage() throws SQLException {
        List<Baggage> baggageList = new ArrayList<>();

        String sql = """
                SELECT
                    bg.baggage_id,
                    bg.booking_id,
                    p.full_name AS passenger_name,
                    f.flight_number,
                    CONCAT(t.source_airport, ' to ', t.destination_airport) AS route,
                    bg.weight,
                    bg.baggage_status,
                    bg.created_at,
                    bg.updated_at
                FROM baggage bg
                JOIN bookings b ON bg.booking_id = b.booking_id
                JOIN passengers p ON b.passenger_id = p.passenger_id
                JOIN trips t ON b.trip_id = t.trip_id
                JOIN flights f ON t.flight_id = f.flight_id
                ORDER BY bg.baggage_id
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Baggage baggage = new Baggage(
                        rs.getInt("baggage_id"),
                        rs.getInt("booking_id"),
                        rs.getString("passenger_name"),
                        rs.getString("flight_number"),
                        rs.getString("route"),
                        rs.getDouble("weight"),
                        rs.getString("baggage_status"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );

                baggageList.add(baggage);
            }
        }

        return baggageList;
    }

    public void updateBaggage(Baggage baggage) throws SQLException {
        if (!bookingExists(baggage.getBookingId())) {
            throw new SQLException("Booking ID does not exist.");
        }

        String sql = "UPDATE baggage SET booking_id = ?, weight = ?, baggage_status = ? WHERE baggage_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, baggage.getBookingId());
            ps.setDouble(2, baggage.getWeight());
            ps.setString(3, baggage.getBaggageStatus());
            ps.setInt(4, baggage.getBaggageId());

            ps.executeUpdate();
        }
    }

    public void deleteBaggage(int baggageId) throws SQLException {
        String sql = "DELETE FROM baggage WHERE baggage_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, baggageId);
            ps.executeUpdate();
        }
    }

    private boolean bookingExists(int bookingId) throws SQLException {
        String sql = "SELECT booking_id FROM bookings WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
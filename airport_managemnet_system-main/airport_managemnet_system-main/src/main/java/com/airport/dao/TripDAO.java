package com.airport.dao;

import com.airport.db.DBConnection;
import com.airport.model.Trip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    public void addTrip(Trip trip) throws SQLException {
        String sql = "INSERT INTO trips " +
                "(flight_id, source_airport, destination_airport, departure_time, arrival_time, total_seats, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trip.getFlightId());
            ps.setString(2, trip.getSourceAirport().trim());
            ps.setString(3, trip.getDestinationAirport().trim());
            ps.setString(4, trip.getDepartureTime().trim());
            ps.setString(5, trip.getArrivalTime().trim());
            ps.setInt(6, trip.getTotalSeats());
            ps.setString(7, trip.getStatus());

            ps.executeUpdate();
        }
    }

    public List<Trip> getAllTrips() throws SQLException {
        List<Trip> trips = new ArrayList<>();

        String sql = """
                SELECT 
                    t.trip_id,
                    t.flight_id,
                    f.flight_number,
                    t.source_airport,
                    t.destination_airport,
                    t.departure_time,
                    t.arrival_time,
                    t.total_seats,
                    t.status
                FROM trips t
                JOIN flights f ON t.flight_id = f.flight_id
                ORDER BY t.trip_id
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Trip trip = new Trip(
                        rs.getInt("trip_id"),
                        rs.getInt("flight_id"),
                        rs.getString("flight_number"),
                        rs.getString("source_airport"),
                        rs.getString("destination_airport"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getInt("total_seats"),
                        rs.getString("status")
                );

                trips.add(trip);
            }
        }

        return trips;
    }

    public void updateTrip(Trip trip) throws SQLException {
        String sql = "UPDATE trips SET flight_id = ?, source_airport = ?, destination_airport = ?, " +
                "departure_time = ?, arrival_time = ?, total_seats = ?, status = ? WHERE trip_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trip.getFlightId());
            ps.setString(2, trip.getSourceAirport().trim());
            ps.setString(3, trip.getDestinationAirport().trim());
            ps.setString(4, trip.getDepartureTime().trim());
            ps.setString(5, trip.getArrivalTime().trim());
            ps.setInt(6, trip.getTotalSeats());
            ps.setString(7, trip.getStatus());
            ps.setInt(8, trip.getTripId());

            ps.executeUpdate();
        }
    }

    public void deleteTrip(int tripId) throws SQLException {
        String sql = "DELETE FROM trips WHERE trip_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tripId);
            ps.executeUpdate();
        }
    }
}
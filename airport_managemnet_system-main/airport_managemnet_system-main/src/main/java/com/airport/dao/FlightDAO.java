package com.airport.dao;

import com.airport.db.DBConnection;
import com.airport.model.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    private void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }

    public void addFlight(Flight flight) throws SQLException {
        String sql = "INSERT INTO flights (flight_number, airline_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flight.getFlightNumber().trim());
            setNullableString(ps, 2, flight.getAirlineName());

            ps.executeUpdate();
        }
    }

    public List<Flight> getAllFlights() throws SQLException {
        List<Flight> flights = new ArrayList<>();

        String sql = "SELECT * FROM flights ORDER BY flight_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("flight_number"),
                        rs.getString("airline_name")
                );

                flights.add(flight);
            }
        }

        return flights;
    }

    public void updateFlight(Flight flight) throws SQLException {
        String sql = "UPDATE flights SET flight_number = ?, airline_name = ? WHERE flight_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flight.getFlightNumber().trim());
            setNullableString(ps, 2, flight.getAirlineName());
            ps.setInt(3, flight.getFlightId());

            ps.executeUpdate();
        }
    }

    public void deleteFlight(int flightId) throws SQLException {
        String sql = "DELETE FROM flights WHERE flight_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ps.executeUpdate();
        }
    }
}
package com.airport.dao;

import com.airport.db.DBConnection;
import com.airport.model.Booking;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void createBookingWithOptionalBaggage(
            int passengerId,
            int tripId,
            String seatNumber,
            BigDecimal fare,
            BigDecimal baggageWeight
    ) throws SQLException {

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            if (!passengerExists(conn, passengerId)) {
                throw new SQLException("Passenger ID does not exist.");
            }

            TripSeatInfo tripSeatInfo = getTripSeatInfo(conn, tripId);

            if (tripSeatInfo == null) {
                throw new SQLException("Trip ID does not exist.");
            }

            if (tripSeatInfo.status.equalsIgnoreCase("Cancelled")
                    || tripSeatInfo.status.equalsIgnoreCase("Completed")) {
                throw new SQLException("Booking is not allowed for a " + tripSeatInfo.status + " trip.");
            }

            if (tripSeatInfo.confirmedBookings >= tripSeatInfo.totalSeats) {
                throw new SQLException("No seats available for this trip.");
            }

            if (seatNumber != null && !seatNumber.trim().isEmpty()) {
                if (seatAlreadyUsed(conn, tripId, seatNumber.trim())) {
                    throw new SQLException("This seat number is already used for this trip.");
                }
            }

            String bookingSql = """
                    INSERT INTO bookings 
                    (passenger_id, trip_id, seat_number, fare, booking_status)
                    VALUES (?, ?, ?, ?, 'Confirmed')
                    """;

            int bookingId;

            try (PreparedStatement ps = conn.prepareStatement(bookingSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, passengerId);
                ps.setInt(2, tripId);

                if (seatNumber == null || seatNumber.trim().isEmpty()) {
                    ps.setNull(3, Types.VARCHAR);
                } else {
                    ps.setString(3, seatNumber.trim());
                }

                ps.setBigDecimal(4, fare);

                int rows = ps.executeUpdate();

                if (rows == 0) {
                    throw new SQLException("Booking insert failed.");
                }

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        bookingId = keys.getInt(1);
                    } else {
                        throw new SQLException("Could not retrieve generated booking ID.");
                    }
                }
            }

            if (baggageWeight != null) {
                String baggageSql = """
                        INSERT INTO baggage 
                        (booking_id, weight, baggage_status)
                        VALUES (?, ?, 'Checked-In')
                        """;

                try (PreparedStatement ps = conn.prepareStatement(baggageSql)) {
                    ps.setInt(1, bookingId);
                    ps.setBigDecimal(2, baggageWeight);
                    ps.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }

            throw e;

        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        String sql = """
                SELECT 
                    b.booking_id,
                    b.passenger_id,
                    p.full_name AS passenger_name,
                    b.trip_id,
                    f.flight_number,
                    CONCAT(t.source_airport, ' to ', t.destination_airport) AS route,
                    b.seat_number,
                    b.fare,
                    b.booking_status,
                    b.booking_date,
                    COALESCE(SUM(bg.weight), 0) AS baggage_weight
                FROM bookings b
                JOIN passengers p ON b.passenger_id = p.passenger_id
                JOIN trips t ON b.trip_id = t.trip_id
                JOIN flights f ON t.flight_id = f.flight_id
                LEFT JOIN baggage bg ON b.booking_id = bg.booking_id
                GROUP BY 
                    b.booking_id,
                    b.passenger_id,
                    p.full_name,
                    b.trip_id,
                    f.flight_number,
                    t.source_airport,
                    t.destination_airport,
                    b.seat_number,
                    b.fare,
                    b.booking_status,
                    b.booking_date
                ORDER BY b.booking_id
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("passenger_id"),
                        rs.getString("passenger_name"),
                        rs.getInt("trip_id"),
                        rs.getString("flight_number"),
                        rs.getString("route"),
                        rs.getString("seat_number"),
                        rs.getDouble("fare"),
                        rs.getString("booking_status"),
                        rs.getString("booking_date"),
                        rs.getDouble("baggage_weight")
                );

                bookings.add(booking);
            }
        }

        return bookings;
    }

    public void cancelBooking(int bookingId) throws SQLException {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sql = """
                    UPDATE bookings
                    SET booking_status = 'Cancelled',
                        cancelled_at = NOW()
                    WHERE booking_id = ?
                      AND booking_status = 'Confirmed'
                    """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, bookingId);

                int rows = ps.executeUpdate();

                if (rows == 0) {
                    throw new SQLException("Booking not found or already cancelled.");
                }
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }

            throw e;

        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private boolean passengerExists(Connection conn, int passengerId) throws SQLException {
        String sql = "SELECT passenger_id FROM passengers WHERE passenger_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, passengerId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean seatAlreadyUsed(Connection conn, int tripId, String seatNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE trip_id = ? AND seat_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ps.setString(2, seatNumber);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private TripSeatInfo getTripSeatInfo(Connection conn, int tripId) throws SQLException {
        String sql = """
                SELECT 
                    t.total_seats,
                    t.status,
                    COUNT(b.booking_id) AS confirmed_bookings
                FROM trips t
                LEFT JOIN bookings b 
                    ON t.trip_id = b.trip_id 
                    AND b.booking_status = 'Confirmed'
                WHERE t.trip_id = ?
                GROUP BY t.trip_id, t.total_seats, t.status
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TripSeatInfo(
                            rs.getInt("total_seats"),
                            rs.getString("status"),
                            rs.getInt("confirmed_bookings")
                    );
                }
            }
        }

        return null;
    }

    private static class TripSeatInfo {
        int totalSeats;
        String status;
        int confirmedBookings;

        TripSeatInfo(int totalSeats, String status, int confirmedBookings) {
            this.totalSeats = totalSeats;
            this.status = status;
            this.confirmedBookings = confirmedBookings;
        }
    }
}
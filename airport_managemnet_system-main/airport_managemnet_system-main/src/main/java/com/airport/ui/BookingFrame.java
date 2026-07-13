package com.airport.ui;

import com.airport.dao.BookingDAO;
import com.airport.model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class BookingFrame extends JFrame {

    private JTextField bookingIdField;
    private JTextField passengerIdField;
    private JTextField tripIdField;
    private JTextField seatNumberField;
    private JTextField fareField;
    private JTextField baggageWeightField;

    private JTable bookingTable;
    private DefaultTableModel tableModel;

    private BookingDAO bookingDAO;

    public BookingFrame() {
        bookingDAO = new BookingDAO();

        setTitle("Booking Management");
        setSize(1150, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Booking Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        bookingIdField = new JTextField();
        bookingIdField.setEditable(false);

        passengerIdField = new JTextField();
        tripIdField = new JTextField();
        seatNumberField = new JTextField();
        fareField = new JTextField();
        baggageWeightField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Booking ID:"));
        formPanel.add(bookingIdField);

        formPanel.add(new JLabel("Passenger ID:"));
        formPanel.add(passengerIdField);

        formPanel.add(new JLabel("Trip ID:"));
        formPanel.add(tripIdField);

        formPanel.add(new JLabel("Seat Number:"));
        formPanel.add(seatNumberField);

        formPanel.add(new JLabel("Fare:"));
        formPanel.add(fareField);

        formPanel.add(new JLabel("Baggage Weight Optional:"));
        formPanel.add(baggageWeightField);

        JButton bookButton = new JButton("Create Booking");
        JButton cancelButton = new JButton("Cancel Booking");
        JButton clearButton = new JButton("Clear");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Booking ID", "Passenger ID", "Passenger Name", "Trip ID",
                        "Flight No.", "Route", "Seat", "Fare", "Status",
                        "Booking Date", "Baggage Weight"
                },
                0
        );

        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JLabel noteLabel = new JLabel("This module uses transaction + rollback: booking and baggage are saved together.");
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(noteLabel, BorderLayout.SOUTH);

        loadBookings();

        bookButton.addActionListener(e -> createBooking());
        cancelButton.addActionListener(e -> cancelBooking());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadBookings());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });

        bookingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookingTable.getSelectedRow() != -1) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void loadBookings() {
        try {
            tableModel.setRowCount(0);

            List<Booking> bookings = bookingDAO.getAllBookings();

            for (Booking b : bookings) {
                tableModel.addRow(new Object[]{
                        b.getBookingId(),
                        b.getPassengerId(),
                        b.getPassengerName(),
                        b.getTripId(),
                        b.getFlightNumber(),
                        b.getRoute(),
                        b.getSeatNumber(),
                        b.getFare(),
                        b.getBookingStatus(),
                        b.getBookingDate(),
                        b.getBaggageWeight()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }

    private void createBooking() {
        try {
            if (!validateBookingFields()) {
                return;
            }

            int passengerId = Integer.parseInt(passengerIdField.getText().trim());
            int tripId = Integer.parseInt(tripIdField.getText().trim());
            String seatNumber = seatNumberField.getText().trim();
            BigDecimal fare = new BigDecimal(fareField.getText().trim());

            BigDecimal baggageWeight = null;

            if (!baggageWeightField.getText().trim().isEmpty()) {
                baggageWeight = new BigDecimal(baggageWeightField.getText().trim());
            }

            bookingDAO.createBookingWithOptionalBaggage(
                    passengerId,
                    tripId,
                    seatNumber,
                    fare,
                    baggageWeight
            );

            JOptionPane.showMessageDialog(this, "Booking created successfully.");
            loadBookings();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Booking failed: " + e.getMessage() +
                            "\n\nTransaction was rolled back. No partial data was saved."
            );
        }
    }

    private void cancelBooking() {
        try {
            if (bookingIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a booking to cancel.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to cancel this booking?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int bookingId = Integer.parseInt(bookingIdField.getText().trim());

                bookingDAO.cancelBooking(bookingId);

                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
                loadBookings();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cancelling booking: " + e.getMessage()
            );
        }
    }

    private boolean validateBookingFields() {
        if (passengerIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Passenger ID is required.");
            return false;
        }

        if (tripIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Trip ID is required.");
            return false;
        }

        if (fareField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fare is required.");
            return false;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdField.getText().trim());
            int tripId = Integer.parseInt(tripIdField.getText().trim());
            BigDecimal fare = new BigDecimal(fareField.getText().trim());

            if (passengerId <= 0) {
                JOptionPane.showMessageDialog(this, "Passenger ID must be positive.");
                return false;
            }

            if (tripId <= 0) {
                JOptionPane.showMessageDialog(this, "Trip ID must be positive.");
                return false;
            }

            if (fare.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Fare cannot be negative.");
                return false;
            }

            if (!baggageWeightField.getText().trim().isEmpty()) {
                BigDecimal baggageWeight = new BigDecimal(baggageWeightField.getText().trim());

                if (baggageWeight.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Baggage weight cannot be negative.");
                    return false;
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Passenger ID, Trip ID, Fare, and Baggage Weight must be valid numbers.");
            return false;
        }

        return true;
    }

    private void fillFieldsFromSelectedRow() {
        int row = bookingTable.getSelectedRow();

        bookingIdField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        passengerIdField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        tripIdField.setText(String.valueOf(tableModel.getValueAt(row, 3)));

        Object seat = tableModel.getValueAt(row, 6);
        Object fare = tableModel.getValueAt(row, 7);
        Object baggageWeight = tableModel.getValueAt(row, 10);

        seatNumberField.setText(seat == null ? "" : seat.toString());
        fareField.setText(fare == null ? "" : fare.toString());
        baggageWeightField.setText(baggageWeight == null ? "" : baggageWeight.toString());
    }

    private void clearFields() {
        bookingIdField.setText("");
        passengerIdField.setText("");
        tripIdField.setText("");
        seatNumberField.setText("");
        fareField.setText("");
        baggageWeightField.setText("");
        bookingTable.clearSelection();
    }
}
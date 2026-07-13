package com.airport.ui;

import com.airport.dao.TripDAO;
import com.airport.model.Trip;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TripFrame extends JFrame {

    private JTextField tripIdField;
    private JTextField flightIdField;
    private JTextField sourceField;
    private JTextField destinationField;
    private JTextField departureField;
    private JTextField arrivalField;
    private JTextField totalSeatsField;
    private JComboBox<String> statusBox;

    private JTable tripTable;
    private DefaultTableModel tableModel;

    private TripDAO tripDAO;

    public TripFrame() {
        tripDAO = new TripDAO();

        setTitle("Trip Management");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Trip Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        tripIdField = new JTextField();
        tripIdField.setEditable(false);

        flightIdField = new JTextField();
        sourceField = new JTextField();
        destinationField = new JTextField();
        departureField = new JTextField();
        arrivalField = new JTextField();
        totalSeatsField = new JTextField();

        statusBox = new JComboBox<>(new String[]{
                "Scheduled", "Boarding", "Delayed", "Cancelled", "Completed"
        });

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Trip ID:"));
        formPanel.add(tripIdField);

        formPanel.add(new JLabel("Flight ID:"));
        formPanel.add(flightIdField);

        formPanel.add(new JLabel("Source Airport:"));
        formPanel.add(sourceField);

        formPanel.add(new JLabel("Destination Airport:"));
        formPanel.add(destinationField);

        formPanel.add(new JLabel("Departure Time:"));
        formPanel.add(departureField);

        formPanel.add(new JLabel("Arrival Time:"));
        formPanel.add(arrivalField);

        formPanel.add(new JLabel("Total Seats:"));
        formPanel.add(totalSeatsField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusBox);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Trip ID", "Flight ID", "Flight No.", "Source", "Destination",
                        "Departure", "Arrival", "Seats", "Status"
                },
                0
        );

        tripTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tripTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JLabel noteLabel = new JLabel("Date-time format: YYYY-MM-DD HH:MM:SS    Example: 2026-07-01 09:00:00");
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(noteLabel, BorderLayout.SOUTH);

        loadTrips();

        addButton.addActionListener(e -> addTrip());
        updateButton.addActionListener(e -> updateTrip());
        deleteButton.addActionListener(e -> deleteTrip());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadTrips());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });

        tripTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tripTable.getSelectedRow() != -1) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void loadTrips() {
        try {
            tableModel.setRowCount(0);

            List<Trip> trips = tripDAO.getAllTrips();

            for (Trip t : trips) {
                tableModel.addRow(new Object[]{
                        t.getTripId(),
                        t.getFlightId(),
                        t.getFlightNumber(),
                        t.getSourceAirport(),
                        t.getDestinationAirport(),
                        t.getDepartureTime(),
                        t.getArrivalTime(),
                        t.getTotalSeats(),
                        t.getStatus()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading trips: " + e.getMessage());
        }
    }

    private void addTrip() {
        try {
            if (!validateFields()) {
                return;
            }

            Trip trip = new Trip(
                    Integer.parseInt(flightIdField.getText().trim()),
                    sourceField.getText(),
                    destinationField.getText(),
                    departureField.getText(),
                    arrivalField.getText(),
                    Integer.parseInt(totalSeatsField.getText().trim()),
                    statusBox.getSelectedItem().toString()
            );

            tripDAO.addTrip(trip);

            JOptionPane.showMessageDialog(this, "Trip added successfully.");
            loadTrips();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding trip: " + e.getMessage() +
                            "\n\nCheck that Flight ID exists and date-time format is correct.");
        }
    }

    private void updateTrip() {
        try {
            if (tripIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a trip to update.");
                return;
            }

            if (!validateFields()) {
                return;
            }

            Trip trip = new Trip(
                    Integer.parseInt(tripIdField.getText().trim()),
                    Integer.parseInt(flightIdField.getText().trim()),
                    sourceField.getText(),
                    destinationField.getText(),
                    departureField.getText(),
                    arrivalField.getText(),
                    Integer.parseInt(totalSeatsField.getText().trim()),
                    statusBox.getSelectedItem().toString()
            );

            tripDAO.updateTrip(trip);

            JOptionPane.showMessageDialog(this, "Trip updated successfully.");
            loadTrips();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating trip: " + e.getMessage() +
                            "\n\nCheck that Flight ID exists, arrival is after departure, and airports are different.");
        }
    }

    private void deleteTrip() {
        try {
            if (tripIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a trip to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this trip?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int tripId = Integer.parseInt(tripIdField.getText().trim());
                tripDAO.deleteTrip(tripId);

                JOptionPane.showMessageDialog(this, "Trip deleted successfully.");
                loadTrips();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting trip: " + e.getMessage() +
                            "\n\nNote: A trip cannot be deleted if bookings already exist for it.");
        }
    }

    private boolean validateFields() {
        if (flightIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Flight ID is required.");
            return false;
        }

        if (sourceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Source airport is required.");
            return false;
        }

        if (destinationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Destination airport is required.");
            return false;
        }

        if (departureField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Departure time is required.");
            return false;
        }

        if (arrivalField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Arrival time is required.");
            return false;
        }

        if (totalSeatsField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total seats is required.");
            return false;
        }

        try {
            Integer.parseInt(flightIdField.getText().trim());
            int totalSeats = Integer.parseInt(totalSeatsField.getText().trim());

            if (totalSeats <= 0) {
                JOptionPane.showMessageDialog(this, "Total seats must be greater than 0.");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Flight ID and Total Seats must be valid numbers.");
            return false;
        }

        if (sourceField.getText().trim().equalsIgnoreCase(destinationField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Source and destination airports cannot be the same.");
            return false;
        }

        return true;
    }

    private void fillFieldsFromSelectedRow() {
        int row = tripTable.getSelectedRow();

        tripIdField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        flightIdField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        sourceField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        destinationField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        departureField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        arrivalField.setText(String.valueOf(tableModel.getValueAt(row, 6)));
        totalSeatsField.setText(String.valueOf(tableModel.getValueAt(row, 7)));
        statusBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 8)));
    }

    private void clearFields() {
        tripIdField.setText("");
        flightIdField.setText("");
        sourceField.setText("");
        destinationField.setText("");
        departureField.setText("");
        arrivalField.setText("");
        totalSeatsField.setText("");
        statusBox.setSelectedItem("Scheduled");
        tripTable.clearSelection();
    }
}
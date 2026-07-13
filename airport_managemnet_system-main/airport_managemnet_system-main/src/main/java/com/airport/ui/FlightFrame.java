package com.airport.ui;

import com.airport.dao.FlightDAO;
import com.airport.model.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FlightFrame extends JFrame {

    private JTextField idField;
    private JTextField flightNumberField;
    private JTextField airlineNameField;

    private JTable flightTable;
    private DefaultTableModel tableModel;

    private FlightDAO flightDAO;

    public FlightFrame() {
        flightDAO = new FlightDAO();

        setTitle("Flight Management");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Flight Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        idField = new JTextField();
        idField.setEditable(false);

        flightNumberField = new JTextField();
        airlineNameField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Flight ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Flight Number:"));
        formPanel.add(flightNumberField);

        formPanel.add(new JLabel("Airline Name:"));
        formPanel.add(airlineNameField);

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
                new Object[]{"ID", "Flight Number", "Airline Name"},
                0
        );

        flightTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(flightTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadFlights();

        addButton.addActionListener(e -> addFlight());
        updateButton.addActionListener(e -> updateFlight());
        deleteButton.addActionListener(e -> deleteFlight());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadFlights());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });

        flightTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && flightTable.getSelectedRow() != -1) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void loadFlights() {
        try {
            tableModel.setRowCount(0);

            List<Flight> flights = flightDAO.getAllFlights();

            for (Flight f : flights) {
                tableModel.addRow(new Object[]{
                        f.getFlightId(),
                        f.getFlightNumber(),
                        f.getAirlineName()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading flights: " + e.getMessage());
        }
    }

    private void addFlight() {
        try {
            String flightNumber = flightNumberField.getText().trim();

            if (flightNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Flight number is required.");
                return;
            }

            Flight flight = new Flight(
                    flightNumber,
                    airlineNameField.getText()
            );

            flightDAO.addFlight(flight);

            JOptionPane.showMessageDialog(this, "Flight added successfully.");
            loadFlights();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding flight: " + e.getMessage());
        }
    }

    private void updateFlight() {
        try {
            if (idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a flight to update.");
                return;
            }

            String flightNumber = flightNumberField.getText().trim();

            if (flightNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Flight number is required.");
                return;
            }

            int flightId = Integer.parseInt(idField.getText());

            Flight flight = new Flight(
                    flightId,
                    flightNumber,
                    airlineNameField.getText()
            );

            flightDAO.updateFlight(flight);

            JOptionPane.showMessageDialog(this, "Flight updated successfully.");
            loadFlights();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating flight: " + e.getMessage());
        }
    }

    private void deleteFlight() {
        try {
            if (idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a flight to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this flight?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int flightId = Integer.parseInt(idField.getText());
                flightDAO.deleteFlight(flightId);

                JOptionPane.showMessageDialog(this, "Flight deleted successfully.");
                loadFlights();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting flight: " + e.getMessage() +
                            "\n\nNote: A flight cannot be deleted if it is already used by a trip."
            );
        }
    }

    private void fillFieldsFromSelectedRow() {
        int row = flightTable.getSelectedRow();

        idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        flightNumberField.setText(String.valueOf(tableModel.getValueAt(row, 1)));

        Object airlineName = tableModel.getValueAt(row, 2);
        airlineNameField.setText(airlineName == null ? "" : airlineName.toString());
    }

    private void clearFields() {
        idField.setText("");
        flightNumberField.setText("");
        airlineNameField.setText("");
        flightTable.clearSelection();
    }
}
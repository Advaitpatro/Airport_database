package com.airport.ui;

import com.airport.dao.BaggageDAO;
import com.airport.model.Baggage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BaggageFrame extends JFrame {

    private JTextField baggageIdField;
    private JTextField bookingIdField;
    private JTextField weightField;
    private JComboBox<String> statusBox;

    private JTable baggageTable;
    private DefaultTableModel tableModel;

    private BaggageDAO baggageDAO;

    public BaggageFrame() {
        baggageDAO = new BaggageDAO();

        setTitle("Baggage Management");
        setSize(1100, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Baggage Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        baggageIdField = new JTextField();
        baggageIdField.setEditable(false);

        bookingIdField = new JTextField();
        weightField = new JTextField();

        statusBox = new JComboBox<>(new String[]{
                "Checked-In", "Loaded", "Lost", "Delivered"
        });

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Baggage ID:"));
        formPanel.add(baggageIdField);

        formPanel.add(new JLabel("Booking ID:"));
        formPanel.add(bookingIdField);

        formPanel.add(new JLabel("Weight:"));
        formPanel.add(weightField);

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
                        "Baggage ID", "Booking ID", "Passenger Name", "Flight No.",
                        "Route", "Weight", "Status", "Created At", "Updated At"
                },
                0
        );

        baggageTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(baggageTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JLabel noteLabel = new JLabel("Use an existing Booking ID from the Booking Management screen.");
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(noteLabel, BorderLayout.SOUTH);

        loadBaggage();

        addButton.addActionListener(e -> addBaggage());
        updateButton.addActionListener(e -> updateBaggage());
        deleteButton.addActionListener(e -> deleteBaggage());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadBaggage());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });

        baggageTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && baggageTable.getSelectedRow() != -1) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void loadBaggage() {
        try {
            tableModel.setRowCount(0);

            List<Baggage> baggageList = baggageDAO.getAllBaggage();

            for (Baggage b : baggageList) {
                tableModel.addRow(new Object[]{
                        b.getBaggageId(),
                        b.getBookingId(),
                        b.getPassengerName(),
                        b.getFlightNumber(),
                        b.getRoute(),
                        b.getWeight(),
                        b.getBaggageStatus(),
                        b.getCreatedAt(),
                        b.getUpdatedAt()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading baggage: " + e.getMessage());
        }
    }

    private void addBaggage() {
        try {
            if (!validateFields()) {
                return;
            }

            Baggage baggage = new Baggage(
                    Integer.parseInt(bookingIdField.getText().trim()),
                    Double.parseDouble(weightField.getText().trim()),
                    statusBox.getSelectedItem().toString()
            );

            baggageDAO.addBaggage(baggage);

            JOptionPane.showMessageDialog(this, "Baggage added successfully.");
            loadBaggage();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding baggage: " + e.getMessage());
        }
    }

    private void updateBaggage() {
        try {
            if (baggageIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a baggage record to update.");
                return;
            }

            if (!validateFields()) {
                return;
            }

            Baggage baggage = new Baggage(
                    Integer.parseInt(baggageIdField.getText().trim()),
                    Integer.parseInt(bookingIdField.getText().trim()),
                    Double.parseDouble(weightField.getText().trim()),
                    statusBox.getSelectedItem().toString()
            );

            baggageDAO.updateBaggage(baggage);

            JOptionPane.showMessageDialog(this, "Baggage updated successfully.");
            loadBaggage();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating baggage: " + e.getMessage());
        }
    }

    private void deleteBaggage() {
        try {
            if (baggageIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a baggage record to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this baggage record?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int baggageId = Integer.parseInt(baggageIdField.getText().trim());

                baggageDAO.deleteBaggage(baggageId);

                JOptionPane.showMessageDialog(this, "Baggage deleted successfully.");
                loadBaggage();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting baggage: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (bookingIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Booking ID is required.");
            return false;
        }

        if (weightField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Weight is required.");
            return false;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());

            if (bookingId <= 0) {
                JOptionPane.showMessageDialog(this, "Booking ID must be positive.");
                return false;
            }

            if (weight < 0) {
                JOptionPane.showMessageDialog(this, "Weight cannot be negative.");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Booking ID and Weight must be valid numbers.");
            return false;
        }

        return true;
    }

    private void fillFieldsFromSelectedRow() {
        int row = baggageTable.getSelectedRow();

        baggageIdField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        bookingIdField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        weightField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        statusBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 6)));
    }

    private void clearFields() {
        baggageIdField.setText("");
        bookingIdField.setText("");
        weightField.setText("");
        statusBox.setSelectedItem("Checked-In");
        baggageTable.clearSelection();
    }
}
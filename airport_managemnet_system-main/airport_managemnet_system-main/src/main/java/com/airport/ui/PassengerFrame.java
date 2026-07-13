package com.airport.ui;

import com.airport.dao.PassengerDAO;
import com.airport.model.Passenger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PassengerFrame extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField passportField;
    private JTextField nationalityField;

    private JTable passengerTable;
    private DefaultTableModel tableModel;

    private PassengerDAO passengerDAO;

    public PassengerFrame() {
        passengerDAO = new PassengerDAO();

        setTitle("Passenger Management");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Passenger Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        idField = new JTextField();
        idField.setEditable(false);

        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        passportField = new JTextField();
        nationalityField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Passenger ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Passport Number:"));
        formPanel.add(passportField);

        formPanel.add(new JLabel("Nationality:"));
        formPanel.add(nationalityField);

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
                new Object[]{"ID", "Full Name", "Email", "Phone", "Passport", "Nationality"},
                0
        );

        passengerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(passengerTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadPassengers();

        addButton.addActionListener(e -> addPassenger());
        updateButton.addActionListener(e -> updatePassenger());
        deleteButton.addActionListener(e -> deletePassenger());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadPassengers());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });

        passengerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && passengerTable.getSelectedRow() != -1) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void loadPassengers() {
        try {
            tableModel.setRowCount(0);

            List<Passenger> passengers = passengerDAO.getAllPassengers();

            for (Passenger p : passengers) {
                tableModel.addRow(new Object[]{
                        p.getPassengerId(),
                        p.getFullName(),
                        p.getEmail(),
                        p.getPhone(),
                        p.getPassportNumber(),
                        p.getNationality()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading passengers: " + e.getMessage());
        }
    }

    private void addPassenger() {
        try {
            String fullName = nameField.getText().trim();

            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Full name is required.");
                return;
            }

            Passenger passenger = new Passenger(
                    fullName,
                    emailField.getText(),
                    phoneField.getText(),
                    passportField.getText(),
                    nationalityField.getText()
            );

            passengerDAO.addPassenger(passenger);

            JOptionPane.showMessageDialog(this, "Passenger added successfully.");
            loadPassengers();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding passenger: " + e.getMessage());
        }
    }

    private void updatePassenger() {
        try {
            if (idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a passenger to update.");
                return;
            }

            String fullName = nameField.getText().trim();

            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Full name is required.");
                return;
            }

            int passengerId = Integer.parseInt(idField.getText());

            Passenger passenger = new Passenger(
                    passengerId,
                    fullName,
                    emailField.getText(),
                    phoneField.getText(),
                    passportField.getText(),
                    nationalityField.getText()
            );

            passengerDAO.updatePassenger(passenger);

            JOptionPane.showMessageDialog(this, "Passenger updated successfully.");
            loadPassengers();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating passenger: " + e.getMessage());
        }
    }

    private void deletePassenger() {
        try {
            if (idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a passenger to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this passenger?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int passengerId = Integer.parseInt(idField.getText());
                passengerDAO.deletePassenger(passengerId);

                JOptionPane.showMessageDialog(this, "Passenger deleted successfully.");
                loadPassengers();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting passenger: " + e.getMessage());
        }
    }

    private void fillFieldsFromSelectedRow() {
        int row = passengerTable.getSelectedRow();

        idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));

        Object email = tableModel.getValueAt(row, 2);
        Object phone = tableModel.getValueAt(row, 3);
        Object passport = tableModel.getValueAt(row, 4);
        Object nationality = tableModel.getValueAt(row, 5);

        emailField.setText(email == null ? "" : email.toString());
        phoneField.setText(phone == null ? "" : phone.toString());
        passportField.setText(passport == null ? "" : passport.toString());
        nationalityField.setText(nationality == null ? "" : nationality.toString());
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passportField.setText("");
        nationalityField.setText("");
        passengerTable.clearSelection();
    }
}
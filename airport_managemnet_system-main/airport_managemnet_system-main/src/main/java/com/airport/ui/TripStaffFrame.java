package com.airport.ui;

import com.airport.dao.TripStaffDAO;
import com.airport.model.TripStaff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TripStaffFrame extends JFrame {

    private JTextField tripIdField;
    private JTextField staffIdField;
    private JTextField assignedRoleField;

    private JTable assignmentTable;
    private DefaultTableModel tableModel;

    private TripStaffDAO tripStaffDAO;

    public TripStaffFrame() {
        tripStaffDAO = new TripStaffDAO();

        setTitle("Trip Staff Assignment");
        setSize(1050, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Trip Staff Assignment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        tripIdField = new JTextField();
        staffIdField = new JTextField();
        assignedRoleField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Trip ID:"));
        formPanel.add(tripIdField);

        formPanel.add(new JLabel("Staff ID:"));
        formPanel.add(staffIdField);

        formPanel.add(new JLabel("Assigned Role:"));
        formPanel.add(assignedRoleField);

        JButton assignButton = new JButton("Assign");
        JButton updateButton = new JButton("Update Role");
        JButton removeButton = new JButton("Remove");
        JButton clearButton = new JButton("Clear");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        buttonPanel.add(assignButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Trip ID", "Staff ID", "Flight No.", "Route",
                        "Staff Name", "Staff Role", "Assigned Role"
                },
                0
        );

        assignmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(assignmentTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JLabel noteLabel = new JLabel("Use existing Trip ID and Staff ID. One staff member can be assigned to many trips.");
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(noteLabel, BorderLayout.SOUTH);

        loadAssignments();

        assignButton.addActionListener(e -> assignStaff());
        updateButton.addActionListener(e -> updateAssignment());
        removeButton.addActionListener(e -> removeAssignment());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadAssignments());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });

        assignmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && assignmentTable.getSelectedRow() != -1) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void loadAssignments() {
        try {
            tableModel.setRowCount(0);

            List<TripStaff> assignments = tripStaffDAO.getAllAssignments();

            for (TripStaff ts : assignments) {
                tableModel.addRow(new Object[]{
                        ts.getTripId(),
                        ts.getStaffId(),
                        ts.getFlightNumber(),
                        ts.getRoute(),
                        ts.getStaffName(),
                        ts.getStaffRole(),
                        ts.getAssignedRole()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading assignments: " + e.getMessage());
        }
    }

    private void assignStaff() {
        try {
            if (!validateFields()) {
                return;
            }

            TripStaff tripStaff = new TripStaff(
                    Integer.parseInt(tripIdField.getText().trim()),
                    Integer.parseInt(staffIdField.getText().trim()),
                    assignedRoleField.getText()
            );

            tripStaffDAO.assignStaffToTrip(tripStaff);

            JOptionPane.showMessageDialog(this, "Staff assigned to trip successfully.");
            loadAssignments();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error assigning staff: " + e.getMessage() +
                            "\n\nCheck that Trip ID and Staff ID exist. Also, the same staff cannot be assigned twice to the same trip."
            );
        }
    }

    private void updateAssignment() {
        try {
            if (!validateFields()) {
                return;
            }

            TripStaff tripStaff = new TripStaff(
                    Integer.parseInt(tripIdField.getText().trim()),
                    Integer.parseInt(staffIdField.getText().trim()),
                    assignedRoleField.getText()
            );

            tripStaffDAO.updateAssignmentRole(tripStaff);

            JOptionPane.showMessageDialog(this, "Assigned role updated successfully.");
            loadAssignments();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating assignment: " + e.getMessage());
        }
    }

    private void removeAssignment() {
        try {
            if (!validateFields()) {
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this staff assignment?",
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int tripId = Integer.parseInt(tripIdField.getText().trim());
                int staffId = Integer.parseInt(staffIdField.getText().trim());

                tripStaffDAO.removeAssignment(tripId, staffId);

                JOptionPane.showMessageDialog(this, "Assignment removed successfully.");
                loadAssignments();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error removing assignment: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (tripIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Trip ID is required.");
            return false;
        }

        if (staffIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Staff ID is required.");
            return false;
        }

        try {
            int tripId = Integer.parseInt(tripIdField.getText().trim());
            int staffId = Integer.parseInt(staffIdField.getText().trim());

            if (tripId <= 0) {
                JOptionPane.showMessageDialog(this, "Trip ID must be positive.");
                return false;
            }

            if (staffId <= 0) {
                JOptionPane.showMessageDialog(this, "Staff ID must be positive.");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Trip ID and Staff ID must be valid numbers.");
            return false;
        }

        return true;
    }

    private void fillFieldsFromSelectedRow() {
        int row = assignmentTable.getSelectedRow();

        tripIdField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        staffIdField.setText(String.valueOf(tableModel.getValueAt(row, 1)));

        Object assignedRole = tableModel.getValueAt(row, 6);
        assignedRoleField.setText(assignedRole == null ? "" : assignedRole.toString());
    }

    private void clearFields() {
        tripIdField.setText("");
        staffIdField.setText("");
        assignedRoleField.setText("");
        assignmentTable.clearSelection();
    }
}
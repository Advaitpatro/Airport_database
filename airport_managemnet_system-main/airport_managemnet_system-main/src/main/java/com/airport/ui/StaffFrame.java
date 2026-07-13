package com.airport.ui;

import com.airport.dao.StaffDAO;
import com.airport.model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffFrame extends JFrame {

    private JTextField staffIdField;
    private JTextField nameField;
    private JTextField roleField;
    private JTextField phoneField;
    private JTextField emailField;

    private JTable staffTable;
    private DefaultTableModel tableModel;

    private StaffDAO staffDAO;

    public StaffFrame() {
        staffDAO = new StaffDAO();

        setTitle("Staff Management");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Staff Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        staffIdField = new JTextField();
        staffIdField.setEditable(false);

        nameField = new JTextField();
        roleField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Staff ID:"));
        formPanel.add(staffIdField);

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleField);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

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
                new Object[]{"Staff ID", "Full Name", "Role", "Phone", "Email"},
                0
        );

        staffTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(staffTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadStaff();

        addButton.addActionListener(e -> addStaff());
        updateButton.addActionListener(e -> updateStaff());
        deleteButton.addActionListener(e -> deleteStaff());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadStaff());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });

        staffTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && staffTable.getSelectedRow() != -1) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void loadStaff() {
        try {
            tableModel.setRowCount(0);

            List<Staff> staffList = staffDAO.getAllStaff();

            for (Staff s : staffList) {
                tableModel.addRow(new Object[]{
                        s.getStaffId(),
                        s.getFullName(),
                        s.getRole(),
                        s.getPhone(),
                        s.getEmail()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading staff: " + e.getMessage());
        }
    }

    private void addStaff() {
        try {
            if (!validateFields()) {
                return;
            }

            Staff staff = new Staff(
                    nameField.getText(),
                    roleField.getText(),
                    phoneField.getText(),
                    emailField.getText()
            );

            staffDAO.addStaff(staff);

            JOptionPane.showMessageDialog(this, "Staff added successfully.");
            loadStaff();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding staff: " + e.getMessage());
        }
    }

    private void updateStaff() {
        try {
            if (staffIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a staff record to update.");
                return;
            }

            if (!validateFields()) {
                return;
            }

            Staff staff = new Staff(
                    Integer.parseInt(staffIdField.getText().trim()),
                    nameField.getText(),
                    roleField.getText(),
                    phoneField.getText(),
                    emailField.getText()
            );

            staffDAO.updateStaff(staff);

            JOptionPane.showMessageDialog(this, "Staff updated successfully.");
            loadStaff();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating staff: " + e.getMessage());
        }
    }

    private void deleteStaff() {
        try {
            if (staffIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a staff record to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this staff record?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int staffId = Integer.parseInt(staffIdField.getText().trim());

                staffDAO.deleteStaff(staffId);

                JOptionPane.showMessageDialog(this, "Staff deleted successfully.");
                loadStaff();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting staff: " + e.getMessage() +
                            "\n\nNote: Staff cannot be deleted if assigned to a trip."
            );
        }
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name is required.");
            return false;
        }

        if (roleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Role is required.");
            return false;
        }

        return true;
    }

    private void fillFieldsFromSelectedRow() {
        int row = staffTable.getSelectedRow();

        staffIdField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        roleField.setText(String.valueOf(tableModel.getValueAt(row, 2)));

        Object phone = tableModel.getValueAt(row, 3);
        Object email = tableModel.getValueAt(row, 4);

        phoneField.setText(phone == null ? "" : phone.toString());
        emailField.setText(email == null ? "" : email.toString());
    }

    private void clearFields() {
        staffIdField.setText("");
        nameField.setText("");
        roleField.setText("");
        phoneField.setText("");
        emailField.setText("");
        staffTable.clearSelection();
    }
}
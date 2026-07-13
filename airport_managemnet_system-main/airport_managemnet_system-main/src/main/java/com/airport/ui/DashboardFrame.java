package com.airport.ui;

import javax.swing.*;

import com.airport.util.Session;

import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Airport Management System - Dashboard");
        setSize(550, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Airport Management Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton passengerButton = new JButton("Manage Passengers");
        JButton flightButton = new JButton("Manage Flights");
        JButton tripButton = new JButton("Manage Trips");
        JButton bookingButton = new JButton("Manage Bookings");
        JButton baggageButton = new JButton("Manage Baggage");
        JButton staffButton = new JButton("Manage Staff");
        JButton tripStaffButton = new JButton("Assign Staff to Trips");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        buttonPanel.add(passengerButton);
        buttonPanel.add(flightButton);
        buttonPanel.add(tripButton);
        buttonPanel.add(bookingButton);
        buttonPanel.add(baggageButton);
        buttonPanel.add(staffButton);
        buttonPanel.add(tripStaffButton);
        buttonPanel.add(logoutButton);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        applyRolePermissions(passengerButton, flightButton, tripButton, bookingButton, baggageButton, staffButton, tripStaffButton);

        passengerButton.addActionListener(e -> {
            dispose();
            new PassengerFrame().setVisible(true);
        });

        flightButton.addActionListener(e -> {
                dispose();
                new FlightFrame().setVisible(true);
        });

        tripButton.addActionListener(e -> {
                dispose();
                new TripFrame().setVisible(true);
        });

        bookingButton.addActionListener(e -> {
                dispose();
                new BookingFrame().setVisible(true);
        });

        baggageButton.addActionListener(e -> {
                dispose();
                new BaggageFrame().setVisible(true);
        });

        staffButton.addActionListener(e -> {
                dispose();
                new StaffFrame().setVisible(true);
        });

        tripStaffButton.addActionListener(e -> {
            dispose();
            new TripStaffFrame().setVisible(true);
        });

        logoutButton.addActionListener(e -> {
                Session.logout();
                dispose();
                new LoginFrame().setVisible(true);
        });
    }

    private void applyRolePermissions(JButton passengerButton,
                                        JButton flightButton,
                                        JButton tripButton, 
                                        JButton bookingButton,
                                        JButton baggageButton,
                                        JButton staffButton,
                                        JButton tripStaffButton) {
        
        if(Session.isAdmin()){
                return;
        }

        if(Session.isStaff()){
                staffButton.setEnabled(false);
                tripStaffButton.setEnabled(false);
                return;
        }

        if(Session.isPassenger()){
                flightButton.setEnabled(false);
                tripButton.setEnabled(false);
                baggageButton.setEnabled(false);
                staffButton.setEnabled(false);
                tripStaffButton.setEnabled(false);  
                return;
        }
    }
}
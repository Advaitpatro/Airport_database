package com.airport.model;

public class TripStaff {
    private int tripId;
    private int staffId;
    private String flightNumber;
    private String route;
    private String staffName;
    private String staffRole;
    private String assignedRole;

    public TripStaff(int tripId, int staffId, String assignedRole) {
        this.tripId = tripId;
        this.staffId = staffId;
        this.assignedRole = assignedRole;
    }

    public TripStaff(int tripId, int staffId, String flightNumber, String route,
                     String staffName, String staffRole, String assignedRole) {
        this.tripId = tripId;
        this.staffId = staffId;
        this.flightNumber = flightNumber;
        this.route = route;
        this.staffName = staffName;
        this.staffRole = staffRole;
        this.assignedRole = assignedRole;
    }

    public int getTripId() {
        return tripId;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getRoute() {
        return route;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public String getAssignedRole() {
        return assignedRole;
    }
}
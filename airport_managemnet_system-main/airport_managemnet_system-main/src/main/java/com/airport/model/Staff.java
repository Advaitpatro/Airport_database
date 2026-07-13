package com.airport.model;

public class Staff {
    private int staffId;
    private String fullName;
    private String role;
    private String phone;
    private String email;

    public Staff() {
    }

    public Staff(int staffId, String fullName, String role, String phone, String email) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.email = email;
    }

    public Staff(String fullName, String role, String phone, String email) {
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.email = email;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
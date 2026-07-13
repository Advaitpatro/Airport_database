package com.airport.model;

public class Passenger{
    private int passengerId;
    private String fullName;
    private String email;
    private String phone;
    private String passportNumber;
    private String nationality;

    public Passenger(){}


    public Passenger(int passengerId, String fullName, String email, String phone, String passportNumber, String nationality) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
    }

    public Passenger(String fullName, String email, String phone, String passportNumber, String nationality) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getNationality() {
        return nationality;
    }
}


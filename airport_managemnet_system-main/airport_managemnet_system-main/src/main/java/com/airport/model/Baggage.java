package com.airport.model;

public class Baggage {
    private int baggageId;
    private int bookingId;
    private String passengerName;
    private String flightNumber;
    private String route;
    private double weight;
    private String baggageStatus;
    private String createdAt;
    private String updatedAt;

    public Baggage(int baggageId, int bookingId, String passengerName, String flightNumber,
                   String route, double weight, String baggageStatus, String createdAt, String updatedAt) {
        this.baggageId = baggageId;
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.flightNumber = flightNumber;
        this.route = route;
        this.weight = weight;
        this.baggageStatus = baggageStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Baggage(int bookingId, double weight, String baggageStatus) {
        this.bookingId = bookingId;
        this.weight = weight;
        this.baggageStatus = baggageStatus;
    }

    public Baggage(int baggageId, int bookingId, double weight, String baggageStatus) {
        this.baggageId = baggageId;
        this.bookingId = bookingId;
        this.weight = weight;
        this.baggageStatus = baggageStatus;
    }

    public int getBaggageId() {
        return baggageId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getRoute() {
        return route;
    }

    public double getWeight() {
        return weight;
    }

    public String getBaggageStatus() {
        return baggageStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
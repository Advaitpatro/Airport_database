package com.airport.model;

public class Booking {
    private int bookingId;
    private int passengerId;
    private String passengerName;
    private int tripId;
    private String flightNumber;
    private String route;
    private String seatNumber;
    private double fare;
    private String bookingStatus;
    private String bookingDate;
    private double baggageWeight;

    public Booking(int bookingId, int passengerId, String passengerName, int tripId,
                   String flightNumber, String route, String seatNumber, double fare,
                   String bookingStatus, String bookingDate, double baggageWeight) {
        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.tripId = tripId;
        this.flightNumber = flightNumber;
        this.route = route;
        this.seatNumber = seatNumber;
        this.fare = fare;
        this.bookingStatus = bookingStatus;
        this.bookingDate = bookingDate;
        this.baggageWeight = baggageWeight;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getTripId() {
        return tripId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getRoute() {
        return route;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public double getFare() {
        return fare;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public double getBaggageWeight() {
        return baggageWeight;
    }
}
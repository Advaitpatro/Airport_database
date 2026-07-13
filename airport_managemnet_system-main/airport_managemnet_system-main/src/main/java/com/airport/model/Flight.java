package com.airport.model;

public class Flight {
    private int flightId;
    private String flightNumber;
    private String airlineName;

    public Flight() {
    }

    public Flight(int flightId, String flightNumber, String airlineName) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.airlineName = airlineName;
    }

    public Flight(String flightNumber, String airlineName) {
        this.flightNumber = flightNumber;
        this.airlineName = airlineName;
    }

    public int getFlightId() {
        return flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }
}
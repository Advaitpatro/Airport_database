package com.airport.model;

public class Trip {
    private int tripId;
    private int flightId;
    private String flightNumber;
    private String sourceAirport;
    private String destinationAirport;
    private String departureTime;
    private String arrivalTime;
    private int totalSeats;
    private String status;

    public Trip() {
    }

    public Trip(int tripId, int flightId, String flightNumber, String sourceAirport,
                String destinationAirport, String departureTime, String arrivalTime,
                int totalSeats, String status) {
        this.tripId = tripId;
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.status = status;
    }

    public Trip(int flightId, String sourceAirport, String destinationAirport,
                String departureTime, String arrivalTime, int totalSeats, String status) {
        this.flightId = flightId;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.status = status;
    }

    public Trip(int tripId, int flightId, String sourceAirport, String destinationAirport,
                String departureTime, String arrivalTime, int totalSeats, String status) {
        this.tripId = tripId;
        this.flightId = flightId;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.status = status;
    }

    public int getTripId() {
        return tripId;
    }

    public int getFlightId() {
        return flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSourceAirport() {
        return sourceAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public String getStatus() {
        return status;
    }
}
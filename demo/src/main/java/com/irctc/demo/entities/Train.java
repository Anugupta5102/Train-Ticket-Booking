package com.irctc.demo.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
public class Train {
    
    @JsonProperty("trainNumber")
    private String trainId; // Maps to trainNumber in JSON

    @JsonProperty("trainName")
    private String trainName;

    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private List<List<Integer>> seats;

    @Builder.Default
    private Map<String, String> stationTimes = new HashMap<>(); // Initialize to avoid null

    private List<String> stations;

    private String sourceStation;
    private String destinationStation;

    public Train(){}

    public Train(String trainId, String trainName, String source, String destination, int totalSeats, int availableSeats, List<List<Integer>> seats, Map<String, String> stationTimes, List<String> stations, String sourceStation, String destinationStation){
        this.trainId = trainId;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.seats = seats;
        this.stationTimes = stationTimes != null ? stationTimes : new HashMap<>(); // Avoid null
        this.stations = stations;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    public List<String> getStations(){
        return stations;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats){
       this.seats = seats;
    }

    public String getTrainId(){
        return trainId;
    }

    public Map<String, String> getStationTimes(){
        return stationTimes;
    }

    public String getTrainName(){
        return trainName;
    }

    public void setTrainName(String trainName){
        this.trainName = trainName;
    }

    public void setTrainId(String trainId){
        this.trainId = trainId;
    }

    public void setStationTimes(Map<String, String> stationTimes){
        this.stationTimes = stationTimes != null ? stationTimes : new HashMap<>(); // Avoid null
    }

    public void setStations(List<String> stations){
        this.stations = stations;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public String getTrainInfo(){
        return String.format("Train ID: %s Train No: %s", trainId, trainName);
    }

}
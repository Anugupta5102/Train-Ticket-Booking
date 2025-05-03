package com.irctc.demo.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.irctc.demo.entities.Train;
import com.irctc.demo.entities.User;
import com.irctc.demo.util.UserServiceUtil;


public class UserBookingService{
    
    private final ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private List<User> userList;
    private List<Train> trains;
    private User user;

    private final String USER_FILE_PATH = "demo/src/main/java/com/irctc/demo/localDB/users.json";
    private final String TRAIN_FILE_PATH = "demo/src/main/java/com/irctc/demo/localDB/trains.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
        loadTrainListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
        loadTrainListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        userList = objectMapper.readValue(new File(USER_FILE_PATH), new TypeReference<List<User>>() {});
    }

    private void loadTrainListFromFile() throws IOException {
        trains = objectMapper.readValue(new File(TRAIN_FILE_PATH), new TypeReference<List<Train>>() {});
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    public void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_FILE_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBookings() {
        if (user == null) {
            System.out.println("No user is logged in.");
            return;
        }
        if (user.getBookings() == null || user.getBookings().isEmpty()) {
            System.out.println("No bookings found for the logged-in user.");
            return;
        }
        System.out.println("Your bookings:");
        for (String booking : user.getBookings()) {
            System.out.println(booking);
        }
    }

    public boolean cancelBooking(String bookingId) {
        if (user == null) {
            System.out.println("No user is logged in.");
            return false;
        }
        List<String> bookings = user.getBookings();
        for (String booking : bookings) {
            if (booking.contains(bookingId)) {
                bookings.remove(booking);
                System.out.println("Booking with ID " + bookingId + " has been canceled.");
                return true;
            }
        }
        System.out.println("Booking ID not found.");
        return false;
    }

    public List<Train> getTrains(String source, String destination) {
        List<Train> matchingTrains = new ArrayList<>();
        for (Train train : trains) {
            if (train.getSource() != null && train.getDestination() != null &&
                train.getSource().equalsIgnoreCase(source) &&
                train.getDestination().equalsIgnoreCase(destination)) {
                matchingTrains.add(train);
            }
        }
        return matchingTrains;
    }

    public List<List<Integer>> fetchSeats(Train train) {
        if (train == null) {
            System.out.println("No train selected for seat booking.");
            return null;
        }

        List<List<Integer>> seats = train.getSeats();
        if (seats == null) { // Initialize seats if null
            seats = new ArrayList<>();
            for (int i = 0; i < 10; i++) { // Example: 10 rows
                List<Integer> row = new ArrayList<>();
                for (int j = 0; j < 10; j++) { // Example: 10 columns
                    row.add(0); // 0 indicates available seat
                }
                seats.add(row);
            }
            train.setSeats(seats); // Save initialized seats back to the train
        }
        return seats;
    }

    public String bookTrainSeat(Train train, int row, int col) {
        if (train == null || train.getSeats() == null) {
            System.out.println("Invalid train or seat data.");
            return null;
        }
        List<List<Integer>> seats = train.getSeats();
        if (row >= 0 && row < seats.size() && col >= 0 && col < seats.get(row).size()) {
            if (seats.get(row).get(col) == 0) {
                seats.get(row).set(col, 1); // Mark seat as booked
                train.setAvailableSeats(train.getAvailableSeats() - 1);
                String bookingId = UUID.randomUUID().toString(); // Generate unique booking ID
                String bookingDetails = "Booking ID: " + bookingId + ", Train: " + train.getTrainName() + 
                                        ", Seat: Row " + row + ", Column " + col;
                user.getBookings().add(bookingDetails); // Add booking to user's bookings
                try {
                    saveUserListToFile(); // Save user list with updated bookings
                } catch (IOException e) {
                    System.out.println("Error saving booking: " + e.getMessage());
                    return null;
                }
                return bookingId;
            } else {
                System.out.println("Seat already booked.");
            }
        } else {
            System.out.println("Invalid seat selection.");
        }
        return null;
    }
}
package com.irctc.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.irctc.demo.entities.Train;
import com.irctc.demo.entities.User;
import com.irctc.demo.services.UserBookingService;
import com.irctc.demo.util.UserServiceUtil;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        try (Scanner scanner = new Scanner(System.in)) {
            int option = 0;
            UserBookingService userBookingService;
            try {
                userBookingService = new UserBookingService();
            } catch (IOException ex) {
                System.out.println("There is something wrong");
                return;
            }
            Train trainSelectedForBooking = null;
            while (option != 7) {
                System.out.println("Choose option");
                System.out.println("1. Sign up");
                System.out.println("2. Login");
                System.out.println("3. Fetch Bookings");
                System.out.println("4. Search Trains");
                System.out.println("5. Book a Seat");
                System.out.println("6. Cancel my Booking");
                System.out.println("7. Exit the App");
                try {
                    option = scanner.nextInt();
                    scanner.nextLine();
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    scanner.nextLine();
                    continue;
                }
                switch (option) {
                    case 1 -> {
                        System.out.println("Enter the username to signup");
                        String nameToSignUp = scanner.nextLine();
                        System.out.println("Enter the password to signup");
                        String passwordToSignUp = scanner.nextLine();
                        User userToSignup = new User(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString(), new ArrayList<>());
                        userBookingService.signUp(userToSignup);
                        System.out.println("Signup successful! Welcome, " + nameToSignUp);
                    }
                    case 2 -> {
                        System.out.println("Enter the username to Login");
                        String nameToLogin = scanner.nextLine();
                        System.out.println("Enter the password to login");
                        String passwordToLogin = scanner.nextLine();
                        User userToLogin = new User(nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString(), new ArrayList<>());
                        try {
                            userBookingService = new UserBookingService(userToLogin);
                            System.out.println("Login successful! Welcome back, " + nameToLogin);
                        } catch (IOException ex) {
                            System.out.println("Login failed. Please try again.");
                        }
                    }
                    case 3 -> {
                        System.out.println("Fetching your bookings");
                        try {
                            userBookingService.fetchBookings();
                        } catch (Exception e) {
                            System.out.println("Error while fetching bookings: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        System.out.println("Type your source station");
                        String source = scanner.nextLine();
                        System.out.println("Type your destination station");
                        String dest = scanner.nextLine();
                        try {
                            List<Train> trains = userBookingService.getTrains(source, dest);
                            if (trains.isEmpty()) {
                                System.out.println("No trains found for the given source and destination.");
                                break;
                            }
                            int index = 1;
                            for (Train t : trains) {
                                System.out.println(index + " Train id: " + t.getTrainId() + ", Train name: " + t.getTrainName());
                                if (t.getStationTimes() != null && !t.getStationTimes().isEmpty()) {
                                    for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                                        System.out.println("Station: " + entry.getKey() + ", Time: " + entry.getValue());
                                    }
                                } else {
                                    System.out.println("No station times available for this train.");
                                }
                                index++;
                            }
                            System.out.println("Select a train by typing 1,2,3...");
                            int trainIndex = scanner.nextInt();
                            scanner.nextLine();
                            if (trainIndex > 0 && trainIndex <= trains.size()) {
                                trainSelectedForBooking = trains.get(trainIndex - 1);
                                System.out.println("Train selected: " + trainSelectedForBooking.getTrainName());
                            } else {
                                System.out.println("Invalid train selection");
                            }
                        } catch (Exception e) {
                            System.out.println("Error while searching for trains: " + e.getMessage());
                        }
                    }
                    case 5 -> {
                        if (trainSelectedForBooking == null) {
                            System.out.println("No train selected. Please search and select a train first.");
                            break;
                        }
                        System.out.println("Select a seat out of these seats");
                        List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                        if (seats == null) {
                            System.out.println("No seats available for the selected train.");
                            break;
                        }
                        for (List<Integer> row : seats) {
                            for (Integer val : row) {
                                System.out.print(val + " ");
                            }
                            System.out.println();
                        }
                        System.out.println("Select the seat by typing the row and column");
                        try {
                            System.out.println("Enter the row");
                            int row = scanner.nextInt();
                            System.out.println("Enter the column");
                            int col = scanner.nextInt();
                            scanner.nextLine();
                            if (row >= 0 && row < seats.size() && col >= 0 && col < seats.get(row).size()) {
                                System.out.println("Booking your seat....");
                                String bookingId = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                                if (bookingId != null) {
                                    System.out.println("Booked! Enjoy your journey. Your booking ID is: " + bookingId);
                                    try {
                                        userBookingService.saveUserListToFile();
                                    } catch (IOException e) {
                                        System.out.println("Error saving user list: " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Can't book this seat");
                                }
                            } else {
                                System.out.println("Invalid seat selection. Please try again.");
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Invalid input. Please enter valid row and column numbers.");
                            scanner.nextLine();
                        }
                    }
                    case 6 -> {
                        System.out.println("Canceling your booking...");
                        try {
                            System.out.println("Enter the booking ID to cancel:");
                            String bookingId = scanner.nextLine();
                            boolean isCanceled = userBookingService.cancelBooking(bookingId);
                            if (isCanceled) {
                                System.out.println("Booking canceled successfully.");
                                try {
                                    userBookingService.saveUserListToFile();
                                } catch (IOException e) {
                                    System.out.println("Error saving user list: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Failed to cancel the booking. Please check the booking ID.");
                            }
                        } catch (Exception e) {
                            System.out.println("An error occurred while canceling the booking. Please try again.");
                        }
                    }
                    case 7 -> System.out.println("Exiting the application. Thank you!");
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 7.");
                }
            }
        }
    }
}
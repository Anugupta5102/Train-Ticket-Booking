# 🚆 Train Ticket Booking System (Console-Based Java Application)

This is a **console-based Train Booking System** implemented in **Java** using the **Maven build system**. It follows a modular design aligned with standard software engineering principles and uses JSON for persistent data storage.

---

## 🛠️ Tech Stack

- **Java** – Core programming language  
- **Maven** – Project build and dependency management  
- **Jackson** – JSON serialization/deserialization  
- **BCrypt (Spring Security)** – Secure password hashing and verification  

---

## 📁 Project Structure & Modules

### 1. 🔹 Main Application

- `IrctcApplication.java` – Menu-driven CLI for login, signup, train search, seat booking, and cancellation

### 2. 🔸 Entities

- `User.java` – Contains username, hashed password, and tickets
- `Train.java` – Contains train ID, name, route, seat matrix, station timings
- `Ticket.java` – Contains ticket ID, travel info, train, and user details

### 3. 🧩 Services

- `UserBookingService.java` – User login, signup, booking/cancellation, file operations
- `TrainService.java` – Train search, update, file persistence

### 4. 🔧 Utilities

- `UserServiceUtil.java` – BCrypt password hashing and JSON config

---

## 💾 Data Storage

- **`users.json`** – Stores registered users and bookings  
- **`trains.json`** – Stores train details and seat info  

---

## 🚀 Features

- 🔐 Secure login/signup with hashed passwords  
- 🧭 Train management with multiple stations  
- 🎟️ Real-time seat booking and ticket generation  
- 📋 View/cancel booked tickets  
- 💾 Local JSON-based persistence
  
---

## 📌 Notes

- ✅ Designed for **single-user CLI usage**
- 🌐 Easily extendable to **web** or **GUI (JavaFX/Swing)** front-end
- 💾 Uses **no external database**, making it ideal for learning, testing, or demos

---

## 📬 Contribution

Feel free to fork this repository and enhance it with:

- 🛠️ Admin features for train and user management  
- 🔄 Real-time seat availability and conflict handling  
- 🎨 Better UI using **Swing** or **JavaFX**  
- 🌍 REST API layer for web and mobile integration  

---

## 📃 License

This project is licensed under the **MIT License**.

---

> Created by [@Anugupta5102](https://github.com/Anugupta5102)  
> ✨ Educational Project | Java | CLI Booking System



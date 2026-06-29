import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Raj@4706";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection =
                         DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

                while (true) {

                    System.out.println("\n========== HOTEL MANAGEMENT SYSTEM ==========");
                    System.out.println("1. Reserve a Room");
                    System.out.println("2. View Reservations");
                    System.out.println("3. Get Room Number");
                    System.out.println("4. Update Reservation");
                    System.out.println("5. Delete Reservation");
                    System.out.println("0. Exit");
                    System.out.print("Choose an option: ");

                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            reserveRoom(connection, scanner);
                            break;

                        case 2:
                            viewReservations(connection);
                            break;

                        case 3:
                            getRoomNumber(connection, scanner);
                            break;

                        case 4:
                            updateReservation(connection, scanner);
                            break;

                        case 5:
                            deleteReservation(connection, scanner);
                            break;

                        case 0:
                            exit();
                            scanner.close();
                            return;

                        default:
                            System.out.println("Invalid Choice! Please try again.");
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver Not Found!");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Program Interrupted.");
        }
    }

    // ================= RESERVE ROOM =================

    private static void reserveRoom(Connection connection,
                                    Scanner scanner) {

        try {

            scanner.nextLine();

            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter Room Number: ");
            int roomNumber = scanner.nextInt();

            String contactNumber;

            while (true) {
                System.out.print("Enter Contact Number: ");
                contactNumber = scanner.next();

                if (contactNumber.matches("\\d{10}")) {
                    break;
                }

                System.out.println(
                        "Invalid Contact Number! Please enter exactly 10 digits.");
            }

            String sql =
                    "INSERT INTO reservations " +
                            "(guest_name, room_no, contact_number) " +
                            "VALUES (?, ?, ?)";

            try (PreparedStatement ps =
                         connection.prepareStatement(sql)) {

                ps.setString(1, guestName);
                ps.setInt(2, roomNumber);
                ps.setString(3, contactNumber);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Reservation Successful!");
                } else {
                    System.out.println("Reservation Failed!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ================= VIEW RESERVATIONS =================

    private static void viewReservations(Connection connection) {

        String sql =
                "SELECT reservation_id, guest_name, room_no, " +
                        "contact_number, reservation_date " +
                        "FROM reservations";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nCurrent Reservations:");

            System.out.println("+----------------+----------------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest Name           | Room Number   | Contact Number       | Reservation Date        |");
            System.out.println("+----------------+----------------------+---------------+----------------------+-------------------------+");

            while (rs.next()) {

                System.out.printf(
                        "| %-14d | %-20s | %-13d | %-20s | %-23s |\n",
                        rs.getInt("reservation_id"),
                        rs.getString("guest_name"),
                        rs.getInt("room_no"),
                        rs.getString("contact_number"),
                        rs.getTimestamp("reservation_date")
                );
            }

            System.out.println("+----------------+----------------------+---------------+----------------------+-------------------------+");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ================= GET ROOM NUMBER =================

    private static void getRoomNumber(Connection connection,
                                      Scanner scanner) {

        try {

            System.out.print("Enter Reservation ID: ");
            int reservationId = scanner.nextInt();

            scanner.nextLine();

            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            String sql =
                    "SELECT room_no " +
                            "FROM reservations " +
                            "WHERE reservation_id = ? " +
                            "AND guest_name = ?";

            try (PreparedStatement ps =
                         connection.prepareStatement(sql)) {

                ps.setInt(1, reservationId);
                ps.setString(2, guestName);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        System.out.println(
                                "Room Number : " +
                                        rs.getInt("room_no"));
                    } else {
                        System.out.println(
                                "Reservation Not Found!");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ================= UPDATE RESERVATION =================

    private static void updateReservation(Connection connection,
                                          Scanner scanner) {

        try {

            System.out.print("Enter Reservation ID: ");
            int reservationId = scanner.nextInt();

            scanner.nextLine();

            if (!reservationExists(connection, reservationId)) {
                System.out.println(
                        "Reservation Not Found!");
                return;
            }

            System.out.print("Enter New Guest Name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter New Room Number: ");
            int roomNumber = scanner.nextInt();

            String contactNumber;

            while (true) {

                System.out.print(
                        "Enter New Contact Number: ");

                contactNumber = scanner.next();

                if (contactNumber.matches("\\d{10}")) {
                    break;
                }

                System.out.println(
                        "Invalid Contact Number! Please enter exactly 10 digits.");
            }

            String sql =
                    "UPDATE reservations " +
                            "SET guest_name = ?, " +
                            "room_no = ?, " +
                            "contact_number = ? " +
                            "WHERE reservation_id = ?";

            try (PreparedStatement ps =
                         connection.prepareStatement(sql)) {

                ps.setString(1, guestName);
                ps.setInt(2, roomNumber);
                ps.setString(3, contactNumber);
                ps.setInt(4, reservationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Reservation Updated Successfully!");
                } else {
                    System.out.println(
                            "Reservation Update Failed!");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ================= DELETE RESERVATION =================

    private static void deleteReservation(Connection connection,
                                          Scanner scanner) {

        try {

            System.out.print("Enter Reservation ID: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println(
                        "Reservation Not Found!");
                return;
            }

            String sql =
                    "DELETE FROM reservations " +
                            "WHERE reservation_id = ?";

            try (PreparedStatement ps =
                         connection.prepareStatement(sql)) {

                ps.setInt(1, reservationId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Reservation Deleted Successfully!");
                } else {
                    System.out.println(
                            "Deletion Failed!");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ================= CHECK RESERVATION =================

    private static boolean reservationExists(
            Connection connection,
            int reservationId) {

        String sql =
                "SELECT reservation_id " +
                        "FROM reservations " +
                        "WHERE reservation_id = ?";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            return false;
        }
    }

    // ================= EXIT =================

    public static void exit()
            throws InterruptedException {

        System.out.print("Exiting System");

        for (int i = 0; i < 5; i++) {
            System.out.print(".");
            Thread.sleep(400);
        }

        System.out.println();
        System.out.println(
                "Thank You For Using Hotel Reservation System!");
    }
}
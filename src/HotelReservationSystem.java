import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class HotelReservationSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static final HotelService hotelService = new HotelService();
    private static final RoomDAO roomDAO = new RoomDAO();
    private static final ReservationDAO reservationDAO = new ReservationDAO();

    public static void main(String[] args) {

        System.out.println(
                "\n=============================================="
        );
        System.out.println(
                "     WELCOME TO HOTEL RESERVATION SYSTEM"
        );
        System.out.println(
                "=============================================="
        );

        while (true) {

            displayMenu();

            int choice = readInteger(
                    "Enter your choice: "
            );

            switch (choice) {

                case 1:
                    viewAllRooms();
                    break;

                case 2:
                    searchAvailableRooms();
                    break;

                case 3:
                    reserveRoom();
                    break;

                case 4:
                    viewAllReservations();
                    break;

                case 5:
                    searchReservationById();
                    break;

                case 6:
                    updateReservation();
                    break;

                case 7:
                    cancelReservation();
                    break;

                case 0:
                    exit();
                    return;

                default:
                    System.out.println(
                            "\nInvalid choice! Please try again."
                    );
            }
        }
    }


    // ==========================================
    // DISPLAY MAIN MENU
    // ==========================================

    private static void displayMenu() {

        System.out.println(
                "\n========== HOTEL RESERVATION SYSTEM =========="
        );

        System.out.println("1. View All Rooms");
        System.out.println("2. Search Available Rooms");
        System.out.println("3. Reserve a Room");
        System.out.println("4. View All Reservations");
        System.out.println("5. Search Reservation by ID");
        System.out.println("6. Update Reservation");
        System.out.println("7. Cancel Reservation");
        System.out.println("0. Exit");

        System.out.println(
                "=============================================="
        );
    }


    // ==========================================
    // VIEW ALL ROOMS
    // ==========================================

    private static void viewAllRooms() {

        List<Room> rooms =
                roomDAO.getAllRooms();

        if (rooms.isEmpty()) {

            System.out.println(
                    "\nNo rooms found."
            );

            return;
        }

        System.out.println(
                "\n================ ALL ROOMS ================="
        );

        System.out.printf(
                "%-12s %-15s %-18s %-15s%n",
                "Room No.",
                "Room Type",
                "Price/Night",
                "Status"
        );

        System.out.println(
                "------------------------------------------------------------"
        );

        for (Room room : rooms) {

            System.out.printf(
                    "%-12d %-15s Rs. %-10.2f %-15s%n",
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPricePerNight(),
                    room.isAvailable()
                            ? "Available"
                            : "Unavailable"
            );
        }
    }


    // ==========================================
    // SEARCH AVAILABLE ROOMS
    // ==========================================

    private static void searchAvailableRooms() {

        System.out.println(
                "\n========== SEARCH AVAILABLE ROOMS =========="
        );

        LocalDate checkInDate =
                readDate(
                        "Enter Check-In Date (YYYY-MM-DD): "
                );

        LocalDate checkOutDate =
                readDate(
                        "Enter Check-Out Date (YYYY-MM-DD): "
                );

        hotelService.displayAvailableRooms(
                checkInDate,
                checkOutDate
        );
    }


    // ==========================================
    // RESERVE ROOM
    // ==========================================

    private static void reserveRoom() {

        System.out.println(
                "\n========== RESERVE A ROOM =========="
        );

        LocalDate checkInDate =
                readDate(
                        "Enter Check-In Date (YYYY-MM-DD): "
                );

        LocalDate checkOutDate =
                readDate(
                        "Enter Check-Out Date (YYYY-MM-DD): "
                );


        // Validate dates before continuing
        if (!hotelService.validateDates(
                checkInDate,
                checkOutDate)) {

            return;
        }


        // Display available rooms
        List<Room> availableRooms =
                hotelService.getAvailableRooms(
                        checkInDate,
                        checkOutDate
                );

        if (availableRooms.isEmpty()) {

            System.out.println(
                    "No rooms are available for selected dates."
            );

            return;
        }


        System.out.println(
                "\n========== AVAILABLE ROOMS =========="
        );

        System.out.printf(
                "%-12s %-15s %-18s%n",
                "Room No.",
                "Room Type",
                "Price/Night"
        );

        System.out.println(
                "-----------------------------------------------"
        );

        for (Room room : availableRooms) {

            System.out.printf(
                    "%-12d %-15s Rs. %-10.2f%n",
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPricePerNight()
            );
        }


        int roomNumber =
                readInteger(
                        "\nEnter Room Number to book: "
                );


        System.out.print(
                "Enter Guest Name: "
        );

        String guestName =
                scanner.nextLine().trim();


        System.out.print(
                "Enter Contact Number: "
        );

        String contactNumber =
                scanner.nextLine().trim();


        hotelService.createReservation(
                guestName,
                contactNumber,
                roomNumber,
                checkInDate,
                checkOutDate,
                scanner
        );
    }


    // ==========================================
    // VIEW ALL RESERVATIONS
    // ==========================================

    private static void viewAllReservations() {

        List<Reservation> reservations =
                hotelService.getAllReservations();

        if (reservations.isEmpty()) {

            System.out.println(
                    "\nNo reservations found."
            );

            return;
        }


        System.out.println(
                "\n========================= RESERVATIONS ========================="
        );


        for (Reservation reservation : reservations) {

            System.out.println(
                    "\nReservation ID : "
                            + reservation.getReservationId()
            );

            System.out.println(
                    "Guest Name     : "
                            + reservation.getGuestName()
            );

            System.out.println(
                    "Contact Number : "
                            + reservation.getContactNumber()
            );

            System.out.println(
                    "Room Number    : "
                            + reservation.getRoomNumber()
            );

            System.out.println(
                    "Check-In Date  : "
                            + reservation.getCheckInDate()
            );

            System.out.println(
                    "Check-Out Date : "
                            + reservation.getCheckOutDate()
            );

            System.out.printf(
                    "Total Amount   : Rs. %.2f%n",
                    reservation.getTotalAmount()
            );

            System.out.println(
                    "Payment Status : "
                            + reservation.getPaymentStatus()
            );

            System.out.println(
                    "---------------------------------------------------------------"
            );
        }
    }


    // ==========================================
    // SEARCH RESERVATION BY ID
    // ==========================================

    private static void searchReservationById() {

        System.out.println(
                "\n========== SEARCH RESERVATION =========="
        );

        int reservationId =
                readInteger(
                        "Enter Reservation ID: "
                );


        Reservation reservation =
                hotelService.getReservationById(
                        reservationId
                );


        if (reservation == null) {

            System.out.println(
                    "Reservation not found."
            );

            return;
        }


        System.out.println(
                "\n========== RESERVATION DETAILS =========="
        );

        System.out.println(
                "Reservation ID : "
                        + reservation.getReservationId()
        );

        System.out.println(
                "Guest Name     : "
                        + reservation.getGuestName()
        );

        System.out.println(
                "Contact Number : "
                        + reservation.getContactNumber()
        );

        System.out.println(
                "Room Number    : "
                        + reservation.getRoomNumber()
        );

        System.out.println(
                "Check-In Date  : "
                        + reservation.getCheckInDate()
        );

        System.out.println(
                "Check-Out Date : "
                        + reservation.getCheckOutDate()
        );

        System.out.printf(
                "Total Amount   : Rs. %.2f%n",
                reservation.getTotalAmount()
        );

        System.out.println(
                "Payment Status : "
                        + reservation.getPaymentStatus()
        );
    }


    // ==========================================
    // UPDATE RESERVATION
    // ==========================================

    private static void updateReservation() {

        System.out.println(
                "\n========== UPDATE RESERVATION =========="
        );

        int reservationId =
                readInteger(
                        "Enter Reservation ID: "
                );


        Reservation existingReservation =
                hotelService.getReservationById(
                        reservationId
                );


        if (existingReservation == null) {

            System.out.println(
                    "Reservation not found."
            );

            return;
        }


        System.out.println(
                "\nCurrent Reservation:"
        );

        System.out.println(
                existingReservation
        );


        System.out.print(
                "\nEnter New Guest Name: "
        );

        String guestName =
                scanner.nextLine().trim();


        if (guestName.isEmpty()) {

            System.out.println(
                    "Guest name cannot be empty."
            );

            return;
        }


        System.out.print(
                "Enter New Contact Number: "
        );

        String contactNumber =
                scanner.nextLine().trim();


        if (!contactNumber.matches("\\d{10}")) {

            System.out.println(
                    "Contact number must contain exactly 10 digits."
            );

            return;
        }


        LocalDate checkInDate =
                readDate(
                        "Enter New Check-In Date (YYYY-MM-DD): "
                );


        LocalDate checkOutDate =
                readDate(
                        "Enter New Check-Out Date (YYYY-MM-DD): "
                );


        if (!hotelService.validateDates(
                checkInDate,
                checkOutDate)) {

            return;
        }


        // Display available rooms for update
// Current reservation will be ignored
        List<Room> availableRooms =
                roomDAO.getAvailableRoomsForUpdate(
                        checkInDate,
                        checkOutDate,
                        reservationId
                );

        if (availableRooms.isEmpty()) {

            System.out.println(
                    "No rooms are available for the selected dates."
            );

            return;
        }


        System.out.println(
                "\nAvailable Rooms:"
        );


        for (Room room : availableRooms) {

            System.out.printf(
                    "Room: %d | Type: %s | Price/Night: Rs. %.2f%n",
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPricePerNight()
            );
        }


        int roomNumber =
                readInteger(
                        "\nEnter New Room Number: "
                );


        Room selectedRoom =
                roomDAO.getRoomByNumber(
                        roomNumber
                );


        if (selectedRoom == null) {

            System.out.println(
                    "Invalid room number."
            );

            return;
        }


        // Check room availability while ignoring
// the reservation currently being updated

        boolean roomAvailable =
                roomDAO.isRoomAvailableForUpdate(
                        roomNumber,
                        checkInDate,
                        checkOutDate,
                        reservationId
                );

        if (!roomAvailable) {

            System.out.println(
                    "Selected room is already booked "
                            + "for the given dates."
            );

            return;
        }


        long numberOfNights =
                hotelService.calculateNumberOfNights(
                        checkInDate,
                        checkOutDate
                );


        double totalAmount =
                hotelService.calculateTotalAmount(
                        selectedRoom,
                        checkInDate,
                        checkOutDate
                );


        // Update reservation object
        existingReservation.setGuestName(
                guestName
        );

        existingReservation.setContactNumber(
                contactNumber
        );

        existingReservation.setRoomNumber(
                roomNumber
        );

        existingReservation.setCheckInDate(
                checkInDate
        );

        existingReservation.setCheckOutDate(
                checkOutDate
        );

        existingReservation.setTotalAmount(
                totalAmount
        );


        boolean updated =
                reservationDAO.updateReservation(
                        existingReservation
                );


        if (updated) {

            System.out.println(
                    "\nReservation Updated Successfully!"
            );

            System.out.println(
                    "Number of Nights: "
                            + numberOfNights
            );

            System.out.printf(
                    "Updated Total Amount: Rs. %.2f%n",
                    totalAmount
            );

        } else {

            System.out.println(
                    "Reservation update failed."
            );
        }
    }


    // ==========================================
    // CANCEL RESERVATION
    // ==========================================

    private static void cancelReservation() {

        System.out.println(
                "\n========== CANCEL RESERVATION =========="
        );

        int reservationId =
                readInteger(
                        "Enter Reservation ID: "
                );


        Reservation reservation =
                hotelService.getReservationById(
                        reservationId
                );


        if (reservation == null) {

            System.out.println(
                    "Reservation not found."
            );

            return;
        }


        System.out.println(
                "\nReservation Details:"
        );

        System.out.println(
                reservation
        );


        System.out.print(
                "\nAre you sure you want to cancel this reservation? (Y/N): "
        );


        String confirmation =
                scanner.nextLine().trim();


        if (confirmation.equalsIgnoreCase("Y")) {

            hotelService.cancelReservation(
                    reservationId
            );

        } else {

            System.out.println(
                    "Cancellation aborted."
            );
        }
    }


    // ==========================================
    // READ INTEGER SAFELY
    // ==========================================

    private static int readInteger(
            String message) {

        while (true) {

            System.out.print(
                    message
            );

            String input =
                    scanner.nextLine().trim();

            try {

                return Integer.parseInt(
                        input
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input! Please enter a valid number."
                );
            }
        }
    }


    // ==========================================
    // READ DATE SAFELY
    // ==========================================

    private static LocalDate readDate(
            String message) {

        while (true) {

            System.out.print(
                    message
            );

            String input =
                    scanner.nextLine().trim();

            try {

                return LocalDate.parse(
                        input
                );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date format!"
                );

                System.out.println(
                        "Please enter date in YYYY-MM-DD format."
                );
            }
        }
    }


    // ==========================================
    // EXIT
    // ==========================================

    private static void exit() {

        System.out.println(
                "\nExiting Hotel Reservation System..."
        );

        System.out.println(
                "Thank you for using our system!"
        );

        scanner.close();
    }
}
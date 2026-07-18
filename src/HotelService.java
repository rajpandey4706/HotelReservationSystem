import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class HotelService {

    private final RoomDAO roomDAO;
    private final ReservationDAO reservationDAO;

    // Constructor
    public HotelService() {
        this.roomDAO = new RoomDAO();
        this.reservationDAO = new ReservationDAO();
    }


    // ==========================================
    // VALIDATE BOOKING DATES
    // ==========================================

    public boolean validateDates(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        if (checkInDate == null || checkOutDate == null) {
            System.out.println(
                    "Check-in and check-out dates cannot be empty."
            );
            return false;
        }

        if (checkInDate.isBefore(LocalDate.now())) {
            System.out.println(
                    "Check-in date cannot be in the past."
            );
            return false;
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            System.out.println(
                    "Check-out date must be after check-in date."
            );
            return false;
        }

        return true;
    }


    // ==========================================
    // GET AVAILABLE ROOMS
    // ==========================================

    public List<Room> getAvailableRooms(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        if (!validateDates(checkInDate, checkOutDate)) {
            return List.of();
        }

        return roomDAO.getAvailableRooms(
                checkInDate,
                checkOutDate
        );
    }


    // ==========================================
    // DISPLAY AVAILABLE ROOMS
    // ==========================================

    public void displayAvailableRooms(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        List<Room> availableRooms =
                getAvailableRooms(
                        checkInDate,
                        checkOutDate
                );

        if (availableRooms.isEmpty()) {
            System.out.println(
                    "No rooms are available for the selected dates."
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
    }


    // ==========================================
    // CHECK ROOM AVAILABILITY
    // ==========================================

    public boolean isRoomAvailable(
            int roomNumber,
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        List<Room> availableRooms =
                roomDAO.getAvailableRooms(
                        checkInDate,
                        checkOutDate
                );

        for (Room room : availableRooms) {

            if (room.getRoomNumber() == roomNumber) {
                return true;
            }
        }

        return false;
    }


    // ==========================================
    // CALCULATE NUMBER OF NIGHTS
    // ==========================================

    public long calculateNumberOfNights(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        return ChronoUnit.DAYS.between(
                checkInDate,
                checkOutDate
        );
    }


    // ==========================================
    // CALCULATE TOTAL AMOUNT
    // ==========================================

    public double calculateTotalAmount(
            Room room,
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        long numberOfNights =
                calculateNumberOfNights(
                        checkInDate,
                        checkOutDate
                );

        return numberOfNights
                * room.getPricePerNight();
    }


    // ==========================================
    // CREATE RESERVATION
    // ==========================================

    public boolean createReservation(
            String guestName,
            String contactNumber,
            int roomNumber,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Scanner scanner) {

        // Validate guest name
        if (guestName == null
                || guestName.trim().isEmpty()) {

            System.out.println(
                    "Guest name cannot be empty."
            );

            return false;
        }


        // Validate contact number
        if (contactNumber == null
                || !contactNumber.matches("\\d{10}")) {

            System.out.println(
                    "Contact number must contain exactly 10 digits."
            );

            return false;
        }


        // Validate dates
        if (!validateDates(
                checkInDate,
                checkOutDate)) {

            return false;
        }


        // Check if room exists
        Room room =
                roomDAO.getRoomByNumber(
                        roomNumber
                );

        if (room == null) {

            System.out.println(
                    "Invalid room number."
            );

            return false;
        }


        // Check room availability
        if (!isRoomAvailable(
                roomNumber,
                checkInDate,
                checkOutDate)) {

            System.out.println(
                    "Selected room is not available "
                            + "for the given dates."
            );

            return false;
        }


        // Calculate number of nights
        long numberOfNights =
                calculateNumberOfNights(
                        checkInDate,
                        checkOutDate
                );


        // Calculate total amount
        double totalAmount =
                calculateTotalAmount(
                        room,
                        checkInDate,
                        checkOutDate
                );


        // Display booking summary
        System.out.println(
                "\n========== BOOKING SUMMARY =========="
        );

        System.out.println(
                "Guest Name      : " + guestName
        );

        System.out.println(
                "Contact Number  : " + contactNumber
        );

        System.out.println(
                "Room Number     : "
                        + room.getRoomNumber()
        );

        System.out.println(
                "Room Type       : "
                        + room.getRoomType()
        );

        System.out.println(
                "Check-In Date   : "
                        + checkInDate
        );

        System.out.println(
                "Check-Out Date  : "
                        + checkOutDate
        );

        System.out.println(
                "Number of Nights: "
                        + numberOfNights
        );

        System.out.println(
                "Price Per Night : Rs. "
                        + room.getPricePerNight()
        );

        System.out.println(
                "Total Amount    : Rs. "
                        + totalAmount
        );


        // Payment simulation
        String paymentStatus =
                processPayment(
                        totalAmount,
                        scanner
                );


        // Create reservation object
        Reservation reservation =
                new Reservation(
                        guestName.trim(),
                        contactNumber,
                        roomNumber,
                        checkInDate,
                        checkOutDate,
                        totalAmount,
                        paymentStatus
                );


        // Save reservation in database
        boolean success =
                reservationDAO.addReservation(
                        reservation
                );


        if (success) {

            System.out.println(
                    "\nReservation Successful!"
            );

            System.out.println(
                    "Payment Status: "
                            + paymentStatus
            );

        } else {

            System.out.println(
                    "\nReservation Failed!"
            );
        }

        return success;
    }


    // ==========================================
    // PAYMENT SIMULATION
    // ==========================================

    private String processPayment(
            double totalAmount,
            Scanner scanner) {

        while (true) {

            System.out.println(
                    "\n========== PAYMENT =========="
            );

            System.out.println(
                    "Total Payable Amount: Rs. "
                            + totalAmount
            );

            System.out.println(
                    "1. Pay Now"
            );

            System.out.println(
                    "2. Pay Later"
            );

            System.out.print(
                    "Choose payment option: "
            );


            if (!scanner.hasNextInt()) {

                System.out.println(
                        "Invalid input. Please enter 1 or 2."
                );

                scanner.nextLine();

                continue;
            }


            int paymentChoice =
                    scanner.nextInt();

            scanner.nextLine();


            switch (paymentChoice) {

                case 1:

                    System.out.println(
                            "Payment processed successfully."
                    );

                    return "PAID";


                case 2:

                    System.out.println(
                            "Payment marked as pending."
                    );

                    return "PENDING";


                default:

                    System.out.println(
                            "Invalid payment option."
                    );
            }
        }
    }


    // ==========================================
    // GET ALL RESERVATIONS
    // ==========================================

    public List<Reservation> getAllReservations() {

        return reservationDAO
                .getAllReservations();
    }


    // ==========================================
    // GET RESERVATION BY ID
    // ==========================================

    public Reservation getReservationById(
            int reservationId) {

        return reservationDAO
                .getReservationById(
                        reservationId
                );
    }


    // ==========================================
    // CANCEL RESERVATION
    // ==========================================

    public boolean cancelReservation(
            int reservationId) {

        Reservation reservation =
                reservationDAO
                        .getReservationById(
                                reservationId
                        );

        if (reservation == null) {

            System.out.println(
                    "Reservation not found."
            );

            return false;
        }


        boolean cancelled =
                reservationDAO
                        .cancelReservation(
                                reservationId
                        );


        if (cancelled) {

            System.out.println(
                    "Reservation cancelled successfully."
            );

        } else {

            System.out.println(
                    "Unable to cancel reservation."
            );
        }

        return cancelled;
    }
}
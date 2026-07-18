import java.time.LocalDate;

public class Reservation {

    private int reservationId;
    private String guestName;
    private String contactNumber;
    private int roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private String paymentStatus;

    // Constructor for creating a new reservation
    public Reservation(String guestName,
                       String contactNumber,
                       int roomNumber,
                       LocalDate checkInDate,
                       LocalDate checkOutDate,
                       double totalAmount,
                       String paymentStatus) {

        this.guestName = guestName;
        this.contactNumber = contactNumber;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    // Constructor for retrieving an existing reservation from database
    public Reservation(int reservationId,
                       String guestName,
                       String contactNumber,
                       int roomNumber,
                       LocalDate checkInDate,
                       LocalDate checkOutDate,
                       double totalAmount,
                       String paymentStatus) {

        this.reservationId = reservationId;
        this.guestName = guestName;
        this.contactNumber = contactNumber;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    // Getters

    public int getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Setters

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Display reservation details
    @Override
    public String toString() {
        return "Reservation ID: " + reservationId
                + " | Guest: " + guestName
                + " | Contact: " + contactNumber
                + " | Room: " + roomNumber
                + " | Check-In: " + checkInDate
                + " | Check-Out: " + checkOutDate
                + " | Total Amount: ₹" + totalAmount
                + " | Payment Status: " + paymentStatus;
    }
}
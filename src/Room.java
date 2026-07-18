public class Room {

    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean available;

    // Constructor
    public Room(int roomNumber, String roomType,
                double pricePerNight, boolean available) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    // Getters
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setter for room availability
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber
                + " | Type: " + roomType
                + " | Price/Night: ₹" + pricePerNight
                + " | Available: " + (available ? "Yes" : "No");
    }
}
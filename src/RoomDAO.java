import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // ==========================================
    // GET ALL ROOMS
    // ==========================================

    public List<Room> getAllRooms() {

        List<Room> rooms = new ArrayList<>();

        String sql =
                "SELECT room_no, room_type, price_per_night, is_available " +
                        "FROM rooms ORDER BY room_no";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Room room = new Room(
                        rs.getInt("room_no"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night"),
                        rs.getBoolean("is_available")
                );

                rooms.add(room);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error while fetching rooms: " + e.getMessage()
            );
        }

        return rooms;
    }


    // ==========================================
    // GET ROOM BY ROOM NUMBER
    // ==========================================

    public Room getRoomByNumber(int roomNumber) {

        String sql =
                "SELECT room_no, room_type, price_per_night, is_available " +
                        "FROM rooms WHERE room_no = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Room(
                            rs.getInt("room_no"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_night"),
                            rs.getBoolean("is_available")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error while finding room: " + e.getMessage()
            );
        }

        return null;
    }


    // ==========================================
    // GET AVAILABLE ROOMS BETWEEN GIVEN DATES
    // ==========================================

    public List<Room> getAvailableRooms(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        List<Room> availableRooms = new ArrayList<>();

        String sql =
                "SELECT r.room_no, r.room_type, " +
                        "r.price_per_night, r.is_available " +
                        "FROM rooms r " +
                        "WHERE r.is_available = TRUE " +
                        "AND NOT EXISTS (" +
                        "SELECT 1 FROM reservations res " +
                        "WHERE res.room_no = r.room_no " +
                        "AND res.check_in_date < ? " +
                        "AND res.check_out_date > ?" +
                        ") " +
                        "ORDER BY r.room_no";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDate(
                    1,
                    java.sql.Date.valueOf(checkOutDate)
            );

            ps.setDate(
                    2,
                    java.sql.Date.valueOf(checkInDate)
            );

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Room room = new Room(
                            rs.getInt("room_no"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_night"),
                            rs.getBoolean("is_available")
                    );

                    availableRooms.add(room);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error while searching available rooms: "
                            + e.getMessage()
            );
        }

        return availableRooms;
    }


    // ==========================================
    // CHECK IF ROOM EXISTS
    // ==========================================

    public boolean roomExists(int roomNumber) {

        String sql =
                "SELECT room_no FROM rooms WHERE room_no = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error while checking room: " + e.getMessage()
            );

            return false;
        }
    }
    // ==========================================
// CHECK ROOM AVAILABILITY FOR UPDATE
// Ignores the current reservation
// ==========================================

    public boolean isRoomAvailableForUpdate(
            int roomNumber,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int reservationId) {

        String sql =
                "SELECT COUNT(*) FROM reservations " +
                        "WHERE room_no = ? " +
                        "AND reservation_id <> ? " +
                        "AND check_in_date < ? " +
                        "AND check_out_date > ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);
            ps.setInt(2, reservationId);
            ps.setDate(
                    3,
                    java.sql.Date.valueOf(checkOutDate)
            );
            ps.setDate(
                    4,
                    java.sql.Date.valueOf(checkInDate)
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while checking room availability: "
                            + e.getMessage()
            );
        }

        return false;
    }
    // ==========================================
// GET AVAILABLE ROOMS FOR UPDATE
// Ignores the current reservation
// ==========================================

    public List<Room> getAvailableRoomsForUpdate(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int reservationId) {

        List<Room> availableRooms = new ArrayList<>();

        String sql =
                "SELECT r.room_no, r.room_type, " +
                        "r.price_per_night, r.is_available " +
                        "FROM rooms r " +
                        "WHERE r.is_available = TRUE " +
                        "AND NOT EXISTS (" +
                        "SELECT 1 FROM reservations res " +
                        "WHERE res.room_no = r.room_no " +
                        "AND res.reservation_id <> ? " +
                        "AND res.check_in_date < ? " +
                        "AND res.check_out_date > ?" +
                        ") " +
                        "ORDER BY r.room_no";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            // Ignore current reservation
            ps.setInt(
                    1,
                    reservationId
            );

            // Requested check-out date
            ps.setDate(
                    2,
                    java.sql.Date.valueOf(checkOutDate)
            );

            // Requested check-in date
            ps.setDate(
                    3,
                    java.sql.Date.valueOf(checkInDate)
            );

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Room room = new Room(
                            rs.getInt("room_no"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_night"),
                            rs.getBoolean("is_available")
                    );

                    availableRooms.add(room);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while fetching available rooms for update: "
                            + e.getMessage()
            );
        }

        return availableRooms;
    }
}
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // ==========================================
    // ADD NEW RESERVATION
    // ==========================================

    public boolean addReservation(Reservation reservation) {

        String sql =
                "INSERT INTO reservations " +
                        "(guest_name, contact_number, room_no, " +
                        "check_in_date, check_out_date, total_amount, payment_status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, reservation.getGuestName());
            ps.setString(2, reservation.getContactNumber());
            ps.setInt(3, reservation.getRoomNumber());

            ps.setDate(
                    4,
                    java.sql.Date.valueOf(
                            reservation.getCheckInDate()
                    )
            );

            ps.setDate(
                    5,
                    java.sql.Date.valueOf(
                            reservation.getCheckOutDate()
                    )
            );

            ps.setDouble(
                    6,
                    reservation.getTotalAmount()
            );

            ps.setString(
                    7,
                    reservation.getPaymentStatus()
            );

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error while adding reservation: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ==========================================
    // GET ALL RESERVATIONS
    // ==========================================

    public List<Reservation> getAllReservations() {

        List<Reservation> reservations =
                new ArrayList<>();

        String sql =
                "SELECT reservation_id, guest_name, " +
                        "contact_number, room_no, check_in_date, " +
                        "check_out_date, total_amount, payment_status " +
                        "FROM reservations " +
                        "ORDER BY reservation_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Reservation reservation =
                        createReservationFromResultSet(rs);

                reservations.add(reservation);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while fetching reservations: "
                            + e.getMessage()
            );
        }

        return reservations;
    }


    // ==========================================
    // GET RESERVATION BY ID
    // ==========================================

    public Reservation getReservationById(
            int reservationId) {

        String sql =
                "SELECT reservation_id, guest_name, " +
                        "contact_number, room_no, check_in_date, " +
                        "check_out_date, total_amount, payment_status " +
                        "FROM reservations " +
                        "WHERE reservation_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return createReservationFromResultSet(rs);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while finding reservation: "
                            + e.getMessage()
            );
        }

        return null;
    }


    // ==========================================
    // UPDATE RESERVATION
    // ==========================================

    public boolean updateReservation(
            Reservation reservation) {

        String sql =
                "UPDATE reservations SET " +
                        "guest_name = ?, " +
                        "contact_number = ?, " +
                        "room_no = ?, " +
                        "check_in_date = ?, " +
                        "check_out_date = ?, " +
                        "total_amount = ?, " +
                        "payment_status = ? " +
                        "WHERE reservation_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(
                    1,
                    reservation.getGuestName()
            );

            ps.setString(
                    2,
                    reservation.getContactNumber()
            );

            ps.setInt(
                    3,
                    reservation.getRoomNumber()
            );

            ps.setDate(
                    4,
                    java.sql.Date.valueOf(
                            reservation.getCheckInDate()
                    )
            );

            ps.setDate(
                    5,
                    java.sql.Date.valueOf(
                            reservation.getCheckOutDate()
                    )
            );

            ps.setDouble(
                    6,
                    reservation.getTotalAmount()
            );

            ps.setString(
                    7,
                    reservation.getPaymentStatus()
            );

            ps.setInt(
                    8,
                    reservation.getReservationId()
            );

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error while updating reservation: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ==========================================
    // CANCEL / DELETE RESERVATION
    // ==========================================

    public boolean cancelReservation(
            int reservationId) {

        String sql =
                "DELETE FROM reservations " +
                        "WHERE reservation_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error while cancelling reservation: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ==========================================
    // CHECK IF RESERVATION EXISTS
    // ==========================================

    public boolean reservationExists(
            int reservationId) {

        String sql =
                "SELECT reservation_id " +
                        "FROM reservations " +
                        "WHERE reservation_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while checking reservation: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ==========================================
    // HELPER METHOD
    // CONVERT RESULTSET INTO RESERVATION OBJECT
    // ==========================================

    private Reservation createReservationFromResultSet(
            ResultSet rs) throws SQLException {

        return new Reservation(

                rs.getInt("reservation_id"),

                rs.getString("guest_name"),

                rs.getString("contact_number"),

                rs.getInt("room_no"),

                rs.getDate("check_in_date")
                        .toLocalDate(),

                rs.getDate("check_out_date")
                        .toLocalDate(),

                rs.getDouble("total_amount"),

                rs.getString("payment_status")
        );
    }
}
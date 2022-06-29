package es.udc.ws.app.model.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlReservationDao implements SqlReservationDao {

    protected AbstractSqlReservationDao() {
    }

    @Override
    public Reservation find(Connection connection, Long reservationId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT excursionId, userEmail,"
                + " creditCardNumber, registerDate, numParticipants, price, canceled FROM Reservation WHERE reservationId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservationId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(reservationId,
                        Reservation.class.getName());
            }

            /* Get results. */
            i = 1;
            Long excursionId = resultSet.getLong(i++);
            String userEmail = resultSet.getString(i++);
            String creditCardNumber = resultSet.getString(i++);
            Timestamp registerDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime registerDate = registerDateAsTimestamp != null
                    ? registerDateAsTimestamp.toLocalDateTime()
                    : null;
            int numParticipants = resultSet.getInt(i++);
            float price = resultSet.getFloat(i++);
            Timestamp timeStampCanceled = resultSet.getTimestamp(i++);

            /* Return reservation. */
            LocalDateTime newTimeStamp = null;
            if (timeStampCanceled != null) {
                newTimeStamp = timeStampCanceled.toLocalDateTime();
            }
            return new Reservation(reservationId, excursionId, userEmail,
                    creditCardNumber, registerDate, numParticipants,price, newTimeStamp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean existsByExcursionId(Connection connection, Long excursionId) {

        /* Create "queryString". */
        String queryString = "SELECT 1 FROM Reservation WHERE excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Return result. */
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Reservation reservation)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Reservation"
                + " SET excursionId = ?, userEmail = ?, registerDate = ?, "
                + " creditCardNumber = ?, price = ?, canceled = CURDATE() WHERE reservationId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {


            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservation.getExcursionId());
            preparedStatement.setString(i++, reservation.getUserEmail());
            Timestamp date = reservation.getRegisterDate() != null ? Timestamp.valueOf(reservation.getRegisterDate()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setString(i++, reservation.getCreditCardNumber());
            preparedStatement.setDouble(i++, reservation.getPrice());
            preparedStatement.setLong(i++, reservation.getReservationId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(reservation.getExcursionId(),
                        Reservation.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long reservationId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Reservation WHERE" + " reservationId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservationId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(reservationId,
                        Reservation.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Reservation> findByUserEmail(Connection connection, String userEmail) {
        /* Validations */

        /* Create "queryString". */
        String queryString = "SELECT reservationId, excursionId, "
                + " creditCardNumber, registerDate, numParticipants, price, canceled FROM Reservation WHERE userEmail = ?";
        queryString += "ORDER BY registerDate ASC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, userEmail);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read excursions. */
            List<Reservation> reservations = new ArrayList<Reservation>();

            while (resultSet.next()) {

                i = 1;

                Long reservationId = resultSet.getLong(i++);
                Long excursionId = resultSet.getLong(i++);
                String creditCardNumber = String.valueOf(resultSet.getString(i++));
                Timestamp registerDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime registerDate = registerDateAsTimestamp.toLocalDateTime();
                int numParticipants = resultSet.getInt(i++);
                float price = resultSet.getFloat(i++);
                Timestamp canceledAsTimestamp =resultSet.getTimestamp(i++);
                LocalDateTime newTimeStamp = null;
                if (canceledAsTimestamp != null) {
                    newTimeStamp = canceledAsTimestamp.toLocalDateTime();
                }

                reservations.add(new Reservation(reservationId, excursionId, userEmail, creditCardNumber, registerDate,
                numParticipants, price, newTimeStamp));
            }

            /* Return reservations. */
            return reservations;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

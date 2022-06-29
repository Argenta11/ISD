package es.udc.ws.app.model.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlReservationDao extends AbstractSqlReservationDao {

    @Override
    public Reservation create(Connection connection, Reservation reservation) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Reservation"
                + " (excursionId, userEmail, registerDate, creditCardNumber,"
                + " price, numParticipants) VALUES (?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservation.getExcursionId());
            preparedStatement.setString(i++, reservation.getUserEmail());
			Timestamp date = reservation.getRegisterDate() != null ? Timestamp.valueOf(reservation.getRegisterDate()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setString(i++, reservation.getCreditCardNumber());
            preparedStatement.setDouble(i++, reservation.getPrice());
            preparedStatement.setInt(i++, reservation.getNumParticipants());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long reservationId = resultSet.getLong(1);

            /* Return reservation. */
            return new Reservation(reservationId, reservation.getExcursionId(), reservation.getUserEmail(),
                     reservation.getCreditCardNumber(),reservation.getRegisterDate(),
                     reservation.getNumParticipants(),reservation.getPrice());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

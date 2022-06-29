package es.udc.ws.app.model.excursion;

import es.udc.ws.app.model.appservice.exceptions.ExcursionNotModificableException;
import es.udc.ws.app.model.reservation.SqlReservationDao;
import es.udc.ws.app.model.reservation.SqlReservationDaoFactory;
import es.udc.ws.util.exceptions.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A partial implementation of
 * <code>SQLExcursionDAO</code> that leaves
 * <code>create(Connection, Excursion)</code> as abstract.
 */
public abstract class AbstractSqlExcursionDao implements SqlExcursionDao {
    private final SqlReservationDao reservationDao = SqlReservationDaoFactory.getDao();
    protected AbstractSqlExcursionDao() {
    }

    @Override
    public Excursion find(Connection connection, Long excursionId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT city, description, "
                + " date, price, creationDate, maxParticipants, numFree FROM Excursion WHERE excursionID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(excursionId, Excursion.class.getName());
            }

            /* Get results. */
            i = 1;
            String city = resultSet.getString(i++);
			String description = resultSet.getString(i++);
			Timestamp dateAsTimestamp = resultSet.getTimestamp(i++);
			LocalDateTime date = dateAsTimestamp.toLocalDateTime();
			float price = resultSet.getFloat(i++);
			Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
			LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();
			int maxParticipants = resultSet.getInt(i++);
			int numFree = resultSet.getInt(i++);

            /* Return excursion. */
            return new Excursion(excursionId, city, description, price, maxParticipants, creationDate, numFree, date);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Excursion excursion)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Excursion"
                + " SET city = ?, description = ?, date=?, maxParticipants = ?, numFree = ?, "
                + "price = ? WHERE excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;

            preparedStatement.setString(i++, excursion.getCity());
            preparedStatement.setString(i++, excursion.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getDate()));
            preparedStatement.setInt(i++, excursion.getMaxParticipants());
            preparedStatement.setInt(i++, excursion.getNumFree());
            preparedStatement.setFloat(i++, excursion.getPrice());
            preparedStatement.setLong(i++, excursion.getExcursionId());


            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(excursion.getExcursionId(),
                        Excursion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Excursion> findByDates(Connection connection, String city, LocalDateTime earlyDate, LocalDateTime lateDate) {
        /* Validations */

        /* Create "queryString". */
        String queryString = "SELECT excursionId, city, description, "
                + " date, price, creationDate, maxParticipants, numFree FROM Excursion";

            queryString += " WHERE city = ? AND date >= DATE_ADD(CURDATE(), INTERVAL 1 DAY)";
        if (earlyDate != null && lateDate != null) {
            queryString += "AND date >= ? AND date <= ? ";
        }
        queryString += "ORDER BY excursionId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, city);
            if (earlyDate != null) {
                preparedStatement.setTimestamp(i++, Timestamp.valueOf(earlyDate));
            }
            if (lateDate != null) {
                preparedStatement.setTimestamp(i++, Timestamp.valueOf(lateDate));
            }

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read excursions. */
            List<Excursion> excursions = new ArrayList<Excursion>();

            while (resultSet.next()) {

                i = 1;

                Long excursionId = resultSet.getLong(i++);
                String resultCity = String.valueOf(resultSet.getString(i++));
                String description = String.valueOf(resultSet.getString(i++));
                Timestamp dateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime date = dateAsTimestamp.toLocalDateTime();
                float price = resultSet.getFloat(i++);
                Timestamp creationDateAsTimestamp =resultSet.getTimestamp(i++);
                LocalDateTime creationDate =creationDateAsTimestamp.toLocalDateTime();
                int maxParticipants = resultSet.getInt(i++);
                int numFree = resultSet.getInt(i++);

                excursions.add(new Excursion(excursionId, resultCity, description, price, maxParticipants,creationDate, numFree,date));
           }

            /* Return excursions. */
            return excursions;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long excursionId)
            throws InstanceNotFoundException, ExcursionNotModificableException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Excursion WHERE" + " excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId);

            if (reservationDao.existsByExcursionId(connection, excursionId)) {
                throw new ExcursionNotModificableException(excursionId);
            }

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();


            if (removedRows == 0) {
                throw new InstanceNotFoundException(excursionId,
                        Excursion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

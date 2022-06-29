package es.udc.ws.app.model.excursion;

import java.sql.*;

public class Jdbc3CcSqlExcursionDao extends AbstractSqlExcursionDao {

    @Override
    public Excursion create(Connection connection, Excursion excursion) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Excursion"
                + " (city, description, price, creationDate, date, maxParticipants, numFree)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++,excursion.getCity());
            preparedStatement.setString(i++, excursion.getDescription());
            preparedStatement.setFloat(i++, excursion.getPrice());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getCreationDate()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getDate()));
            preparedStatement.setInt(i++, excursion.getMaxParticipants());
            preparedStatement.setInt(i++, excursion.getNumFree());


            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long excursionId = resultSet.getLong(1);

            /* Return excursion. */
            return new Excursion(excursionId, excursion.getCity(),
                    excursion.getDescription(), excursion.getPrice(), excursion.getMaxParticipants(),excursion.getCreationDate()
                    ,excursion.getNumFree(), excursion.getDate());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

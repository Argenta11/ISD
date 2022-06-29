package es.udc.ws.app.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.MAX_PRICE;
import static es.udc.ws.app.model.util.ModelConstants.MAX_PARTICIPANTS;
import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.reservation.SqlReservationDao;
import es.udc.ws.app.model.reservation.SqlReservationDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

public class AppServiceImpl implements AppService {
	/*
	 * IMPORTANT: Some JDBC drivers require "setTransactionIsolation" to be called
	 * before "setAutoCommit".
	 */

	private final DataSource dataSource;
	private SqlExcursionDao excursionDao = null;
	private SqlReservationDao reservationDao = null;

	public AppServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
		excursionDao = SqlExcursionDaoFactory.getDao();
		reservationDao = SqlReservationDaoFactory.getDao();
	}

	private void validateExcursion(Excursion excursion) throws InputValidationException {

		PropertyValidator.validateMandatoryString("city", excursion.getCity());
		PropertyValidator.validateMandatoryString("description", excursion.getDescription());
		PropertyValidator.validateDouble("price", excursion.getPrice(), 0, MAX_PRICE);
		PropertyValidator.validateLong("maxParticipants",excursion.getMaxParticipants(),1,MAX_PARTICIPANTS);
		PropertyValidator.validateNotNegativeLong("maxParticipants",excursion.getMaxParticipants());
		PropertyValidator.validateNotNegativeLong("numFree",excursion.getNumFree());
		PropertyValidator.validateLong("numFree",excursion.getNumFree(),0,MAX_PARTICIPANTS);

		if(excursion.getDate().isBefore(LocalDateTime.now().plusHours(72))){
			throw new InputValidationException("Invalid date value (it must be after 72 hours from now) ");

		}


	}

	@Override
	public Excursion createExcursion(Excursion excursion) throws InputValidationException {

		validateExcursion(excursion);
		excursion.setCreationDate(LocalDateTime.now());
		excursion.setNumFree(excursion.getMaxParticipants());

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Excursion createdExcursion = excursionDao.create(connection, excursion);

				/* Commit. */
				connection.commit();

				return createdExcursion;

			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	public void updateExcursion(Excursion excursion) throws InputValidationException, InstanceNotFoundException,
			ExcursionNotUpdateablePlacesException,ExcursionNotUpdateableDatesException {

		validateExcursion(excursion);

		if(excursion.getDate().compareTo(LocalDateTime.now().withNano(0).plusHours(72)) <=0 ){
			throw new InputValidationException("Invalid date value (it must be after 72 hours from now) ");
		}

		try (Connection connection = dataSource.getConnection()) {
			Excursion oldExcursion = excursionDao.find(connection,excursion.getExcursionId()); //25-08
			excursion.setNumFree(oldExcursion.getNumFree());
			excursion.setCreationDate(oldExcursion.getCreationDate());
			int dif=oldExcursion.getMaxParticipants()-excursion.getMaxParticipants();

			if(excursion.getNumFree()<dif){
				throw new ExcursionNotUpdateablePlacesException(excursion.getExcursionId(),excursion.getNumFree());
			}
			excursion.setNumFree(oldExcursion.getNumFree()-dif);
			excursion.setMaxParticipants(oldExcursion.getMaxParticipants()-dif);
			if(oldExcursion.getDate().isAfter(excursion.getDate())) {
				throw new ExcursionNotUpdateableDatesException(excursion.getExcursionId(), excursion.getDate());
			}
			if(oldExcursion.getDate().isBefore(LocalDateTime.now().withNano(0))){
				throw new ExcursionNotUpdateableDatesException(excursion.getExcursionId(), excursion.getDate());
			}
			try {


				// Prepare connection.
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);



				// Do work.
				excursionDao.update(connection, excursion);

				// Commit.
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public List<Excursion> findExcursions(String city, LocalDateTime earlyDate, LocalDateTime lateDate) throws InputValidationException {

		if (city == null) {
			throw new InputValidationException(Excursion.class.getName());
		}  else if ((earlyDate != null && lateDate == null) || (earlyDate == null && lateDate != null)) {
			throw new InputValidationException(Excursion.class.getName());
		}

		try (Connection connection = dataSource.getConnection()) {
			return excursionDao.findByDates(connection, city, earlyDate, lateDate);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Reservation buyReservation(Long excursionId, String userEmail, String creditCardNumber, int numParticipants)
			throws InstanceNotFoundException, InputValidationException,ReservationOutOfTimeException,
			ReservationNotEnoughPlacesException {

		if (numParticipants < 1 || numParticipants > 5 ) {
			throw new InputValidationException("Invalid participants value (it must be for 1 to 5 participants) ");
		}

		PropertyValidator.validateCreditCard(creditCardNumber);

		try (Connection connection = dataSource.getConnection()) {
			try {

				// Prepare connection.
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				// Do work.
				Excursion excursion = excursionDao.find(connection, excursionId);
				if (excursion.getDate().isBefore(LocalDateTime.now().plusDays(1))) {
					throw new ReservationOutOfTimeException(excursion.getExcursionId(), excursion.getDate());
				}
				if (excursion.getNumFree() < numParticipants) {
					throw new ReservationNotEnoughPlacesException(excursion.getExcursionId(), excursion.getNumFree());
				}
				Reservation reservation = reservationDao.create(connection, new Reservation(excursionId, userEmail,
						creditCardNumber, LocalDateTime.now().withNano(0), numParticipants, excursion.getPrice()));

				// Update excursion
				int newFree = excursion.getNumFree() - numParticipants;
				excursion.setNumFree(newFree);
				excursionDao.update(connection, excursion);

				// Commit.
				connection.commit();


				return reservation;

			} catch (InstanceNotFoundException | ReservationOutOfTimeException | ReservationNotEnoughPlacesException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cancelReservation(Long reservationId, String userEmail) throws InstanceNotFoundException,
			ReservationOutOfTimeException,ReservationCanceledException,ReservationNotSameUserEmailException {


		Reservation reservation = null;
		Excursion excursion = null;

		try (Connection connection = dataSource.getConnection()) {

			try {
				// Prepare connection.
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				reservation = reservationDao.find(connection, reservationId);
				excursion = excursionDao.find(connection, reservation.getExcursionId());


				LocalDateTime fechaInicio = excursion.getDate();
				LocalDateTime fechaActual = LocalDateTime.now().withNano(0);


				if(fechaInicio.isBefore(fechaActual.plusDays(2))){
					throw new ReservationOutOfTimeException(reservationId,fechaActual);
				}
				if(reservation.getCanceled()!=null){
					throw new ReservationCanceledException(reservationId,fechaActual);
				}
				if(!Objects.equals(reservation.getUserEmail(), userEmail)){
					throw new ReservationNotSameUserEmailException(reservationId,userEmail);
				}


				reservation.setCanceled(fechaActual);
				excursion.setNumFree(excursion.getNumFree()+reservation.getNumParticipants());
				excursionDao.update(connection,excursion);
				reservationDao.update(connection,reservation);
				connection.commit();

			} catch (InstanceNotFoundException | ReservationOutOfTimeException | ReservationCanceledException |ReservationNotSameUserEmailException e) {
				connection.commit();
				throw e;
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Reservation> findReservations(String userEmail) {

		try (Connection connection = dataSource.getConnection()) {
			return reservationDao.findByUserEmail(connection, userEmail);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

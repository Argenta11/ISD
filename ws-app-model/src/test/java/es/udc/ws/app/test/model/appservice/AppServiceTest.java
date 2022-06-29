package es.udc.ws.app.test.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.appservice.AppService;
import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.reservation.SqlReservationDao;
import es.udc.ws.app.model.reservation.SqlReservationDaoFactory;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class AppServiceTest {
	
	private final long NON_EXISTENT_EXCURSION_ID = -1;
	private final long NON_EXISTENT_RESERVATION_ID = -1;
	private final String USER_EMAIL = "correo@electronico.com";
	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	public static final Integer NUM_PARTIPANTES = 4;
	private final String INVALID_CREDIT_CARD_NUMBER = "";

	private static AppService appService = null;
	private static SqlExcursionDao excursionDao = null;
	private static SqlReservationDao reservationDao = null;

	@BeforeAll
	public static void init() {

		/*
		 * Create a simple data source and add it to "DataSourceLocator" (this
		 * is needed to test "es.udc.ws.app.model.appservice.appService"
		 */
		DataSource dataSource = new SimpleDataSource();

		/* Add "dataSource" to "DataSourceLocator". */
		DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

		appService = AppServiceFactory.getService();

		excursionDao = SqlExcursionDaoFactory.getDao();

		reservationDao = SqlReservationDaoFactory.getDao();

	}

	private Excursion getValidExcursion(LocalDateTime date, String city) {
		return new Excursion(city, "Excursion description", 18.99F,  (short) 12, (short) 12, date);
	}

	private Excursion getValidExcursion(LocalDateTime date) {
		return getValidExcursion(date, "Excursion city");
	}

	private Excursion createExcursion(Excursion excursion) {

		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
		Excursion addedExcursion = null;

		try (Connection connection = dataSource.getConnection()) {

			try {

				// Prepare connection.
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				// Do work.
				addedExcursion = excursionDao.create(connection, excursion);

				// Commit.
				connection.commit();

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

		return addedExcursion;

	}

	private void removeExcursion(Long excursionId) throws ExcursionNotModificableException {

		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				// Prepare connection.
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				// Do work.
				excursionDao.remove(connection, excursionId);

				// Commit.
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
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

	private void removeReservation(Long reservationId) {

		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				// Prepare connection.
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				// Do work.
				reservationDao.remove(connection, reservationId);

				// Commit.
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
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

	@Test
	public void testAddExcursionAndFindExcursion() throws InputValidationException, InstanceNotFoundException, ExcursionNotModificableException {
		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
		Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(4));
		Excursion addedExcursion = null;

		try {

			try (Connection connection = dataSource.getConnection()) {


				// Create Excursion
				LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

				addedExcursion = appService.createExcursion(excursion);

				LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

				// Find Excursion
				Excursion foundExcursion = excursionDao.find(connection,addedExcursion.getExcursionId());

				assertEquals(addedExcursion, foundExcursion);

				assertEquals(foundExcursion.getCity(),excursion.getCity());
				assertEquals(foundExcursion.getDescription(),excursion.getDescription());
				assertEquals(foundExcursion.getDate(),excursion.getDate());
				assertEquals(foundExcursion.getPrice(),excursion.getPrice());
				assertTrue(foundExcursion.getCreationDate().compareTo(beforeCreationDate) >= 0
						&& (foundExcursion.getCreationDate().compareTo(afterCreationDate) <= 0));
				assertEquals(foundExcursion.getMaxParticipants(),excursion.getMaxParticipants());
				assertEquals(foundExcursion.getNumFree(),excursion.getNumFree());

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Clear Database
			if (addedExcursion!=null) {
				removeExcursion(addedExcursion.getExcursionId());
			}
		}
	}

	@Test
	public void testAddExcursionAndFindExcursionByCity() throws InputValidationException, ExcursionNotModificableException {

		Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(4));
		Excursion addedExcursion = null;

		try {

			// Create Excursion
			LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

			addedExcursion = appService.createExcursion(excursion);

			LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

			// Find Excursion
			List<Excursion> foundExcursions = new ArrayList<Excursion>();
			foundExcursions = appService.findExcursions(addedExcursion.getCity(), LocalDateTime.now().plusDays(2),
					LocalDateTime.now().plusDays(7));
			Excursion foundExcursion = foundExcursions.get(foundExcursions.size() - 1);


			assertEquals(addedExcursion, foundExcursion);

			assertEquals(foundExcursion.getCity(),excursion.getCity());

			assertEquals(foundExcursion.getDescription(),excursion.getDescription());
			assertEquals(foundExcursion.getDate(),excursion.getDate());
			assertEquals(foundExcursion.getPrice(),excursion.getPrice());
			assertTrue(foundExcursion.getCreationDate().compareTo(beforeCreationDate) >= 0
					&& (foundExcursion.getCreationDate().compareTo(afterCreationDate) <= 0));
			assertEquals(foundExcursion.getMaxParticipants(),excursion.getMaxParticipants());
			assertEquals(foundExcursion.getNumFree(),excursion.getNumFree());


		} finally {
			// Clear Database
			if (addedExcursion!=null) {
				removeExcursion(addedExcursion.getExcursionId());
			}
		}
	}

	@Test
	public void testAddInvalidExcursion() {

		// Check excursion city not null
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setCity(null);
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion city not empty
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setCity("");
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion description not null
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setDescription(null);
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion description not null
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setDescription("");
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion price >= 0
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setPrice((short) -1);
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion price <= MAX_PRICE
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setPrice((short) (MAX_PRICE + 1));
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion Maxparticipants >= 0
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setMaxParticipants((short) -1);
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion price <= MAX_PRICE
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setMaxParticipants((short) (MAX_PARTICIPANTS + 1));
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion Numfree >= 0
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setNumFree((short) -1);
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check excursion Numfree <= MAX_PRICE
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(10));
			excursion.setNumFree((short) (MAX_PARTICIPANTS + 1));
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

		// Check
		assertThrows(InputValidationException.class, () -> {
			Excursion excursion = getValidExcursion(LocalDateTime.now().minusDays(1));
			Excursion addedExcursion = appService.createExcursion(excursion);
			removeExcursion(addedExcursion.getExcursionId());
		});

	}

	@Test
	public void testFindNonExistentExcursion() throws SQLException {
		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {
			assertThrows(InstanceNotFoundException.class, () -> excursionDao.find(connection,NON_EXISTENT_EXCURSION_ID));
		}

	}

	@Test
	public void testUpdateExcursion() throws InputValidationException, InstanceNotFoundException
			, ExcursionNotModificableException, SQLException {
		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
		Excursion excursion = createExcursion(getValidExcursion(LocalDateTime.now().plusDays(4)));
		Excursion addedExcursion = null;

		try {

			try (Connection connection = dataSource.getConnection()) {
				addedExcursion = appService.createExcursion(excursion);
				Long oldExcursionId = addedExcursion.getExcursionId();
				Excursion excursionToUpdate = new Excursion(excursion.getExcursionId(), "new city",
						"new description", 15.49F, (short) 80, (short) 80, excursion.getDate());

				excursionDao.update(connection,excursionToUpdate);

				Excursion updatedExcursion = excursionDao.find(connection,excursion.getExcursionId());

				// Delete no longer used excursion
				removeExcursion(oldExcursionId);

				excursionToUpdate.setCreationDate(addedExcursion.getCreationDate());
				assertEquals(excursionToUpdate, updatedExcursion);
			}


		} finally {
			// Clear Database
			removeExcursion(excursion.getExcursionId());
		}
	}



	@Test
	public void testUpdateInvalidExcursion() throws InstanceNotFoundException, SQLException, ExcursionNotModificableException {

			Excursion excursion1 = createExcursion(new Excursion("new city", "new description",
					15.49F, (short) 80, (short) 80, LocalDateTime.now().plusDays(1)));
			Long excursionId = createExcursion(getValidExcursion(LocalDateTime.now().plusDays(4))).getExcursionId();
			DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

			try {

				try (Connection connection = dataSource.getConnection()) {
					excursion1.setDate(LocalDateTime.now());
					assertThrows(InputValidationException.class, () -> appService.updateExcursion(excursion1));
					excursion1.setDate(LocalDateTime.now().plusDays(5));
					excursion1.setNumFree(10);
					excursionDao.update(connection,excursion1);
					excursion1.setMaxParticipants(excursion1.getMaxParticipants() -20 );
					assertThrows(ExcursionNotUpdateablePlacesException.class, () -> appService.updateExcursion(excursion1));
					// Check excursion city not null
					Excursion excursion = excursionDao.find(connection,excursionId);
					excursion.setCity(null);
					assertThrows(InputValidationException.class, () -> appService.updateExcursion(excursion));
				}

			} finally {
				// Clear Database
				removeExcursion(excursionId);
				removeExcursion(excursion1.getExcursionId());
			}

	}

	@Test
	public void testUpdateNonExistentExcursion() throws ExcursionNotModificableException {

		Excursion excursion = createExcursion(getValidExcursion(LocalDateTime.now().plusDays(4)));
		Long oldExcursionId = excursion.getExcursionId();
		excursion.setExcursionId(NON_EXISTENT_EXCURSION_ID);
		excursion.setCreationDate(LocalDateTime.now());

		assertThrows(InstanceNotFoundException.class, () -> appService.updateExcursion(excursion));

		// Remove excursion
		removeExcursion(oldExcursionId);

	}

	@Test
	public void testRemoveExcursion() throws InstanceNotFoundException, ExcursionNotModificableException, SQLException {

		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {

			Excursion excursion = createExcursion(getValidExcursion(LocalDateTime.now().plusDays(4)));

			excursionDao.remove(connection,excursion.getExcursionId());

			assertThrows(InstanceNotFoundException.class, () -> excursionDao.find(connection,excursion.getExcursionId()));

		}


	}

	@Test
	public void testRemoveNonExistentExcursion() throws SQLException {
		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			assertThrows(InstanceNotFoundException.class, () -> excursionDao.remove(connection,NON_EXISTENT_EXCURSION_ID));
		}
	}

	@Test
	public void testRemoveExcursionWithReservations()
			throws InputValidationException, InstanceNotFoundException, ReservationNotPossibleException
			, ReservationOutOfTimeException, ReservationNotEnoughPlacesException, ExcursionNotModificableException {

		Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(4));
		Excursion addedExcursion = null;
		Reservation reservation = null;
		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);


		try {
			try (Connection connection = dataSource.getConnection()) {
				addedExcursion = appService.createExcursion(excursion);
				Long excursionId = addedExcursion.getExcursionId();
				reservation = appService.buyReservation(excursionId, USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);

				assertThrows(ExcursionNotModificableException.class, () -> excursionDao.remove(connection,excursionId));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Clear Database (reservation if it was created and excursion)
			if (reservation != null) {
				removeReservation(reservation.getReservationId());
			}
			removeExcursion(addedExcursion.getExcursionId());
		}
	}

	@Test
	public void testFindExcursions() throws ExcursionNotModificableException {

		// Add excursions
		List<Excursion> excursions = new LinkedList<Excursion>();
		Excursion excursion1 = createExcursion(new Excursion("City Test 1", "Excursion description",
				18.99F,  (short) 12, (short) 12, LocalDateTime.now().plusDays(4)));
		excursions.add(excursion1);
		Excursion excursion2 = createExcursion(new Excursion("City 2", "Excursion description2",
				18.99F,  (short) 12, (short) 12, LocalDateTime.now().plusDays(4)));
		excursions.add(excursion2);
		Excursion excursion3 = createExcursion(new Excursion("City Test 3", "Excursion description3",
				18.99F,  (short) 12, (short) 12, LocalDateTime.now().plusDays(4)));
		excursions.add(excursion3);

		try {
			List<Excursion> foundExcursions = appService.findExcursions("City Test 1", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(6));
			assertEquals(1, foundExcursions.size());
			assertEquals(excursions.get(0), foundExcursions.get(0));

			assertThrows(InputValidationException.class, () -> appService.findExcursions("City Test 1", LocalDateTime.now().plusDays(2), null));

			assertThrows(InputValidationException.class, () -> appService.findExcursions("City Test 1", null, LocalDateTime.now().plusDays(2)));

			foundExcursions = appService.findExcursions("City 2", null, null);
			assertEquals(1, foundExcursions.size());
			assertEquals(excursions.get(1), foundExcursions.get(0));

			foundExcursions = appService.findExcursions("City 33", null, null);
			assertEquals(0, foundExcursions.size());
		} catch (InputValidationException e) {
			e.printStackTrace();
		} finally {
			// Clear Database
			for (Excursion excursion : excursions) {
				removeExcursion(excursion.getExcursionId());
			}
		}
	}

	@Test
	public void testBuyExcursionAndFindReservation() throws InstanceNotFoundException, InputValidationException,
			ReservationNotPossibleException, ExcursionNotUpdateablePlacesException, ReservationOutOfTimeException,
			ReservationNotEnoughPlacesException, SQLException, ExcursionNotModificableException {

			Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(4));
			Excursion addedExcursion = null;
			Reservation reservation = null;
			DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

			try {

				try (Connection connection = dataSource.getConnection()) {
					// Create Excursion
					LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);
					addedExcursion = appService.createExcursion(excursion);
					LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

					// Buy excursion
					LocalDateTime beforeBuyDate = LocalDateTime.now().withNano(0);
					reservation = appService.buyReservation(addedExcursion.getExcursionId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
					LocalDateTime afterBuyDate = LocalDateTime.now().withNano(0);

					// Find reservation
					Reservation foundReservation = reservationDao.find(connection,reservation.getReservationId());

					// Check reservation
					assertEquals(reservation, foundReservation);
					assertEquals(VALID_CREDIT_CARD_NUMBER, foundReservation.getCreditCardNumber());
					assertEquals(USER_EMAIL, foundReservation.getUserEmail());
					assertEquals(addedExcursion.getExcursionId(), foundReservation.getExcursionId());
					assertEquals(addedExcursion.getPrice(), foundReservation.getPrice());
					assertTrue((foundReservation.getRegisterDate().compareTo(beforeBuyDate) >= 0)
							&& (foundReservation.getRegisterDate().compareTo(afterBuyDate) <= 0));

				}

			} finally {
				// Clear database: remove reservation (if created) and excursion
				if (reservation != null) {
					removeReservation(reservation.getReservationId());
				}
				removeExcursion(addedExcursion.getExcursionId());
			}
	}

	@Test
	public void testBuyExcursionOutOfTime() throws ExcursionNotModificableException {

			Excursion excursion1= createExcursion(new Excursion("new city", "new description",
					15.49F, (short) 80, (short) 80, LocalDateTime.now().plusHours(15)));

			try {

				// Try to buy excursion
				assertThrows(ReservationOutOfTimeException.class, () -> appService.buyReservation(excursion1.getExcursionId(),
						USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES));

			} finally {
				// Clear database: remove excursion
				removeExcursion(excursion1.getExcursionId());
			}
	}

	@Test
	public void testBuyExcursionWithInvalidCreditCard() throws  InputValidationException,  ExcursionNotModificableException {

		Excursion excursion = getValidExcursion(LocalDateTime.now().plusDays(4));
		Excursion addedExcursion = null;

		try {
			addedExcursion = appService.createExcursion(excursion);
			Long excursionId = addedExcursion.getExcursionId();
			assertThrows(InputValidationException.class, () -> {
				Reservation reservation = appService.buyReservation(excursionId, USER_EMAIL, INVALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
				removeReservation(reservation.getReservationId());
			});
		} finally {
			// Clear database
			removeExcursion(addedExcursion.getExcursionId());

		}

	}

	@Test
	public void testBuyNonExistentExcursion() {

		assertThrows(InstanceNotFoundException.class, () -> {
			Reservation reservation = appService.buyReservation(NON_EXISTENT_EXCURSION_ID, USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
			removeReservation(reservation.getReservationId());
		});

	}

	@Test
	public void testFindNonExistentReservation() throws SQLException {
		DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			assertThrows(InstanceNotFoundException.class, () -> reservationDao.find(connection,NON_EXISTENT_RESERVATION_ID));
		}

	}

	@Test
	public void testCanceledReservation() throws ReservationNotPossibleException, InstanceNotFoundException,
			InputValidationException, ExcursionNotModificableException, ReservationOutOfTimeException,
			ReservationNotEnoughPlacesException, ReservationCanceledException, SQLException,ReservationNotSameUserEmailException {
			DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

			Excursion excursion = createExcursion(new Excursion("City Test 1", "Excursion description",
					18.99F, (short) 12, (short) 12, LocalDateTime.now().plusDays(4)));

			Excursion excursion1= createExcursion(new Excursion("new city", "new description",
					15.49F, (short) 80, (short) 80, LocalDateTime.now().plusHours(15)));

            int freePlaces = excursion1.getNumFree();

			try (Connection connection = dataSource.getConnection()) {
				assertThrows(ReservationOutOfTimeException.class, () ->  appService.buyReservation(excursion1.getExcursionId(),
						USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES));

				Reservation reservation = appService.buyReservation(excursion.getExcursionId(), USER_EMAIL,
						VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
				Long resId = reservation.getReservationId();

				assertThrows(InstanceNotFoundException.class, () -> appService.cancelReservation(NON_EXISTENT_RESERVATION_ID,
						USER_EMAIL));

				appService.cancelReservation(resId, USER_EMAIL);
				// Check Free Plazas y hay fecha de cancelación
				assertEquals(excursionDao.find(connection,excursion1.getExcursionId()).getNumFree(), freePlaces);
				assertTrue(reservationDao.find(connection,resId).getCanceled() != null);
				assertThrows(ReservationCanceledException.class, () -> appService.cancelReservation(resId, USER_EMAIL));

				// Clear database: remove reservation (if created) and excursion
				removeReservation(reservation.getReservationId());
				removeExcursion(excursion.getExcursionId());
				removeExcursion(excursion1.getExcursionId());
			}
	}

	@Test
	public void testGetAllExcursions() throws InstanceNotFoundException, InputValidationException, ReservationNotPossibleException, ExcursionNotModificableException, ReservationOutOfTimeException, ReservationNotEnoughPlacesException {

		// Add excursions
		List<Reservation> reservations = new LinkedList<Reservation>();
		Excursion excursion1 = createExcursion(new Excursion("City Test 1", "Excursion description",
				18.99F,  (short) 12, (short) 12, LocalDateTime.now().plusDays(4)));
		Excursion excursion2 = createExcursion(new Excursion("City 2", "Excursion description",
				18.99F,  (short) 12, (short) 12, LocalDateTime.now().plusDays(4)));

		try {

			Reservation reservation1 = appService.buyReservation(excursion1.getExcursionId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
			reservations.add(reservation1);
			Reservation reservation2 = appService.buyReservation(excursion2.getExcursionId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
			reservations.add(reservation2);
			Reservation reservation3 = appService.buyReservation(excursion2.getExcursionId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
			reservations.add(reservation3);
			Reservation reservation4 = appService.buyReservation(excursion1.getExcursionId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_PARTIPANTES);
			reservations.add(reservation4);

			List<Reservation> foundReservations = appService.findReservations(USER_EMAIL);
			assertEquals(4, foundReservations.size());
			assertEquals(reservations.get(0), foundReservations.get(0));
			assertEquals(reservations.get(1), foundReservations.get(1));

			foundReservations = appService.findReservations("Email mal");
			assertEquals(0, foundReservations.size());
		} finally {
			// Clear Database
			for (Reservation reservation : reservations) {
				removeReservation(reservation.getReservationId());
			}
			removeExcursion(excursion1.getExcursionId());
			removeExcursion(excursion2.getExcursionId());
		}
	}

}

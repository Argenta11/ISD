-- ----------------------------------------------------------------------------
-- Excursion Model
-------------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Excursion;

-- ---------------------------------- Excursion -------------------------------------
CREATE TABLE Excursion ( excursionId BIGINT NOT NULL AUTO_INCREMENT,
    city VARCHAR(100) COLLATE latin1_bin NOT NULL,
    description VARCHAR(1024) COLLATE latin1_bin NOT NULL,
	date DATETIME NOT NULL,
    price FLOAT NOT NULL,
    creationDate DATETIME NOT NULL,
	maxParticipants SMALLINT NOT NULL,
	numFree SMALLINT,
    CONSTRAINT ExcursionPK PRIMARY KEY(excursionId),
    CONSTRAINT validPrice2 CHECK ( price >= 0.0 AND price <= 199.9 ),
	CONSTRAINT validNumFree CHECK ( numFree >=0 AND numFree <= maxParticipants )
	) ENGINE = InnoDB;

-- --------------------------------- Reservation ------------------------------------

CREATE TABLE Reservation ( reservationId BIGINT NOT NULL AUTO_INCREMENT,
    excursionId BIGINT NOT NULL,
    userEmail VARCHAR(40) COLLATE latin1_bin NOT NULL,
	creditCardNumber VARCHAR(16),
    registerDate DATETIME NOT NULL,
    numParticipants SMALLINT NOT NULL,
    price FLOAT NOT NULL,
    canceled DATETIME DEFAULT NULL,
    CONSTRAINT ReservationPK PRIMARY KEY(reservationId),
    CONSTRAINT ReservationExcursionIdFK FOREIGN KEY(excursionId)
        REFERENCES Excursion(excursionId) ON DELETE CASCADE ) ENGINE = InnoDB;

package app.daos;


import app.domains.Reservation;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReservationDaoImplTest extends BaseDaoTest {

    @Autowired
    private ReservationDao reservationDao;

    private Reservation reservation;
    private String propertyId = "propertyID";
    private String userId = "userID";

    @BeforeEach
    void setup() {
        prepareDB();

        // Initializing test reservation
        reservation = getNewReservation();

        // Creating reservation
        reservationDao.create(reservation);
    }

    @AfterEach
    void tearDown() {
        clearTables("Reservations", "Users", "Property_infos");
    }

    @Test
    void testCreate_successFlow() {
        // Initializing test reservation
        Reservation reservation = getNewReservation();

        // Testing
        Reservation resultReservation = reservationDao.create(reservation);

        // Asserting results
        assertEquals(reservation, resultReservation);
        assertNotNull(resultReservation.getId());

        Reservation reservationFromDB = reservationDao.findById(reservation.getId());
        assertThat(reservationFromDB).isEqualToIgnoringGivenFields(resultReservation,
                "createdDate", "updatedDate");
    }

    @Test
    void testCreate_omittedNotNullFields() {
        Reservation emptyReservation = new Reservation();
        assertThrows(DbException.class, () -> reservationDao.create(emptyReservation));
    }

    @Test
    void testFindById_successFlow() {
        Reservation resultReservation = reservationDao.findById(reservation.getId());
        assertThat(resultReservation).isEqualToIgnoringGivenFields(reservation,
                "createdDate", "updatedDate");
    }

    @Test
    void testFindById_incorrectId() {
        assertThrows(NotFoundException.class, () -> reservationDao.findById("someRandomString"));
    }

    @Test
    void testUpdate_successFlow() {
        // Initializing reservation
        Reservation updatedReservation = getNewReservation();
        updatedReservation.setId(reservation.getId());

        // Testing
        Reservation resultReservation = reservationDao.update(updatedReservation);

        // Asserting results
        assertSame(updatedReservation, resultReservation);

        Reservation reservationFromDB = reservationDao.findById(reservation.getId());
        assertThat(reservationFromDB).isEqualToIgnoringGivenFields(resultReservation,
                "createdDate", "updatedDate");
    }

    @Test
    void testUpdate_incorrectId() {
        reservation.setId("someRandomString");
        assertThrows(NotFoundException.class, () -> reservationDao.update(reservation));
    }

    @Test
    void testUpdate_omittedNotNullFields() {
        Reservation emptyReservation = new Reservation();
        emptyReservation.setId(reservation.getId());
        assertThrows(DbException.class, () -> reservationDao.update(emptyReservation));
    }

    @Test
    void testDelete_successFlow() {
        assertTrue(reservationDao.delete(reservation.getId()));
        assertThrows(NotFoundException.class, () -> reservationDao.findById(reservation.getId()));
    }

    @Test
    void testDelete_incorrectId() {
        // Deleting not existing reservation from db
        assertThrows(NotFoundException.class, () -> reservationDao.delete("randomString"));
    }

    private Reservation getNewReservation() {
        Reservation reservation = new Reservation();
        reservation.setPropertyId(propertyId);
        reservation.setUserId(userId);
        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckOutDate(LocalDate.now().plusDays(7));
        return reservation;
    }

    private void prepareDB() {
        // insert user
        template.update("INSERT INTO Users ( id, email, password, surname, name, phone_number," +
                " active, created_date, updated_date) VALUES ('" + userId + "', 'john@john.com', " +
                "'1234', 'Sonmez', 'John', '0955456465', 1, '2002-09-24-06:00', '2002-09-24-06:00');");

        // insert property
        template.update("INSERT INTO Property_infos(id, owner_id, location, description, name, created_date," +
                " updated_date) VALUES ('" + propertyId + "', 'owner_id', 'somewhere', 'pretty good house', 'house', " +
                "'2002-09-24-06:00', '2002-09-24-06:00');");
    }

}
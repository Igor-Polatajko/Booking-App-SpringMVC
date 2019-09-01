package app.daos;

import app.domains.Reservation;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import app.mappers.ReservationMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class ReservationDaoImpl implements ReservationDao {

    private JdbcTemplate jdbcTemplate;
    private ReservationMapper mapper;
    private Logger logger;

    @Autowired
    public ReservationDaoImpl(JdbcTemplate jdbcTemplate, ReservationMapper mapper, Logger logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.logger = logger;
    }

    @Override
    public Reservation create(Reservation reservation) {
        try {
            reservation.setId(UUID.randomUUID().toString());

            LocalDateTime currentDate = LocalDateTime.now();
            reservation.setCreatedDate(currentDate);
            reservation.setUpdatedDate(currentDate);

            jdbcTemplate.update(
                    Queries.SQL_INSERT_RESERVATION,
                    reservation.getId(),
                    reservation.getPropertyId(),
                    reservation.getUserId(),
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    currentDate,
                    currentDate
            );

            return reservation;
        }
        catch (Exception e) {
            logger.warn("Exception while creating new reservation. Message: {}.", e.getMessage());
            throw new DbException("Create reservation exception");
        }
    }

    @Override
    public Reservation findById(String id) {
        try {
            return jdbcTemplate.queryForObject(Queries.SQL_SELECT_RESERVATION_BY_ID, mapper, id);
        }
        catch (EmptyResultDataAccessException ex) {
            throw getAndLogReservationNotFoundException(id);
        }
        catch (Exception e) {
            logger.warn("Exception while finding reservation by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Find reservation by id exception");
        }
    }

    @Override
    public Reservation update(Reservation reservation) {
        int rowsAffected;
        try {
            LocalDateTime updatedDate = LocalDateTime.now();
            rowsAffected = jdbcTemplate.update(
                    Queries.SQL_UPDATE_RESERVATION,
                    reservation.getPropertyId(),
                    reservation.getUserId(),
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    updatedDate,
                    reservation.getId());

            reservation.setUpdatedDate(updatedDate);
        }
        catch (Exception e) {
            logger.warn("Exception while updating reservation with id = {}. Message: {}.", reservation.getId(), e.getMessage());
            throw new DbException("Update reservation exception");
        }
        if (rowsAffected < 1) {
            throw getAndLogReservationNotFoundException(reservation.getId());
        }
        return reservation;
    }

    @Override
    public boolean delete(String id) {
        int rowsAffected;
        try {
            rowsAffected = jdbcTemplate.update(Queries.SQL_DELETE_RESERVATION, id);
        }
        catch (Exception e) {
            logger.warn("Exception while deleting reservation by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Delete reservation exception");
        }
        if (rowsAffected < 1) {
            throw getAndLogReservationNotFoundException(id);
        }
        return true;
    }

    private NotFoundException getAndLogReservationNotFoundException(String id) {
        NotFoundException notFoundException = new NotFoundException("Reservation not found");
        logger.warn("Runtime exception. Reservation not found (id = {}). Message: {}",
                id, notFoundException.getMessage());

        return notFoundException;
    }

    private class Queries {
        private static final String SQL_INSERT_RESERVATION = "INSERT INTO Reservations" +
                "(id, property_id, user_id, check_in_date, check_out_date, created_date, updated_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        private static final String SQL_SELECT_RESERVATION_BY_ID = "SELECT * FROM Reservations WHERE id = ?";

        private static final String SQL_UPDATE_RESERVATION = "UPDATE Reservations SET property_id = ?, " +
                "user_id = ?, check_in_date = ?, check_out_date = ?, updated_date = ? WHERE id = ?";

        private static final String SQL_DELETE_RESERVATION = "DELETE FROM Reservations WHERE id = ?";
    }
}

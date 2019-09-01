package app.daos;

import app.domains.Reservation;

public interface ReservationDao {
    Reservation create(Reservation reservation);

    Reservation findById(String id);

    Reservation update(Reservation reservation);

    boolean delete(String id);
}

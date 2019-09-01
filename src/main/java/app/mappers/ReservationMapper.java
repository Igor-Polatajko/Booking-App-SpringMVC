package app.mappers;

import app.domains.Reservation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ReservationMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet resultSet, int i) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getString("id"));
        reservation.setPropertyId(resultSet.getString("property_id"));
        reservation.setUserId(resultSet.getString("user_id"));
        reservation.setCheckInDate(resultSet.getObject("check_in_date", LocalDate.class));
        reservation.setCheckOutDate(resultSet.getObject("check_out_date", LocalDate.class));
        reservation.setCreatedDate(resultSet.getObject("created_date", LocalDateTime.class));
        reservation.setUpdatedDate(resultSet.getObject("updated_date", LocalDateTime.class));
        return reservation;
    }
}

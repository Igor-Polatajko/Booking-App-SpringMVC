package app.mappers;

import app.domains.UserRole;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class UserRoleMapper implements RowMapper<UserRole> {

    @Override
    public UserRole mapRow(ResultSet resultSet, int i) throws SQLException {
        UserRole user = new UserRole();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setCreatedDate(resultSet.getObject("created_date", LocalDateTime.class));
        user.setUpdatedDate(resultSet.getObject("updated_date", LocalDateTime.class));
        return user;
    }
}

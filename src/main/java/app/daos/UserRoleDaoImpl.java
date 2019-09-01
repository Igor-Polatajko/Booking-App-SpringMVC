package app.daos;

import app.domains.UserRole;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import app.mappers.UserRoleMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {

    private JdbcTemplate jdbcTemplate;
    private UserRoleMapper mapper;
    private Logger logger;

    @Autowired
    public UserRoleDaoImpl(JdbcTemplate jdbcTemplate, UserRoleMapper mapper, Logger logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.logger = logger;
    }


    @Override
    public UserRole create(UserRole userRole) {
        try {
            userRole.setId(UUID.randomUUID().toString());

            LocalDateTime currentDate = LocalDateTime.now();
            userRole.setCreatedDate(currentDate);
            userRole.setUpdatedDate(currentDate);

            jdbcTemplate.update(
                    Queries.SQL_INSERT_USERROLE,
                    userRole.getId(),
                    userRole.getName(),
                    currentDate,
                    currentDate
            );

            return userRole;
        }
        catch (Exception e) {
            logger.warn("Exception while creating new userRole. Message: {}.", e.getMessage());
            throw new DbException("Create userRole exception");
        }
    }

    @Override
    public UserRole findById(String id) {
        try {
            return jdbcTemplate.queryForObject(Queries.SQL_SELECT_USERROLE_BY_ID, mapper, id);
        }
        catch (EmptyResultDataAccessException ex) {
            throw getAndLogUserRoleNotFoundException(id);
        }
        catch (Exception e) {
            logger.warn("Exception while finding userRole by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Find userRole by id exception");
        }
    }

    @Override
    public UserRole update(UserRole userRole) {
        int rowsAffected;
        try {
            LocalDateTime updatedDate = LocalDateTime.now();
            rowsAffected = jdbcTemplate.update(
                    Queries.SQL_UPDATE_USERROLE,
                    userRole.getName(),
                    updatedDate,
                    userRole.getId());

            userRole.setUpdatedDate(updatedDate);
        }
        catch (Exception e) {
            logger.warn("Exception while updating userRole with id = {}. Message: {}.", userRole.getId(), e.getMessage());
            throw new DbException("Update userRole exception");
        }
        if (rowsAffected < 1) {
            throw getAndLogUserRoleNotFoundException(userRole.getId());
        }
        return userRole;
    }

    @Override
    public boolean delete(String id) {
        int rowsAffected;
        try {
            rowsAffected = jdbcTemplate.update(Queries.SQL_DELETE_USERROLE, id);
        }
        catch (Exception e) {
            logger.warn("Exception while deleting userRole by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Delete userRole exception");
        }

        if (rowsAffected < 1) {
            throw getAndLogUserRoleNotFoundException(id);
        }

        return true;
    }

    private NotFoundException getAndLogUserRoleNotFoundException(String id) {
        NotFoundException notFoundException = new NotFoundException("UserRole not found");
        logger.warn("Runtime exception. UserRole not found (id = {}). Message: {}",
                id, notFoundException.getMessage());

        return notFoundException;
    }

    private class Queries {
        private static final String SQL_INSERT_USERROLE = "INSERT INTO User_roles" +
                "(id, name, created_date, updated_date) VALUES (?, ?, ?, ?)";

        private static final String SQL_SELECT_USERROLE_BY_ID = "SELECT * FROM User_roles WHERE id = ?";

        private static final String SQL_UPDATE_USERROLE = "UPDATE User_roles SET name = ?, " +
                "updated_date = ? WHERE id = ?";

        private static final String SQL_DELETE_USERROLE = "DELETE FROM User_roles WHERE id = ?";
    }
}

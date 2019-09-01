package app.daos;

import app.domains.User;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import app.mappers.UserMapper;
import app.utils.EncryptionUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private UserMapper mapper;
    private EncryptionUtil encryptionUtil;
    private Logger logger;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, UserMapper mapper, EncryptionUtil encryptionUtil, Logger logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.encryptionUtil = encryptionUtil;
        this.logger = logger;
    }

    @Override
    public User create(User user) {
        try {
            user.setId(UUID.randomUUID().toString());

            LocalDateTime currentDate = LocalDateTime.now();
            user.setCreatedDate(currentDate);
            user.setUpdatedDate(currentDate);

            String encryptedPassword = encryptionUtil.encode(user.getPassword());

            jdbcTemplate.update(
                    Queries.SQL_INSERT_USER,
                    user.getId(),
                    user.getEmail(),
                    encryptedPassword,
                    user.getSurname(),
                    user.getName(),
                    user.getPhoneNumber(),
                    user.isActive(),
                    currentDate,
                    currentDate
            );
        }
        catch (Exception e) {
            logger.warn("Exception while creating new user. Message: {}.", e.getMessage());
            throw new DbException("Create user exception");
        }
        return user;
    }

    @Override
    public User findById(String id) {
        try {
            return jdbcTemplate.queryForObject(Queries.SQL_SELECT_USER_BY_ID, mapper, id);
        }
        catch (EmptyResultDataAccessException ex) {
            throw getAndLogUserNotFoundException(id);
        }
        catch (Exception e) {
            logger.warn("Exception while finding user by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Find user by id exception");
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(Queries.SQL_SELECT_USER_BY_EMAIL, mapper, email);
        }
        catch (EmptyResultDataAccessException ex) {
            NotFoundException notFoundException = new NotFoundException("User not found");
            logger.warn("Runtime exception. User not found (email = {}). Message: {}.",
                    email, notFoundException.getMessage());

            throw notFoundException;
        }
        catch (Exception e) {
            logger.warn("Exception while finding user by id = {}. Message: {}.", email, e.getMessage());
            throw new DbException("Find user by email exception");
        }
    }

    @Override
    public User update(User user) {
        int rowsAffected;
        try {
            LocalDateTime updatedDate = LocalDateTime.now();
            rowsAffected = jdbcTemplate.update(
                    Queries.SQL_UPDATE_USER,
                    user.getEmail(),
                    user.getSurname(),
                    user.getName(),
                    user.getPhoneNumber(),
                    user.isActive(),
                    updatedDate,
                    user.getId());

            user.setUpdatedDate(updatedDate);
        }
        catch (Exception e) {
            logger.warn("Exception while updating user with id = {}. Message: {}.", user.getId(), e.getMessage());
            throw new DbException("Update user exception");
        }
        if (rowsAffected < 1) {
            throw getAndLogUserNotFoundException(user.getId());
        }
        return user;
    }

    @Override
    public boolean delete(String id) {
        int rowsAffected;
        try {
            rowsAffected = jdbcTemplate.update(Queries.SQL_DELETE_USER, id);
        }
        catch (Exception e) {
            logger.warn("Exception while deleting user by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Delete user exception");
        }

        if (rowsAffected < 1) {
            throw getAndLogUserNotFoundException(id);
        }

        return true;
    }

    private NotFoundException getAndLogUserNotFoundException(String id) {
        NotFoundException notFoundException = new NotFoundException("User not found");
        logger.warn("Runtime exception. User not found (id = {}). Message: {}",
                id, notFoundException.getMessage());

        return notFoundException;
    }

    private class Queries {
        private static final String SQL_INSERT_USER = "INSERT INTO Users(id, email, password, surname, " +
                "name, phone_number, active, created_date, updated_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        private static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM Users WHERE id = ?";
        private static final String SQL_SELECT_USER_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
        private static final String SQL_UPDATE_USER = "UPDATE Users SET email = ?, surname = ?," +
                " name = ?, phone_number = ?, active = ?, updated_date = ? WHERE id = ?";
        private static final String SQL_DELETE_USER = "DELETE FROM Users WHERE id = ?";
    }
}

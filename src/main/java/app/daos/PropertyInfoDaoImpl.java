package app.daos;

import app.domains.PropertyInfo;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import app.mappers.PropertyInfoMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class PropertyInfoDaoImpl implements PropertyInfoDao {

    private JdbcTemplate jdbcTemplate;
    private PropertyInfoMapper mapper;
    private Logger logger;

    @Autowired
    public PropertyInfoDaoImpl(JdbcTemplate jdbcTemplate, PropertyInfoMapper mapper, Logger logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.logger = logger;
    }

    @Override
    public PropertyInfo create(PropertyInfo propertyInfo) {
        try {
            propertyInfo.setId(UUID.randomUUID().toString());

            LocalDateTime currentDate = LocalDateTime.now();
            propertyInfo.setCreatedDate(currentDate);
            propertyInfo.setUpdatedDate(currentDate);

            jdbcTemplate.update(
                    Queries.SQL_INSERT_PROPERTY_INFO,
                    propertyInfo.getId(),
                    propertyInfo.getOwnerId(),
                    propertyInfo.getLocation(),
                    propertyInfo.getDescription(),
                    propertyInfo.getName(),
                    currentDate,
                    currentDate
            );

            return propertyInfo;
        }
        catch (Exception e) {
            logger.warn("Exception while creating new propertyInfo. Message: {}.", e.getMessage());
            throw new DbException("Create propertyInfo exception");
        }
    }

    @Override
    public PropertyInfo findById(String id) {
        try {
            return jdbcTemplate.queryForObject(Queries.SQL_SELECT_PROPERTY_INFO_BY_ID, mapper, id);
        }
        catch (EmptyResultDataAccessException ex) {
            throw getAndLogPropertyInfoNotFoundException(id);
        }
        catch (Exception e) {
            logger.warn("Exception while finding propertyInfo by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Find propertyInfo by id exception");
        }
    }

    @Override
    public PropertyInfo update(PropertyInfo propertyInfo) {
        int rowsAffected;
        try {
            LocalDateTime updatedDate = LocalDateTime.now();
            rowsAffected = jdbcTemplate.update(
                    Queries.SQL_UPDATE_PROPERTY_INFO,
                    propertyInfo.getOwnerId(),
                    propertyInfo.getLocation(),
                    propertyInfo.getDescription(),
                    propertyInfo.getName(),
                    updatedDate,
                    propertyInfo.getId());

            propertyInfo.setUpdatedDate(updatedDate);
        }
        catch (Exception e) {
            logger.warn("Exception while updating propertyInfo with id = {}. Message: {}.", propertyInfo.getId(), e.getMessage());
            throw new DbException("Update propertyInfo exception");
        }
        if (rowsAffected < 1) {
            throw getAndLogPropertyInfoNotFoundException(propertyInfo.getId());
        }
        return propertyInfo;
    }

    @Override
    public boolean delete(String id) {
        int rowsAffected;
        try {
            rowsAffected = jdbcTemplate.update(Queries.SQL_DELETE_PROPERTY_INFO, id);
        }
        catch (Exception e) {
            logger.warn("Exception while deleting propertyInfo by id = {}. Message: {}.", id, e.getMessage());
            throw new DbException("Delete propertyInfo exception");
        }
        if (rowsAffected < 1) {
            throw getAndLogPropertyInfoNotFoundException(id);
        }
        return true;
    }

    private NotFoundException getAndLogPropertyInfoNotFoundException(String id) {
        NotFoundException notFoundException = new NotFoundException("PropertyInfo not found");
        logger.warn("Runtime exception. PropertyInfo not found (id = {}). Message: {}",
                id, notFoundException.getMessage());

        return notFoundException;
    }

    private class Queries {
        private static final String SQL_INSERT_PROPERTY_INFO = "INSERT INTO Property_infos(id, owner_id, location, description, " +
                "name, created_date, updated_date) VALUES(?, ?, ?, ?, ?, ?, ?)";
        private static final String SQL_SELECT_PROPERTY_INFO_BY_ID = "SELECT * FROM Property_infos WHERE id = ?";
        private static final String SQL_UPDATE_PROPERTY_INFO = "UPDATE Property_infos SET owner_id = ?, location = ?," +
                "description = ?, name = ?, updated_date = ? WHERE id = ?";
        private static final String SQL_DELETE_PROPERTY_INFO = "DELETE FROM Property_infos WHERE id = ?";
    }
}

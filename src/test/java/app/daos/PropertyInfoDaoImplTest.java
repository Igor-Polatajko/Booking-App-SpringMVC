package app.daos;


import app.domains.PropertyInfo;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PropertyInfoDaoImplTest extends BaseDaoTest {

    @Autowired
    private PropertyInfoDao propertyInfoDao;

    private PropertyInfo propertyInfo;
    private String ownerId = "ownerID";

    @BeforeEach
    void setup() {
        prepareDB();

        // Initializing test propertyInfo
        propertyInfo = getNewPropertyInfo();

        // Creating propertyInfo
        propertyInfoDao.create(propertyInfo);
    }

    @AfterEach
    void tearDown() {
        clearTables("Users", "Property_infos");
    }

    @Test
    void testCreate_successFlow() {
        // Initializing test propertyInfo
        PropertyInfo propertyInfo = getNewPropertyInfo();
        ;

        // Testing
        PropertyInfo resultPropertyInfo = propertyInfoDao.create(propertyInfo);

        // Asserting results
        assertEquals(propertyInfo, resultPropertyInfo);
        assertNotNull(resultPropertyInfo.getId());

        PropertyInfo propertyInfoFromDB = propertyInfoDao.findById(propertyInfo.getId());
        assertThat(propertyInfoFromDB).isEqualToIgnoringGivenFields(resultPropertyInfo,
                "createdDate", "updatedDate");
    }

    @Test
    void testCreate_omittedNotNullFields() {
        PropertyInfo emptyPropertyInfo = new PropertyInfo();
        assertThrows(DbException.class, () -> propertyInfoDao.create(emptyPropertyInfo));
    }

    @Test
    void testFindById_successFlow() {
        PropertyInfo resultPropertyInfo = propertyInfoDao.findById(propertyInfo.getId());
        assertThat(resultPropertyInfo).isEqualToIgnoringGivenFields(propertyInfo,
                "createdDate", "updatedDate");
    }

    @Test
    void testFindById_incorrectId() {
        assertThrows(NotFoundException.class, () -> propertyInfoDao.findById("someRandomString"));
    }

    @Test
    void testUpdate_successFlow() {
        // Initializing propertyInfo
        PropertyInfo updatedPropertyInfo = getNewPropertyInfo();
        updatedPropertyInfo.setId(propertyInfo.getId());

        // Testing
        PropertyInfo resultPropertyInfo = propertyInfoDao.update(updatedPropertyInfo);

        // Asserting results
        assertSame(updatedPropertyInfo, resultPropertyInfo);

        PropertyInfo propertyInfoFromDB = propertyInfoDao.findById(propertyInfo.getId());
        assertThat(propertyInfoFromDB).isEqualToIgnoringGivenFields(resultPropertyInfo,
                "createdDate", "updatedDate");
    }

    @Test
    void testUpdate_incorrectId() {
        propertyInfo.setId("someRandomString");
        assertThrows(NotFoundException.class, () -> propertyInfoDao.update(propertyInfo));
    }

    @Test
    void testUpdate_omittedNotNullFields() {
        PropertyInfo emptyPropertyInfo = new PropertyInfo();
        emptyPropertyInfo.setId(propertyInfo.getId());
        assertThrows(DbException.class, () -> propertyInfoDao.update(emptyPropertyInfo));
    }

    @Test
    void testDelete_successFlow() {
        assertTrue(propertyInfoDao.delete(propertyInfo.getId()));
        assertThrows(NotFoundException.class, () -> propertyInfoDao.findById(propertyInfo.getId()));
    }

    @Test
    void testDelete_incorrectId() {
        // Deleting not existing propertyInfo from db
        assertThrows(NotFoundException.class, () -> propertyInfoDao.delete("randomString"));
    }

    private PropertyInfo getNewPropertyInfo() {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setLocation("Somewhere");
        propertyInfo.setOwnerId(ownerId);
        propertyInfo.setDescription("Pretty looking house");
        propertyInfo.setName("House under water");
        return propertyInfo;
    }

    private void prepareDB() {
        // insert user
        template.update("INSERT INTO Users ( id, email, password, surname, name, phone_number," +
                " active, created_date, updated_date) VALUES ('" + ownerId + "', 'john@john.com', " +
                "'1234', 'Sonmez', 'John', '0955456465', 1, '2002-09-24-06:00', '2002-09-24-06:00');");
    }
}
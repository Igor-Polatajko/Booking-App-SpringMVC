package app.daos;


import app.domains.UserRole;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserRoleDaoImplTest extends BaseDaoTest {

    @Autowired
    private UserRoleDao userRoleDao;

    private UserRole userRole;

    @BeforeEach
    void setup() {
        // Initializing test userRole
        userRole = new UserRole();
        userRole.setName("ADMIN");

        // Creating user userRole
        userRoleDao.create(userRole);
    }

    @AfterEach
    void tearDown() {
        clearTables("User_roles");
    }

    @Test
    void testCreate_successFlow() {
        // Initializing test userRole
        UserRole userRole = new UserRole();
        userRole.setId("userRoleID");
        userRole.setName("USER");

        // Testing
        UserRole resultUserRole = userRoleDao.create(userRole);

        // Asserting results
        assertEquals(userRole, resultUserRole);
        assertNotNull(resultUserRole.getId());

        UserRole userRoleFromDB = userRoleDao.findById(userRole.getId());
        assertThat(resultUserRole).isEqualToIgnoringGivenFields(userRoleFromDB,
                "createdDate", "updatedDate");
    }

    @Test
    void testCreate_duplicateName() {
        UserRole newUserRole = new UserRole();
        newUserRole.setName(userRole.getName());

        assertThrows(DbException.class, () -> userRoleDao.create(newUserRole));
    }

    @Test
    void testCreate_omittedNotNullFields() {
        UserRole emptyUserRole = new UserRole();
        assertThrows(DbException.class, () -> userRoleDao.create(emptyUserRole));
    }

    @Test
    void testFindById_successFlow() {
        UserRole resultUserRole = userRoleDao.findById(userRole.getId());
        assertThat(resultUserRole).isEqualToIgnoringGivenFields(userRole,
                "createdDate", "updatedDate");
    }

    @Test
    void testFindById_incorrectId() {
        assertThrows(NotFoundException.class, () -> userRoleDao.findById("someRandomString"));
    }

    @Test
    void testUpdate_successFlow() {
        // Initializing userRole
        UserRole updatedUserRole = new UserRole();
        updatedUserRole.setId(userRole.getId());
        updatedUserRole.setName("ROLE_ADMIN");
        updatedUserRole.setCreatedDate(userRole.getCreatedDate());

        // Testing
        UserRole resultUserRole = userRoleDao.update(updatedUserRole);

        // Asserting results
        assertSame(updatedUserRole, resultUserRole);

        UserRole userRoleFromDB = userRoleDao.findById(userRole.getId());
        assertThat(userRoleFromDB).isEqualToIgnoringGivenFields(updatedUserRole,
                "createdDate", "updatedDate");
    }

    @Test
    void testUpdate_incorrectId() {
        userRole.setId("someRandomString");
        assertThrows(NotFoundException.class, () -> userRoleDao.update(userRole));
    }

    @Test
    void testUpdate_omittedNotNullFields() {
        UserRole emptyUserRole = new UserRole();
        emptyUserRole.setId(userRole.getId());
        assertThrows(DbException.class, () -> userRoleDao.update(emptyUserRole));
    }

    @Test
    void testDelete_successFlow() {
        assertTrue(userRoleDao.delete(userRole.getId()));
        assertThrows(NotFoundException.class, () -> userRoleDao.findById(userRole.getId()));
    }

    @Test
    void testDelete_incorrectId() {
        // Deleting not existing userRole from db
        assertThrows(NotFoundException.class, () -> userRoleDao.delete("randomString"));
    }

}

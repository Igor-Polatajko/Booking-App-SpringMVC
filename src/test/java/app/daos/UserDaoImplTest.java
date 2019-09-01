package app.daos;

import app.domains.User;
import app.exceptions.DbException;
import app.exceptions.NotFoundException;
import app.utils.EncryptionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest extends BaseDaoTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private EncryptionUtil encryptionUtil;

    private User user;

    @BeforeEach
    void setup() {
        // Initializing test user
        user = new User();
        user.setEmail("example@gmail.com");
        user.setPassword("12345");
        user.setSurname("Johnson");
        user.setName("John");
        user.setPhoneNumber("0626552521415");
        user.setActive(true);

        // Creating user
        userDao.create(user);
    }

    @AfterEach
    void tearDown() {
        clearTables("Users");
    }

    @Test
    void testCreate_successFlow() {
        // Instantiating new user object
        User newUser = new User();
        newUser.setEmail("someAnotherEmail@gmail.com");
        newUser.setPassword("12345");
        newUser.setSurname("Johnson");
        newUser.setName("John");
        newUser.setPhoneNumber("0626552521415");

        // Testing
        User resultUser = userDao.create(newUser);

        // Asserting results
        assertSame(newUser, resultUser);
        assertNotNull(newUser.getId());

        User userFromDB = userDao.findById(resultUser.getId());
        // Encrypting user password
        resultUser.setPassword(encryptionUtil.encode(resultUser.getPassword()));
        assertThat(resultUser).isEqualToIgnoringGivenFields(userFromDB,
                "createdDate",  "updatedDate");
    }

    @Test
    void testCreate_duplicateUsername() {
        // A user with this name already exists
        // Should throw DbOperationException
        assertThrows(DbException.class, () -> userDao.create(user));
    }


    @Test
    void testCreate_omittedNotNullFields() {
        // Creating empty user item
        User emptyUserItem = new User();
        assertThrows(DbException.class, () -> userDao.create(emptyUserItem));
    }

    @Test
    void testFindById_successFlow() {
        // Getting user
        User resultUser = userDao.findById(user.getId());

        // Encrypting user password
        user.setPassword(encryptionUtil.encode(user.getPassword()));

        assertThat(user).isEqualToIgnoringGivenFields(resultUser, "id", "createdDate", "updatedDate");
    }

    @Test
    void testFindById_invalidId() {
        assertThrows(NotFoundException.class, () -> userDao.findById("randomString"));
    }

    @Test
    void testFindByEmail_successFlow() {
        User resultUser = userDao.findByEmail(user.getEmail());

        // Encrypting user password
        user.setPassword(encryptionUtil.encode(user.getPassword()));

        assertThat(user).isEqualToIgnoringGivenFields(resultUser, "id", "createdDate", "updatedDate");
    }

    @Test
    void testFindByEmail_incorrectEmail() {
        assertThrows(NotFoundException.class, () -> userDao.findByEmail("not_existing_email@gmail.com"));
    }

    @Test
    void testUpdate_successFlow() {
        // Creating updated user
        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setEmail("updated@gmail.com");
        updatedUser.setSurname("Smith");
        updatedUser.setName("Den");
        updatedUser.setPhoneNumber("0333333333");
        updatedUser.setCreatedDate(user.getCreatedDate());
        userDao.update(updatedUser);
        User resultUser = userDao.findById(user.getId());
        assertThat(updatedUser).isEqualToIgnoringGivenFields(resultUser,
                "createdDate", "updatedDate", "password");
    }

    @Test
    void testUpdate_invalidId() {
        user.setId("randomString");
        assertThrows(NotFoundException.class, () -> userDao.update(user));
    }

    @Test
    void testUpdate_omittedNotNullFieldsExceptId() {
        // Creating empty user item
        User someUser = new User();

        // Setting id of an existing user
        someUser.setId(user.getId());

        assertThrows(DbException.class, () -> userDao.update(someUser));
    }

    @Test
    void testDelete_successFlow() {
        // Deleting user from db
        assertTrue(userDao.delete(user.getId()));

        assertThrows(NotFoundException.class, () -> userDao.findById(user.getId()));
    }

    @Test
    void testDelete_invalidId() {
        // Deleting not existing user from db
        assertThrows(NotFoundException.class, () -> userDao.delete("randomString"));
    }
}

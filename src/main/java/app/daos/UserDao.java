package app.daos;

import app.domains.User;

public interface UserDao {
    User create(User user);

    User findById(String id);

    User findByEmail(String email);

    User update(User user);

    boolean delete(String id);
}

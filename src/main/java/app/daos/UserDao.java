package app.daos;

import app.domains.User;

public interface UserDao extends CrudDao<User> {
    User findByEmail(String email);
}

package app.daos;

import app.domains.UserRole;

public interface UserRoleDao {
    UserRole create(UserRole userRole);

    UserRole findById(String id);

    UserRole update(UserRole userRole);

    boolean delete(String id);
}

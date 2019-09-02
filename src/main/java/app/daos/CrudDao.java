package app.daos;

public interface CrudDao<T> {
    T create(T obj);

    T findById(String id);

    T update(T obj);

    boolean delete(String id);
}

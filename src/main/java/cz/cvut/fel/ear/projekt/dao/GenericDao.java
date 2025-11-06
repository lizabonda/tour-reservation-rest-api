package cz.cvut.fel.ear.projekt.dao;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


public interface GenericDao<T> {


    T find(Integer id);

    List<T> findAll();

    void persist(T entity);

    default void persist(Collection<T> entities) {
        Objects.requireNonNull(entities);
        entities.forEach(this::persist);
    }

    T update(T entity);

    void remove(T entity);

    default boolean exists(Integer id) {
        return id != null && find(id) != null;
    }
}

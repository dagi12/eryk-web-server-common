package pl.edu.amu.wmi.service;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import pl.edu.amu.wmi.db.CommonEntityManager;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public interface CrudService<T> {

    CommonEntityManager getCommonEntityManager();

    Class<T> gettClass();

    default T create(T item) {
        setDefaultValues(item);
        getCommonEntityManager().save(item);
        return item;
    }

    default void setDefaultValues(T t) {
    }

    default T update(int id, T t) {
        getCommonEntityManager().update(t);
        return t;
    }

    default void delete(int id) {
        throw new InvalidDataAccessApiUsageException("Make sure class has audit annotation");
    }

    default String verify(T t) {
        return null;
    }
}

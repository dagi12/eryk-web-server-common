package pl.edu.amu.wmi.service;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import pl.edu.amu.wmi.db.MyEntityManager;
import pl.edu.amu.wmi.util.ObjectUtil;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public interface CrudService<T> {

    MyEntityManager getEntityManager();

    default Class<T> gettClass() {
        return ObjectUtil.getGenericType(this, CrudService.class);
    }

    default T create(T item) {
        setDefaultValues(item);
        getEntityManager().persist(item);
        return item;
    }

    default void setDefaultValues(T t) {
    }

    default T update(int id, T t) {
        getEntityManager().merge(t);
        return t;
    }

    default void delete(int id) {
        throw new InvalidDataAccessApiUsageException("Make sure class has audit annotation");
    }

    default String verify(T t) {
        return null;
    }

    default String verifyUpdate(T t) {
        return null;
    }
}

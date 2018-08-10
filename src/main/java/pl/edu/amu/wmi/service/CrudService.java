package pl.edu.amu.wmi.service;

import pl.edu.amu.wmi.db.CommonEntityManager;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public interface CrudService<T> {

    CommonEntityManager getCommonEntityManager();

    Class<T> gettClass();

    default T create(T t) {
        setDefaultValues(t);
        getCommonEntityManager().save(t);
        return t;
    }

    default void setDefaultValues(T t) {
    }

    default T update(int id, T t) {
        getCommonEntityManager().update(t);
        return t;
    }

    default void delete(int id) {
        getCommonEntityManager().delete(gettClass(), id);
    }
}

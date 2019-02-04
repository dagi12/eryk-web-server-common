package pl.edu.amu.wmi.service;

import pl.edu.amu.wmi.db.MyEntityManager;

/**
 * Created by erykmariankowski on 21.09.2018.
 */
public class CrudServiceImpl<T> implements CrudService<T> {

    private final MyEntityManager myEntityManager;

    public CrudServiceImpl(MyEntityManager myEntityManager) {
        this.myEntityManager = myEntityManager;
    }

    @Override
    public MyEntityManager getEntityManager() {
        return myEntityManager;
    }

}

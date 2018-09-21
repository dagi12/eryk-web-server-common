package pl.edu.amu.wmi.service;

import pl.edu.amu.wmi.db.CommonEntityManager;

/**
 * Created by erykmariankowski on 21.09.2018.
 */
public class CrudServiceImpl<T> implements CrudService<T> {

    private final CommonEntityManager commonEntityManager;

    public CrudServiceImpl(CommonEntityManager commonEntityManager) {
        this.commonEntityManager = commonEntityManager;
    }

    @Override
    public CommonEntityManager getCommonEntityManager() {
        return commonEntityManager;
    }

}

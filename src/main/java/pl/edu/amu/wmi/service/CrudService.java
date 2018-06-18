package pl.edu.amu.wmi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.amu.wmi.controller.CrudController;
import pl.edu.amu.wmi.db.CommonEntityManager;
import pl.edu.amu.wmi.util.ObjectUtil;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
@Service
@Transactional
public class CrudService<T> {

    private final CommonEntityManager commonEntityManager;
    private final Class<T> tClass;

    @Autowired
    public CrudService(CommonEntityManager commonEntityManager) {
        this.commonEntityManager = commonEntityManager;
        this.tClass = ObjectUtil.getGenericType(this, CrudController.class, 0);
    }

    public T create(T t) {
        commonEntityManager.save(t);
        return t;
    }

    public T update(int id, T t) {
        commonEntityManager.update(t);
        return t;
    }

    public void delete(int id) {
        commonEntityManager.delete(tClass, id);
    }
}

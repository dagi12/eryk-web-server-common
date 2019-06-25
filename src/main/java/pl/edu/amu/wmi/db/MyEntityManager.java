package pl.edu.amu.wmi.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.amu.wmi.model.MyRuntimeException;
import pl.edu.amu.wmi.util.pair.Pair;
import pl.edu.amu.wmi.util.pair.PairUtil;
import pl.softra.common.db.context.initializer.DataBaseConfigHolder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.util.List;

/**
 * Stworzone przez Eryk Mariankowski dnia 10.02.18.
 */
@Service
public class MyEntityManager {

    private final EntityManager entityManager;

    private final SpecExecutor specExecutor;

    private final DataBaseConfigHolder dataBaseConfig;

    private final StoredProcedureUtilService storedProcedureUtilService;

    @Autowired
    public MyEntityManager(EntityManager entityManager,
                           SpecExecutor specExecutor, DataBaseConfigHolder dataBaseConfig, StoredProcedureUtilService storedProcedureUtilService) {
        this.entityManager = entityManager;
        this.specExecutor = specExecutor;
        this.dataBaseConfig = dataBaseConfig;
        this.storedProcedureUtilService = storedProcedureUtilService;
    }

    /**
     * Compatible with Oracle
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public <T> List<T> executeProcedure2(Class<T> tClass, String procedureName, Object... params) {
        if (dataBaseConfig.isOracle()) {
            Query nativeQuery = storedProcedureUtilService.commonProcedure(procedureName, tClass, params);
            return nativeQuery.getResultList();
        }
        return executeProcedure(procedureName, params);
    }


    public <T> List<T> all(Class<T> tClass) {
        return entityManager
                .createQuery(specExecutor.commonQuery(tClass))
                .getResultList();
    }

    public <T> T unique(Class<T> tClass) {
        return entityManager
                .createQuery(specExecutor.commonQuery(tClass))
                .getSingleResult();
    }

    @Transactional
    public <T, S extends Serializable> T find(Class<T> tClass, S id) {
        return entityManager.find(tClass, id);
    }

    @Transactional
    public <T> void deleteAll(Class<T> tClass) {
        for (T entity : all(tClass)) {
            entityManager.remove(entity);
        }
        entityManager.flush();
    }

    @Transactional
    public <T, S extends Serializable> void remove(Class<T> aClass, S id) {
        T obj = find(aClass, id);
        entityManager.remove(obj);
    }


    @Transactional
    public <T> void merge(T entity) {
        entityManager.merge(entity);
    }

    @Transactional
    public <T> void persist(T object) {
        entityManager.persist(object);
    }

    // should be transactional because call to executeProcedure (sonar)
    @Transactional
    public <T> T executeProcedureSingle(String procedureName, Object... parameters) {
        List<T> list = executeProcedure(procedureName, parameters);
        if (list.size() != 1) {
            throw new MyRuntimeException("Procedure result is not single");
        }
        return list.get(0);
    }

    /**
     * This method is used for procedure with single output parameter return type
     * Should be transactional or execution from unit tests won't work
     *
     * @param procedureName in sql
     * @param parameters    all parameters to procedure in order
     * @param <T>           type of last output parameter
     * @return single primitive output
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public <T> T procedureResult(String procedureName, Object... parameters) {
        StoredProcedureQuery query = procedureQuery(procedureName, parameters);
        return (T) query.getOutputParameterValue(parameters.length + 1);
    }

    public StoredProcedureQuery procedureQuery(String procedureName, Object... parameters) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery(procedureName);
        return StoredProcedureUtilService.procedureQueryInternal(query, parameters);
    }

    // should be transactional for BookingControllerIT
    @SuppressWarnings("unchecked")
    @Transactional
    public <T> List<T> executeProcedure(String procedureName, Object... parameters) {
        return procedureQuery(procedureName, parameters).getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T, S> Pair<T, S> procedureResultPair(String procedureName, Object... parameters) {
        StoredProcedureQuery query = procedureQuery(procedureName, parameters);
        return PairUtil.of(
                (T) query.getOutputParameterValue(parameters.length + 1),
                (S) query.getOutputParameterValue(parameters.length + 2)
        );
    }


}

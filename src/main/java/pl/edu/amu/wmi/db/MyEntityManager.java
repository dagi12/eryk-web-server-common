package pl.edu.amu.wmi.db;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.amu.wmi.model.MyRuntimeException;
import pl.edu.amu.wmi.util.pair.Pair;
import pl.edu.amu.wmi.util.pair.PairUtil;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

/**
 * Stworzone przez Eryk Mariankowski dnia 10.02.18.
 */
@Service
public class MyEntityManager {

    private final EntityManager entityManager;

    private final SpecExecutor specExecutor;

    @Autowired
    public MyEntityManager(EntityManager entityManager,
                           SpecExecutor specExecutor) {
        this.entityManager = entityManager;
        this.specExecutor = specExecutor;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> executeProcedure(String procedureName, Object... parameters) {
        return procedureQuery(procedureName, parameters).getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> T executeProcedureSingle(String procedureName, Object... parameters) {
        List list = executeProcedure(procedureName, parameters);
        if (list.size() != 1) {
            throw new MyRuntimeException("Procedure result is not single");
        }
        return (T) list.get(0);
    }

    @SuppressWarnings("squid:S2077")
    public void voidProcedure(String procedureName, Object... parameters) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            builder.append("?");
            if (i != parameters.length - 1) {
                builder.append(",");
            }
        }
        String sqlString = "SELECT 1 FROM " + procedureName + "(" + builder.toString() + ")";
        Query query = entityManager.createNativeQuery(sqlString);
        for (int i = 0; i < parameters.length; ++i) {
            query.setParameter(i + 1, parameters[i]);
        }
        query.getSingleResult();
    }


    private StoredProcedureQuery procedureQueryInternal(StoredProcedureQuery query, Object[] parameters) {
        for (int i = 0; i < parameters.length; ++i) {
            query.setParameter(i + 1, parameters[i]);
        }
        query.execute();
        return query;
    }

    public StoredProcedureQuery procedureQuery(String procedureName, Object... parameters) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery(procedureName);
        return procedureQueryInternal(query, parameters);
    }

    /**
     * This method is used for procedure with single output parameter return type
     *
     * @param procedureName in sql
     * @param parameters    all parameters to procedure in order
     * @param <T>           type of last output parameter
     * @return single primitive output
     */
    @SuppressWarnings("unchecked")
    public <T> T procedureResult(String procedureName, Object... parameters) {
        StoredProcedureQuery query = procedureQuery(procedureName, parameters);
        return (T) query.getOutputParameterValue(parameters.length + 1);
    }

    @SuppressWarnings("unchecked")
    public <T, S> Pair<T, S> procedureResultPair(String procedureName, Object... parameters) {
        StoredProcedureQuery query = procedureQuery(procedureName, parameters);
        return PairUtil.of(
                (T) query.getOutputParameterValue(parameters.length + 1),
                (S) query.getOutputParameterValue(parameters.length + 2)
        );
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> dynamicProcedure(Class<T> resultClasses, String procedureName, Object... parameters) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedureName, resultClasses);
        for (int i = 0; i < parameters.length; ++i) {
            Class type = parameters[i] != null ? parameters[i].getClass() : String.class;
            query.registerStoredProcedureParameter(i + 1, type, ParameterMode.IN);
        }
        return procedureQueryInternal(query, parameters).getResultList();
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
    public <T, S> T findById(Class<T> tClass, S id) {
        return entityManager.find(tClass, id);
    }

    public void doWork(final Work work) {
        entityManager.unwrap(Session.class).doWork(work);
    }

    @Transactional
    public <T> void deleteAll(Class<T> tClass) {
        for (T entity : all(tClass)) {
            entityManager.remove(entity);
        }
        entityManager.flush();
    }

    @Transactional
    public <T> void delete(Class<T> aClass, int id) {
        T obj = findById(aClass, id);
        entityManager.remove(obj);
    }


    @Transactional
    public <T> void update(T entity) {
        entityManager.merge(entity);
    }

    @Transactional
    public <T> void save(T object) {
        entityManager.persist(object);
    }
}

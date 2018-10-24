package pl.edu.amu.wmi.db;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.amu.wmi.model.MyRuntimeException;
import pl.edu.amu.wmi.util.pair.Pair;
import pl.edu.amu.wmi.util.pair.PairUtil;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

import static pl.edu.amu.wmi.db.CommonSpecifications.MAX_AUTOCOMPLETE_ROWS;
import static pl.edu.amu.wmi.db.CommonSpecifications.findByAutoCompleteValue;

/**
 * Stworzone przez Eryk Mariankowski dnia 10.02.18.
 */
@Service
public class CommonEntityManager {

    private final EntityManager entityManager;

    private final GenericSpecificationExecutor specificationExecutor;

    @Autowired
    public CommonEntityManager(EntityManager entityManager,
                               GenericSpecificationExecutor specificationExecutor) {
        this.entityManager = entityManager;
        this.specificationExecutor = specificationExecutor;
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

    public void detach(Object object) {
        entityManager.detach(object);
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

    private <T> CriteriaQuery<T> commonQuery(Class<T> tClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(tClass);
        Root<T> variableRoot = query.from(tClass);
        return query.select(variableRoot);
    }

    private <T> CriteriaQuery commonQueryProjection(Class<T> inputClass, String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(inputClass);
        Root<T> variableRoot = query.from(inputClass);
        return query.select(variableRoot.get(name));
    }

    public <T> TypedQuery<T> allTypedQuery(Class<T> tClass) {
        return entityManager
                .createQuery(commonQuery(tClass));
    }

    public <T> List<T> all(Class<T> tClass) {
        return allTypedQuery(tClass).getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> allProjection(Class tClass, String name) {
        return entityManager
                .createQuery(commonQueryProjection(tClass, name))
                .getResultList();
    }

    public <T, S> List<T> allOrder(Class<T> tClass, SingularAttribute<T, S> col, boolean asc) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(tClass);
        Root<T> root = query.from(tClass);
        CriteriaQuery<T> ordered = query
                .orderBy(asc ?
                        builder.asc(root.get(col)) :
                        builder.desc(root.get(col)));
        return entityManager
                .createQuery(ordered.select(root))
                .getResultList();

    }

    public <T> T unique(Class<T> tClass) {
        return entityManager.createQuery(commonQuery(tClass)).getSingleResult();
    }

    @Transactional
    public <T, S> T findById(Class<T> tClass, S id) {
        return entityManager.find(tClass, id);
    }

    @Transactional
    public <T> T getByString(Class<T> tClass, String id) {
        return entityManager.find(tClass, id);
    }

    @Transactional
    public <T> void update(T entity) {
        entityManager.merge(entity);
    }

    public void doWork(final Work work) {
        entityManager.unwrap(Session.class).doWork(work);
    }

    @Transactional
    public <T> void save(T object) {
        entityManager.persist(object);
    }

    public <T> Integer count(Class<T> tClass) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        return entityManager
                .createQuery(cq.select(qb.count(cq.from(tClass))))
                .getSingleResult()
                .intValue();
    }


    public <T> T last(Class<T> tClass, String col) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(tClass);
        Root<T> variableRoot = query.from(tClass);
        query.orderBy(builder.desc(variableRoot.get(col)));
        return entityManager
                .createQuery(query.select(variableRoot))
                .setMaxResults(1)
                .getSingleResult();
    }


    public <T> void delete(T item) {
        entityManager.remove(item);
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

    public <T> List<T> findAutoCompleteValue(Class<T> tClass, String columnName, String text) {
        return specificationExecutor
                .findAllQuery(tClass, findByAutoCompleteValue(columnName, text))
                .setMaxResults(MAX_AUTOCOMPLETE_ROWS)
                .getResultList();
    }

}

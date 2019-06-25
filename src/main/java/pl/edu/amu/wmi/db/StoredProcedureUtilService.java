package pl.edu.amu.wmi.db;

import org.springframework.stereotype.Service;
import pl.softra.common.db.context.initializer.DataBaseConfigHolder;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

/**
 * Stworzone przez Eryk Mariankowski dnia 24.06.2019.
 */
@Service
public class StoredProcedureUtilService {

    private final EntityManager entityManager;

    private final DataBaseConfigHolder dataBaseConfig;

    public StoredProcedureUtilService(EntityManager entityManager, DataBaseConfigHolder dataBaseConfig) {
        this.entityManager = entityManager;
        this.dataBaseConfig = dataBaseConfig;
    }

    static StoredProcedureQuery procedureQueryInternal(StoredProcedureQuery query, Object[] parameters) {
        for (int i = 0; i < parameters.length; ++i) {
            query.setParameter(i + 1, parameters[i]);
        }
        query.execute();
        return query;
    }

    @SuppressWarnings("SqlResolve")
    public <T> Query internalCommonProcedure(String procedureName, Class<T> tClass, Object... parameters) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            builder.append("?");
            if (i != parameters.length - 1) {
                builder.append(",");
            }
        }
        Query query = getQuery(tClass);
        query.setParameter(0, procedureName);
        query.setParameter(1, builder.toString());
        int correctedLength = parameters.length + 3;
        for (int i = 0; i < correctedLength; ++i) {
            query.setParameter(i, parameters[i]);
        }
        return query;
    }

    @SuppressWarnings("SqlResolve")
    private <T> Query getQuery(Class<T> tClass) {
        if (tClass != null) {
            if (dataBaseConfig.isOracle()) {
                return entityManager.createNativeQuery("SELECT * FROM table(?(?))", tClass);
            }
            return entityManager.createNativeQuery("SELECT * FROM ?(?)", tClass);
        }
        if (dataBaseConfig.isOracle()) {
            return entityManager.createNativeQuery("SELECT * FROM table(?(?))");
        }
        return entityManager.createNativeQuery("SELECT * FROM ?(?)");
    }

    public <T> Query commonProcedure(String procedureName, Class<T> tClass, Object... parameters) {
        return internalCommonProcedure(procedureName, tClass, parameters);
    }

    public Query commonProcedure(String procedureName, Object... parameters) {
        return internalCommonProcedure(procedureName, null, parameters);
    }

    @SuppressWarnings("squid:S2077")
    public void voidProcedure(String procedureName, Object... parameters) {
        Query query = commonProcedure(procedureName, null, parameters);
        query.getSingleResult();
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

}

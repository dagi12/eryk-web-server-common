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

    public <T> Query internalCommonProcedure(String procedureName, Class<T> tClass, Object... parameters) {
        StringBuilder builder = getStringBuilder(parameters);
        Query query = getQuery(tClass, procedureName, builder.toString());
        for (int i = 1; i <= parameters.length; ++i) {
            query.setParameter(i, parameters[i - 1]);
        }
        return query;
    }

    private <T> Query getQuery(Class<T> tClass, String procedureName, String parametersToFill) {
        String format1 = String.format("SELECT * FROM %s(%s)", procedureName, parametersToFill);
        if (tClass != null) {
            if (dataBaseConfig.isOracle()) {
                String format = String.format("SELECT * FROM table(%s(%s))", procedureName, parametersToFill);
                return entityManager.createNativeQuery(format, tClass);
            }
            return entityManager.createNativeQuery(format1, tClass);
        }
        if (dataBaseConfig.isOracle()) {
            String format = String.format("SELECT * FROM table(%s(%s))", procedureName, parametersToFill);
            return entityManager.createNativeQuery(format);
        }
        return entityManager.createNativeQuery(format1);
    }

    public <T> Query commonProcedure(String procedureName, Class<T> tClass, Object... parameters) {
        return internalCommonProcedure(procedureName, tClass, parameters);
    }

    public Query commonProcedure(String procedureName, Object... parameters) {
        return internalCommonProcedure(procedureName, null, parameters);
    }

    // do not change that MappingException: No Dialect mapping for JDBC type: 1111
    @SuppressWarnings("squid:S2077")
    public void voidProcedure(String procedureName, Object... parameters) {
        StringBuilder builder = getStringBuilder(parameters);
        String sqlString = "SELECT 1 FROM " + procedureName + "(" + builder.toString() + ")";
        Query query = entityManager.createNativeQuery(sqlString);
        for (int i = 0; i < parameters.length; ++i) {
            query.setParameter(i + 1, parameters[i]);
        }
        query.getSingleResult();
    }

    private StringBuilder getStringBuilder(Object[] parameters) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            builder.append("?");
            if (i != parameters.length - 1) {
                builder.append(",");
            }
        }
        return builder;
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

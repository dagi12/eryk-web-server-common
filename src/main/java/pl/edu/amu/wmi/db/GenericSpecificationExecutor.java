package pl.edu.amu.wmi.db;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by erykmariankowski on 11.09.2018.
 */
@Component
public class GenericSpecificationExecutor {

    private final EntityManager entityManager;

    public GenericSpecificationExecutor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> TypedQuery<T> findAllQuery(Class<T> tClass, Specification<T> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(tClass);
        Root<T> root = query.from(tClass);
        query.select(root);
        return entityManager
                .createQuery(query.where(spec.toPredicate(root, query, cb)));
    }

    public <T> List<T> findAll(Class<T> tClass, Specification<T> spec) {
        return findAllQuery(tClass, spec).getResultList();
    }

}

package pl.edu.amu.wmi.db;

import org.springframework.data.domain.Pageable;
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
public class SpecExecutor {

    private final EntityManager entityManager;

    public SpecExecutor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    <T> CriteriaQuery<T> commonQuery(Class<T> tClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(tClass);
        Root<T> variableRoot = query.from(tClass);
        return query.select(variableRoot);
    }

    public <T> TypedQuery<T> findAllQuery(Class<T> tClass, Specification<T> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(tClass);
        Root<T> root = query.from(tClass);
        query.select(root);
        return entityManager
                .createQuery(query.where(spec.toPredicate(root, query, cb)));
    }

    /**
     * @throws javax.persistence.NoResultException        if there is no result
     * @throws javax.persistence.NonUniqueResultException if there is more than one result
     */
    public <T> T findOne(Class<T> tClass, Specification<T> spec) {
        return findAllQuery(tClass, spec).getSingleResult();
    }

    public <T> List<T> findAll(Class<T> tClass, Specification<T> spec) {
        return findAllQuery(tClass, spec).getResultList();
    }

    public <T> List<T> findAll(Class<T> tClass, Specification<T> spec, Pageable pageable) {
        return findAllQuery(tClass, spec)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult(Math.toIntExact(pageable.getOffset()))
                .getResultList();
    }

    public <T> Long count(Class<T> tClass, Specification<T> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> root = query.from(tClass);
        query.select(cb.count(root));
        return entityManager
                .createQuery(query.where(spec.toPredicate(root, query, cb)))
                .getSingleResult();
    }

}

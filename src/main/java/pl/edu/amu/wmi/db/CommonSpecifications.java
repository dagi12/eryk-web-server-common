package pl.edu.amu.wmi.db;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Stworzone przez Eryk Mariankowski dnia 23.03.18.
 */
public final class CommonSpecifications {

    public static final String QUERY_PREFIX = "SELECT * FROM ";
    public static final int MAX_AUTOCOMPLETE_ROWS = 15;
    public static final PageRequest FIRST_RESULT = PageRequest.of(0, 1);
    public static final PageRequest AUTOCOMPLETE_RESULT = PageRequest.of(0, MAX_AUTOCOMPLETE_ROWS);


    private CommonSpecifications() {

    }

    public static <T, S> Specification<T> findByColumnValue(SingularAttribute<T, S> attribute, S value) {
        return (root, query, cb) -> cb.equal(root.get(attribute), value);
    }

    public static <T, U extends T> Specification<U> findByColumnLikeValue(SingularAttribute<T, String> attribute,
                                                                          String value) {
        return (root, query, cb) -> cb.like(root.get(attribute), value);
    }

    public static <T, S> Specification<T> findByColumnValue(String attribute, S value) {
        return (root, query, cb) -> cb.equal(root.get(attribute), value);
    }

    public static <T, S> Specification<T> findByColumnValueOrdered(SingularAttribute<T, S> attribute, S value) {
        return (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get(attribute), value);
            query.orderBy(cb.asc(root.get(attribute)));
            return predicate;
        };
    }

    public static <T> Specification<T> findByAutoCompleteValue(String columnName, String value) {
        return (root, query, cb) -> ilike(cb, root.get(columnName), wrapLike(value));
    }

    public static <T> Specification<T> findByAutoCompleteValue(SingularAttribute<T, String> attribute, String value) {
        return (root, query, cb) -> ilike(cb, root.get(attribute), wrapLike(value));
    }

    public static <T, S> Specification<T> existsByColumnValue(SingularAttribute<T, S> attribute, S value) {
        return (root, query, cb) -> cb.equal(root.get(attribute), value);
    }

    public static <T, S> Specification<T> existsByColumnValue(String column, S value) {
        return (root, query, cb) -> cb.equal(root.get(column), value);
    }

    public static Predicate ilike(CriteriaBuilder cb, Expression<String> path, String value) {
        return cb.like(cb.lower(path), value.toLowerCase());
    }

    // don't add to lowercase
    public static String wrapLike(String stmt) {
        return "%" + stmt + "%";
    }
}
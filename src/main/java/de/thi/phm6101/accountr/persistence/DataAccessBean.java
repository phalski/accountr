package de.thi.phm6101.accountr.persistence;


import de.thi.phm6101.accountr.domain.AbstractEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Stateless
public class DataAccessBean {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    public <T extends AbstractEntity> T get(Class<T> clazz, long id) {
        return em.find(clazz, id);
    }

    /**
     * Returns all entities of a JPA class
     * @param clazz JPA entity class
     * @param <T> JPA entity
     * @return Entity list
     */
    public <T extends AbstractEntity> List<T> getAll(Class<T> clazz) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        return em.createQuery(query.select(query.from(clazz))).getResultList();
    }

    /**
     * Executes named query for JPA class
     * @param clazz JPA entity class
     * @param queryName name of query
     * @param parameters parameters of query
     * @param <T> JPA entity
     * @return Entity result list
     */
    public <T extends AbstractEntity> List<T> namedQuery(Class<T> clazz, String queryName, Map<String, Object> parameters) {
        TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
        for (Map.Entry<String, Object> parameter : parameters.entrySet())
        {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        return query.getResultList();
    }

    public <T extends AbstractEntity> void insert(T t) {
        em.persist(t);
    }

    public <T extends AbstractEntity> T update(T t) {
        return em.merge(t);
    }

    public <T extends AbstractEntity> void delete(T t) {
        em.remove(em.contains(t) ? t : em.merge(t));
    }

    public <T extends AbstractEntity> boolean exists(T t) {
        return Optional.ofNullable(em.find(t.getClass(), t.getId())).isPresent();
    }

}

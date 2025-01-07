package com.cjq.onlineshoppingapp.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class AbstractHibernateDao<T> {
    @Autowired
    protected SessionFactory sessionFactory;

    protected Class<T> entityClass;

    protected final void setEntityClass(Class<T> entityClassToSet) { this.entityClass = entityClassToSet; }

    public List<T> getAll() {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        query.from(entityClass);
        return session.createQuery(query).getResultList();
    }

    public T findById(long id) { return getCurrentSession().get(entityClass, id); }

    public void add(T entity) { getCurrentSession().save(entity); }

    protected Session getCurrentSession() { return sessionFactory.getCurrentSession(); }
}

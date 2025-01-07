package com.cjq.onlineshoppingapp.repository;

import com.cjq.onlineshoppingapp.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class UserDao extends AbstractHibernateDao<User> {

    public UserDao() {
        setEntityClass(User.class);
    }

    public boolean isUsernameOrEmailUnique(String username, String email) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        query.select(builder.count(root))
                .where(builder.or(
                        builder.equal(root.get("username"), username),
                        builder.equal(root.get("email"), email)
                ));

        Long count = getCurrentSession().createQuery(query).getSingleResult();
        return count > 0;
    }

    public void addUser(User user) { this.add(user); }

    public void updateUser(User user) {
        getCurrentSession().update(user);
    }

    public User findByUsername(String username) {
        Session session = getCurrentSession();
        Query<User> query = session.createQuery("FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }
}

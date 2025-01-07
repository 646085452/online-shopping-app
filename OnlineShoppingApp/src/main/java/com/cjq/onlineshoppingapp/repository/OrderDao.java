package com.cjq.onlineshoppingapp.repository;

import com.cjq.onlineshoppingapp.model.Order;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDao extends AbstractHibernateDao<Order> {

    public OrderDao() { setEntityClass(Order.class); }

    public void saveOrder(Order order) { getCurrentSession().save(order); }

    public void updateOrder(Order order) { getCurrentSession().update(order); }

    public List<Order> getAllOrders() { return getAll(); }

    public List<Order> getAllOrdersByUserId(Long userId) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root).where(builder.equal(root.get("user").get("userId"), userId));

        return session.createQuery(query).getResultList();
    }

    public Order getOrderById(long orderId) { return findById(orderId); }

    public List<Order> getOrdersByUserIdAndStatus(Long userId, String... statuses) {
        String hql = "FROM Order o WHERE o.user.userId = :userId AND o.orderStatus IN (:statuses)";

        return getCurrentSession().createQuery(hql, Order.class)
                .setParameter("userId", userId)
                .setParameterList("statuses", statuses)
                .getResultList();
    }

    public List<Order> getOrdersByStatus(String status) {
        String hql = "FROM Order o WHERE o.orderStatus = :status";

        return getCurrentSession().createQuery(hql, Order.class)
                .setParameter("status", status)
                .getResultList();
    }
}

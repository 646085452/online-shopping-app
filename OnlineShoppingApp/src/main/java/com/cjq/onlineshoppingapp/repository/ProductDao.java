package com.cjq.onlineshoppingapp.repository;

import com.cjq.onlineshoppingapp.model.Product;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao extends AbstractHibernateDao{

    public ProductDao() {
        setEntityClass(Product.class);
    }

    public List<Product> getAllProducts(){
        Session session = getCurrentSession();
        Query<Product> query = session.createQuery("FROM Product p", Product.class);
        return query.getResultList();
    }

    public Product getProductById(long id) {
        return (Product) findById(id);
    }

    public boolean isStockAvailable(long productId, int requestedQuantity) {
        Product product = getProductById(productId);
        return product != null && product.getQuantity() >= requestedQuantity;
    }

    public void updateProductStock(Long productId, int quantityPurchased) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setQuantity(product.getQuantity() - quantityPurchased);
            getCurrentSession().saveOrUpdate(product);
        }
    }

    public void saveProduct(Product product) {
        getCurrentSession().save(product);
    }

    public void deleteProduct(Product product) {
        getCurrentSession().delete(product);
    }

    public void updateProduct(Product product) {
        getCurrentSession().update(product);
    }

    @Cacheable(value = "totalSoldAmountCache", key = "#product.productId")
    public Long getTotalSoldAmount(Product product) {
        Session session = getCurrentSession();
        String hql = "SELECT SUM(oi.quantity) " +
                "FROM OrderItem oi " +
                "JOIN oi.order o " +
                "WHERE oi.product = :product AND o.orderStatus = :status";

        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("product", product);
        query.setParameter("status", "Completed");

        Long totalSoldAmount = query.uniqueResult();
        return totalSoldAmount != null ? totalSoldAmount : 0L;
    }

}

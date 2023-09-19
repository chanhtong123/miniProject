package com.example.superdataworker.repository;

import com.example.superdataworker.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Repository
public class CustomerRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public CustomerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(customer);
            session.getTransaction().commit();
        }
        return customer; // Trả về đối tượng Customer sau khi đã tạo
    }


    @Transactional
    public Customer readCustomer(String customerID) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Customer.class, customerID);
        }
    }

    @Transactional
    public Customer updateCustomer(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(customer);
            session.getTransaction().commit();
        }
        return customer;
    }

    @Transactional
    public void deleteCustomer(String customerID) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Customer customer = session.get(Customer.class, customerID);
            if (customer != null) {
                session.delete(customer);
            }
            session.getTransaction().commit();
        }
    }

    @Transactional
    public List<Customer> getAllCustomers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        }
    }

    @Transactional
    public List<Customer> searchCustomersByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            if (name != null && !name.isEmpty()) {
                Query<Customer> query = session.createQuery("FROM Customer WHERE firstName LIKE :name", Customer.class);
                query.setParameter("name", "%" + name + "%"); // Sử dụng ký tự '%' để tìm kiếm bất kỳ ký tự nào
                return query.list();
            } else {
                // Trả về danh sách trống nếu name là null hoặc trống trơn
                return Collections.emptyList();
            }
        }
    }


    @Transactional
    public boolean existsByCustomerId(String customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Customer WHERE customerId = :customerId", Long.class);
            query.setParameter("customerId", customerId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }

}

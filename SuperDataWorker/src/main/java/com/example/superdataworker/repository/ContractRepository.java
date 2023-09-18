package com.example.superdataworker.repository;

import com.example.superdataworker.model.Contract;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ContractRepository {

    private final SessionFactory sessionFactory;

    public ContractRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void createContract(Contract contract) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(contract);
            session.getTransaction().commit();
        }
    }

    @Transactional
    public Contract readContract(String contractID) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Contract.class, contractID);
        }
    }

    @Transactional
    public void updateContract(Contract contract) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(contract);
            session.getTransaction().commit();
        }
    }

    @Transactional
    public void deleteContract(String contractID) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Contract contract = session.get(Contract.class, contractID);
            if (contract != null) {
                session.delete(contract);
            }
            session.getTransaction().commit();
        }
    }

    @Transactional
    public List<Contract> getAllContract() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Contract", Contract.class).list();
        }
    }
}


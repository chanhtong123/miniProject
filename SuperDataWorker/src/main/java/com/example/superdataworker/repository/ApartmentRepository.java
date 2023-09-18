package com.example.superdataworker.repository;

import com.example.superdataworker.model.Apartment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ApartmentRepository {
    private final SessionFactory sessionFactory;

    public ApartmentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void createApartment(Apartment apartment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(apartment);
            session.getTransaction().commit();
        }
    }

    @Transactional
    public Apartment readApartment(String apartmentID) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Apartment.class, apartmentID);
        }
    }

    @Transactional
    public void updateApartment(Apartment apartment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(apartment);
            session.getTransaction().commit();
        }
    }

    @Transactional
    public void deleteApartment(String ApartmentID) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Apartment apartment = session.get(Apartment.class, ApartmentID);
            if (apartment != null) {
                session.delete(apartment);
            }
            session.getTransaction().commit();
        }
    }

    @Transactional
    public List<Apartment> getAllApartment() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Apartment", Apartment.class).list();
        }
    }
}

package com.example.superdataworker.repositories;

import com.example.superdataworker.models.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String> {

    Apartment findByApartmentId(String ApartmentId);
}

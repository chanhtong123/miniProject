package com.example.superdataworker.repositories;

import com.example.superdataworker.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

}


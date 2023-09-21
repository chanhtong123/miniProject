package com.example.superdataworker.service;

import com.example.superdataworker.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service

public interface CustomerService {
     List<Customer> getAllCustomers();
     Customer save(Customer customer);
     List<Customer> searchByName(String name);

     ResponseEntity<String> uploadFileCustomersFromCSV(MultipartFile csvFile) throws IOException;

}
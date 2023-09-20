package com.example.superdataworker.services;

import com.example.superdataworker.models.Customer;
import com.example.superdataworker.repositorys.CustomerRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service

public interface CustomerService {
    public List<Customer> getAllCustomers();
    public Customer save(Customer customer);
    public List<Customer> searchByName(String name);

    public ResponseEntity<String> uploadFileCustomersFromCSV(MultipartFile csvFile) throws IOException;

}
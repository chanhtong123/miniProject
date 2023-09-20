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

public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }

    public List<Customer> searchByName(String name){
        return customerRepository.findByFirstNameStartingWithOrLastNameStartingWith(name,name);
    }

    @Transactional
    public ResponseEntity<String> uploadFileCustomersFromCSV(MultipartFile csvFile) throws IOException {
        List<String> errorMessages = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1);

            while ((nextLine = csvReader.readNext()) != null) {
                String customerId = nextLine[0];
                String address = nextLine[1];
                String ageStr = nextLine[2];
                String firstName = nextLine[3];
                String lastName = nextLine[4];
                String status = nextLine[5];


                if (customerId == null || customerId.isEmpty()) {
                    errorMessages.add("CustomerID empty.");
                }
                if (firstName == null || firstName.isEmpty()) {
                    errorMessages.add("FirstName empty.");
                }
                if (lastName == null || lastName.isEmpty()) {
                    errorMessages.add("LastName empty.");
                }
                if (address == null) {
                    errorMessages.add("Address empty.");
                }
                if (ageStr == null) {
                    errorMessages.add("Age empty.");
                }
                if (status == null) {
                    errorMessages.add("Status empty.");
                }


                int age = 0;
                try {
                    age = Integer.parseInt(ageStr);
                    if (age < 1 || age > 150) {
                        errorMessages.add("Invalid age value.");
                    }
                } catch (NumberFormatException e) {
                    errorMessages.add("Invalid age value.");
                }


                if (!status.equals("Active") && !status.equals("Inactive")) {
                    errorMessages.add("Invalid status value.");
                }

                if (customerRepository.existsById(customerId)) {
                    errorMessages.add("CustomerID existed.");
                }


                if (!errorMessages.isEmpty()) {
                    continue;
                }


                Customer customer = new Customer();
                customer.setCustomerId(customerId);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setAddress(address);
                customer.setAge(age);
                customer.setStatus(status);

                customerRepository.save(customer);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }


        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.join("\n", errorMessages));
        }


        return ResponseEntity.ok("upload file success.");
    }



}
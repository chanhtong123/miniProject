package com.example.superdataworker.controllers;

import com.example.superdataworker.models.Customer;
import com.example.superdataworker.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {

        this.customerService = customerService;
    }

    @GetMapping("/get-all-customer")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }




    @PostMapping("/up-load-file")
    public ResponseEntity<String> uploadFileCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            ResponseEntity<String> response = customerService.uploadFileCustomersFromCSV(csvFile);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        }
    }

    @PostMapping("/search-by-name")
    public List<Customer> searchByFirstName(@RequestParam String name) {
        return customerService.searchByName(name);
    }
}

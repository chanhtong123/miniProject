package com.example.superdataworker.controller;

import com.example.superdataworker.model.Customer;
import com.example.superdataworker.repository.CustomerRepository;
import com.example.superdataworker.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @GetMapping("/gettAllCustomer")
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }


    @PostMapping("/create")
    public Customer createCustomer(@RequestBody Customer customer) {
        // Lưu thông tin khách hàng vào cơ sở dữ liệu sử dụng Repository
        Customer createdCustomer = customerRepository.createCustomer(customer);

        return createdCustomer;
    }


    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFileCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            ResponseEntity<String> response = customerService.uploadFileCustomersFromCSV(csvFile);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi nhập dữ liệu từ tệp CSV vào table Customer: " + e.getMessage());
        }
    }

    @PostMapping("/searchByName")
    public List<Customer> searchByFirstName(@RequestParam String name) {
        return customerRepository.searchCustomersByName(name);
    }
}

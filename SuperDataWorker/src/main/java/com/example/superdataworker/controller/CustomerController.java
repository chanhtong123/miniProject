package com.example.superdataworker.controllers;

import com.example.superdataworker.models.Customer;
import com.example.superdataworker.response.ResponseMessage;
import com.example.superdataworker.services.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    private final Logger logger = (Logger) LogManager.getLogger(CustomerController.class);

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllCustomers() {
        logger.info("Load all of customer");
        try {
            List<Customer> customers = customerService.getAllCustomers();
             logger.info("Get all of customers from database successfully!!!");

            return ResponseEntity.ok(
                    ResponseMessage.builder()
                            .statusCode(200)
                            .message("Success")
                            .data(customers)
                            .build()

            );
        } catch (Exception e){
            logger.error(e.getMessage());

            return ResponseEntity
                    .internalServerError()
                    .body(
                            ResponseMessage.builder()
                                    .statusCode(500)
                                    .message(e.getMessage())
                                    .build()
                    );
        }
    }


    @PostMapping
    public ResponseEntity<String> uploadFileCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            ResponseEntity<String> response = customerService.uploadFileCustomersFromCSV(csvFile);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        }
    }

    @PostMapping
    public List<Customer> searchByFirstName(@RequestParam String name) {
        return customerService.searchByName(name);
    }
}

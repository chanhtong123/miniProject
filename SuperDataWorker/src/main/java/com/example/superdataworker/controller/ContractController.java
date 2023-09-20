package com.example.superdataworker.controllers;

import com.example.superdataworker.models.Contract;
import com.example.superdataworker.response.ResponseMessage;
import com.example.superdataworker.services.ContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    private final Logger logger = (Logger) LogManager.getLogger(CustomerController.class);

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllCustomers() {
        logger.info("Load all of contracts");
        try {
            List<Contract> contracts = contractService.getAllContracts();
            logger.info("Get all of contracts from database successfully!!!");

            return ResponseEntity.ok(
                    ResponseMessage.builder()
                            .statusCode(200)
                            .message("Success")
                            .data(contracts)
                            .build()

            );
        } catch (Exception e) {
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

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFileCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        logger.info("Upload contracts");
        try {
            ResponseEntity<String> response = contractService.uploadFileContractsFromCSV(csvFile);
            logger.info("Upload all of contracts from file successfully!!!");

            return ResponseEntity.ok(
                    ResponseMessage.builder()
                            .statusCode(200)
                            .message("Success")
                            .data(response)
                            .build()

            );
        } catch (Exception e) {
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
}


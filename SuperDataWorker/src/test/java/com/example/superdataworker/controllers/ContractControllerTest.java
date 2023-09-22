package com.example.superdataworker.controllers;

import com.example.superdataworker.models.Contract;
import com.example.superdataworker.responses.ResponseMessage;
import com.example.superdataworker.services.ContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractControllerTest {

    @Autowired
    private ContractService contractService;

    private final Logger logger = LogManager.getLogger(CustomerController.class);

    @GetMapping("get-all-contracts")
    public ResponseEntity<ResponseMessage> getAllContracts() {
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
                                    .statusCode(201)
                                    .message(e.getMessage())
                                    .build()
                    );
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            ResponseEntity<String> response = contractService.uploadFileContractsFromCSV(csvFile);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        }
    }
}


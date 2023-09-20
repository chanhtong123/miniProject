package com.example.superdataworker.controllers;

import com.example.superdataworker.models.Contract;
import com.example.superdataworker.repositorys.ContractRepository;
import com.example.superdataworker.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    private final ContractRepository contractRepository;
    private final ContractService contractService;

    @Autowired
    public ContractController(ContractRepository contractRepository, ContractService contractService) {
        this.contractRepository = contractRepository;
        this.contractService = contractService;
    }


    @GetMapping("/get-all-contract")
    public List<Contract> getAllContract(){
        return contractService.getAllContract();
    }

    @PostMapping("/up-load-file")
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

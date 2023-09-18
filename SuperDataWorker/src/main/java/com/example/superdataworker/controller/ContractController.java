package com.example.superdataworker.controller;

import com.example.superdataworker.repository.ContractRepository;
import com.example.superdataworker.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contract")
public class ContractController {

    private final ContractRepository contractRepository;
    private final ContractService contractService;

    public ContractController(ContractRepository contractRepository, ContractService contractService) {
        this.contractRepository = contractRepository;
        this.contractService = contractService;
    }


    @PostMapping("/import")
    public ResponseEntity<String> importCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            contractService.importContractsFromCSV(csvFile);
            return ResponseEntity.status(HttpStatus.OK).body("Dữ liệu đã được nhập thành công từ tệp CSV vào table Contract.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi nhập dữ liệu từ tệp CSV vào table Contract: " + e.getMessage());
        }
    }
}

package com.example.superdataworker.services;

import com.example.superdataworker.models.Contract;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ContractService {
     List<Contract> getAllContracts();
     Contract save(Contract contract);
     ResponseEntity<String> uploadFileContractsFromCSV(MultipartFile csvFile) throws IOException;
}
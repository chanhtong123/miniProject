package com.example.superdataworker.services;

import com.example.superdataworker.model.Contract;
import com.example.superdataworker.models.Apartment;
import com.example.superdataworker.models.Contract;
import com.example.superdataworker.models.Customer;
import com.example.superdataworker.repository.ContractRepository;
import com.example.superdataworker.repositorys.ApartmentRepository;
import com.example.superdataworker.repositorys.ContractRepository;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository contractRepository;
    private CustomerRepository customerRepository;
    private ApartmentRepository apartmentRepository;

    public ContractService(ContractRepository contractRepository, CustomerRepository customerRepository, ApartmentRepository apartmentRepository) {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
        this.apartmentRepository = apartmentRepository;
    }


    public List<Contract> getAllContracts(){
        return contractRepository.findAll();
    }
    public Contract save(Contract contract){
        return contractRepository.save(contract);
    }

    @Transactional
    public ResponseEntity<String> uploadFileContractsFromCSV(MultipartFile csvFile) throws IOException {
        List<String> errorMessages = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1);

            while ((nextLine = csvReader.readNext()) != null) {
                String contractId = nextLine[0];
                String customerId = nextLine[4];
                String apartmentId = nextLine[3];
                String startDateStr = nextLine[2];
                String endDateStr = nextLine[1];

                Customer customer = customerRepository.findByCustomerId(customerId);
                Apartment apartment = apartmentRepository.findByApartmentId(apartmentId);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate startDate = null;
                LocalDate endDate = null;
                startDate = LocalDate.parse(startDateStr, formatter);
                endDate = LocalDate.parse(endDateStr, formatter);

                if(contractId == null || contractId.isEmpty()){
                    errorMessages.add("contracId empty.");
                }
                if(startDate == null){
                    errorMessages.add("startDate empty.");
                }
                if(endDate == null){
                    errorMessages.add("endDate empty.");
                }
                if (customer == null){
                    errorMessages.add("customerId not exist.");
                }
                if (apartment ==null){
                    errorMessages.add("apartmentId not exist.");
                }


                if (!errorMessages.isEmpty()) {
                    continue;
                }

                Contract contract = new Contract();
                contract.setContractId(contractId);
                contract.setCustomer(customer);
                contract.setApartment(apartment);
                contract.setStartDate(startDate);
                contract.setEndDate(endDate);
                contractRepository.save(contract);
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

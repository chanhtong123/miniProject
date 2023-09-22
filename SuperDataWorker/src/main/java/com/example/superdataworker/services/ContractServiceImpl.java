package com.example.superdataworker.services;

import com.example.superdataworker.models.Contract;
import com.example.superdataworker.models.Apartment;
import com.example.superdataworker.models.Customer;
import com.example.superdataworker.repositories.ContractRepository;
import com.example.superdataworker.repositories.ApartmentRepository;
import com.example.superdataworker.repositories.CustomerRepository;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {


    private final ContractRepository contractRepository;

    private final CustomerRepository customerRepository;

    private final ApartmentRepository apartmentRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, CustomerRepository customerRepository, ApartmentRepository apartmentRepository) {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    @Transactional
    @Override
    public ResponseEntity<String> uploadFileContractsFromCSV(MultipartFile csvFile) throws IOException {
        List<String> errorMessages = new ArrayList<>();
        List<Contract> validContracts = new ArrayList<>();
        int lineNumber = 1; // Số dòng bắt đầu từ 1

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1); // Bỏ qua dòng tiêu đề

            while ((nextLine = csvReader.readNext()) != null) {
                lineNumber++; // Tăng số dòng sau mỗi lần đọc

                if (nextLine.length < 5) {
                    // Đảm bảo có ít nhất 5 cột trong dòng CSV trước khi truy cập
                    errorMessages.add("Line " + lineNumber + ": Invalid number of columns in CSV line.");
                    continue;
                }
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

                try {
                    startDate = LocalDate.parse(startDateStr, formatter);
                    endDate = LocalDate.parse(endDateStr, formatter);
                } catch (DateTimeParseException e) {
                    errorMessages.add("Line " + lineNumber + ": Invalid date format in CSV line.");
                    continue;
                }

                // Kiểm tra các điều kiện lỗi
                boolean hasError = false;

                if (contractId == null || contractId.isEmpty()) {
                    errorMessages.add("Line " + lineNumber + ": ContractID empty.");
                    hasError = true;
                }
                if (customer == null) {
                    errorMessages.add("Line " + lineNumber + ": CustomerId not exist.");
                    hasError = true;
                }
                if (apartment == null) {
                    errorMessages.add("Line " + lineNumber + ": ApartmentId not exist.");
                    hasError = true;
                }

                assert contractId != null;
                if (contractRepository.existsById(contractId)) {
                    errorMessages.add("Line " + lineNumber + ": ContractId existed.\n");
                    hasError = true;
                }
                if (!hasError) {
                    // Tạo đối tượng Contract và thêm vào danh sách validContracts
                    Contract contract = new Contract();
                    contract.setContractId(contractId);
                    contract.setCustomer(customer);
                    contract.setApartment(apartment);
                    contract.setStartDate(startDate);
                    contract.setEndDate(endDate);
                    validContracts.add(contract);
                }
            }

            // Lưu tất cả các hợp đồng hợp lệ vào cơ sở dữ liệu
            contractRepository.saveAll(validContracts);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.join("\n", errorMessages));
        }

        return ResponseEntity.ok("Upload file success.");
    }

}

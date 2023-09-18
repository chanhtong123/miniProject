package com.example.superdataworker.service;

import com.example.superdataworker.model.Contract;
import com.example.superdataworker.model.Customer;
import com.example.superdataworker.repository.ContractRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ContractService {
    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Transactional
    public void importContractsFromCSV(MultipartFile csvFile) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1); // Bỏ qua dòng tiêu đề

            while ((nextLine = csvReader.readNext()) != null) {
                String contractId = nextLine[0];
                String customerId = nextLine[1];
                String apartmentId = nextLine[2];
                String startDateStr = nextLine[3];
                String endDateStr = nextLine[4];

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date startDate = null;
                Date endDate = null;

                try {
                    startDate = dateFormat.parse(startDateStr);
                    endDate = dateFormat.parse(endDateStr);
                } catch (ParseException e) {
                    // Xử lý trường hợp không thể chuyển đổi ngày thành đối tượng Date
                    e.printStackTrace();
                }


                // Tạo đối tượng Contract và lưu vào cơ sở dữ liệu
                Contract contract = new Contract();
                contract.setContractId(contractId);
                contract.setCustomerId(customerId);
                contract.setApartmentId(apartmentId);
                contract.setStartDate(startDate);
                contract.setEndDate(endDate);

                contractRepository.createContract(contract);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
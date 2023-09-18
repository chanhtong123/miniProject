package com.example.superdataworker.service;

import com.example.superdataworker.model.Customer;
import com.example.superdataworker.repository.CustomerRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
@Service

public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Transactional
    public void importCustomersFromCSV(MultipartFile csvFile) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1); // Bỏ qua dòng tiêu đề

            while ((nextLine = csvReader.readNext()) != null) {
                String customerId = nextLine[0];
                String firstName = nextLine[1];
                String lastName = nextLine[2];
                String address = nextLine[3];
                int age = 0; // Giá trị mặc định

                try {
                    age = Integer.parseInt(nextLine[4]);
                } catch (NumberFormatException e) {
                    // Xử lý trường hợp không thể chuyển đổi age thành số nguyên
                    age = 0; // Hoặc một giá trị mặc định khác tùy theo trường hợp của bạn
                }

                String status = nextLine[5];

                // Tạo đối tượng Customer và lưu vào cơ sở dữ liệu
                Customer customer = new Customer();
                customer.setCustomerId(customerId);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setAddress(address);
                customer.setAge(age);
                customer.setStatus(status);

                customerRepository.createCustomer(customer);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
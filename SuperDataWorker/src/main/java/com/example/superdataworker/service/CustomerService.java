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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service

public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }



    @Transactional
    public ResponseEntity<String> importCustomersFromCSV(MultipartFile csvFile) throws IOException {
        List<String> errorMessages = new ArrayList<>(); // Danh sách thông báo lỗi

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1); // Bỏ qua dòng tiêu đề

            while ((nextLine = csvReader.readNext()) != null) {
                String customerId = nextLine[0];
                String firstName = nextLine[1];
                String lastName = nextLine[2];
                String address = nextLine[3];
                String ageStr = nextLine[4];
                String status = nextLine[5];

                // Kiểm tra nếu có bất kỳ trường nào là null thì thêm thông báo lỗi vào danh sách
                if (customerId == null) {
                    errorMessages.add("Trường CustomerID trống.");
                }
                if (firstName == null || firstName.isEmpty()) {
                    errorMessages.add("Trường FirstName trống.");
                }
                if (lastName == null || lastName.isEmpty()) {
                    errorMessages.add("Trường LastName trống.");
                }
                if (address == null) {
                    errorMessages.add("Trường Address trống.");
                }
                if (ageStr == null) {
                    errorMessages.add("Trường Age trống.");
                }
                if (status == null) {
                    errorMessages.add("Trường Status trống.");
                }

                // Kiểm tra giá trị age
                int age = 0;
                try {
                    age = Integer.parseInt(ageStr);
                    if (age < 1 || age > 150) {
                        errorMessages.add("Giá trị tuổi không hợp lệ.");
                    }
                } catch (NumberFormatException e) {
                    errorMessages.add("Giá trị tuổi không hợp lệ.");
                }

                // Kiểm tra giá trị status
                if (!status.equals("Active") && !status.equals("Inactive")) {
                    errorMessages.add("Trạng thái không hợp lệ.");
                }

                // Kiểm tra trùng lặp với CustomerID trong database
                if (customerRepository.existsByCustomerId(customerId)) {
                    errorMessages.add("Trùng lặp CustomerID.");
                }

                // Nếu có lỗi trong dòng CSV, tiếp tục đọc các dòng khác
                if (!errorMessages.isEmpty()) {
                    continue;
                }

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

        // Nếu danh sách thông báo lỗi không trống, trả về lỗi
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.join("\n", errorMessages));
        }

        // Trả về thành công nếu không có lỗi
        return ResponseEntity.ok("Nhập dữ liệu thành công.");
    }



}
package com.example.superdataworker.services;

import com.example.superdataworker.models.Customer;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    @Override
    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }
    @Override
    public List<Customer> searchByName(String name){
        return customerRepository.findByFirstNameStartingWithOrLastNameStartingWith(name,name);
    }

    @Transactional
    @Override
    public ResponseEntity<String> uploadFileCustomersFromCSV(MultipartFile csvFile) throws IOException {
        List<String> errorMessages = new ArrayList<>();
        List<Customer> validCustomers = new ArrayList<>();
        int lineNumber = 1; // Số dòng bắt đầu từ 1

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1); // Bỏ qua dòng tiêu đề

            while ((nextLine = csvReader.readNext()) != null) {
                lineNumber++; // Tăng số dòng sau mỗi lần đọc

                if (nextLine.length < 6) {
                    // Đảm bảo có ít nhất 6 cột trong dòng CSV trước khi truy cập
                    errorMessages.add("Line " + lineNumber + ": Invalid number of columns in CSV line.");
                    continue;
                }

                String customerId = nextLine[0];
                String address = nextLine[1];
                String ageStr = nextLine[2];
                String firstName = nextLine[3];
                String lastName = nextLine[4];
                String status = nextLine[5];

                // Kiểm tra các điều kiện lỗi
                boolean hasError = false;

                if (customerId == null || customerId.isEmpty()) {
                    errorMessages.add("Line " + lineNumber + ": CustomerID empty.");
                    hasError = true;
                }
                if (firstName == null || firstName.isEmpty()) {
                    errorMessages.add("Line " + lineNumber + ": FirstName empty.");
                    hasError = true;
                }
                if (lastName == null || lastName.isEmpty()) {
                    errorMessages.add("Line " + lineNumber + ": LastName empty.");
                    hasError = true;
                }
                if (address == null) {
                    errorMessages.add("Line " + lineNumber + ": Address empty.");
                    hasError = true;
                }
                if (ageStr == null) {
                    errorMessages.add("Line " + lineNumber + ": Age empty.");
                    hasError = true;
                }
                if (status == null) {
                    errorMessages.add("Line " + lineNumber + ": Status empty.");
                    hasError = true;
                }

                int age = 0;
                try {
                    age = Integer.parseInt(ageStr);
                    if (age < 1 || age > 150) {
                        errorMessages.add("Line " + lineNumber + ": Invalid age value.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    errorMessages.add("Line " + lineNumber + ": Invalid age value.");
                    hasError = true;
                }

                if (!status.equals("Active") && !status.equals("Inactive")) {
                    errorMessages.add("Line " + lineNumber + ": Invalid status value.");
                    hasError = true;
                }
                if (customerRepository.existsById(customerId)) {
                    errorMessages.add("Line " + lineNumber + ": CustomerId existed.\n");
                    hasError = true;
                }

                if (hasError) {
                    continue; // Bỏ qua hàng nếu có lỗi
                }

                // Tạo đối tượng Customer và thêm vào danh sách validCustomers
                Customer customer = new Customer();
                customer.setCustomerId(customerId);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setAddress(address);
                customer.setAge(age);
                customer.setStatus(status);

                validCustomers.add(customer);
            }

            // Lưu các đối tượng hợp lệ vào cơ sở dữ liệu
            customerRepository.saveAll(validCustomers);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.join("\n", errorMessages));
        }

        return ResponseEntity.ok("upload file success.");
    }


}

package com.example.superdataworker.services;

import com.example.superdataworker.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    public void testUploadFileCustomersFromCSV_Success() throws IOException {
        // Tạo dữ liệu CSV ảo
        String csvData = "customer_id,address,age,first_name,last_name,status\n" +
                "1,123 Main St,30,John,Doe,Active\n" +
                "2,456 Elm St,25,Jane,Smith,Inactive";

        // Tạo đối tượng MultipartFile từ dữ liệu CSV ảo
        MultipartFile csvFile = new MockMultipartFile("customers.csv", csvData.getBytes());

        // Khi gọi phương thức uploadFileCustomersFromCSV
        ResponseEntity<String> response = customerService.uploadFileCustomersFromCSV(csvFile);

        // Kiểm tra kết quả trả về
        assertEquals("upload file success.", response.getBody());
    }

    @Test
    public void testUploadFileCustomersFromCSV_InvalidData() throws IOException {
        // Tạo dữ liệu CSV ảo với dòng lỗi
        String csvData = "customer_id,address,age,first_name,last_name,status\n" +
                "1,123 Main St,30,John,Doe,Active\n" +
                "2,456 Elm St,,Jane,,Inactive"; // Dòng này có dữ liệu không hợp lệ

        // Tạo đối tượng MultipartFile từ dữ liệu CSV ảo
        MultipartFile csvFile = new MockMultipartFile("customers.csv", csvData.getBytes());

        // Khi gọi phương thức uploadFileCustomersFromCSV
        ResponseEntity<String> response = customerService.uploadFileCustomersFromCSV(csvFile);

        // Kiểm tra kết quả trả về
        assertTrue(response.getBody().contains("Invalid number of columns in CSV line."));
        assertTrue(response.getBody().contains("Age empty."));
        assertTrue(response.getBody().contains("Invalid age value."));
        assertTrue(response.getBody().contains("LastName empty."));
    }

    // Thêm các test case khác tùy theo yêu cầu của bạn

}

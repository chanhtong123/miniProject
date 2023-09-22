package com.example.superdataworker.controllers;
import com.example.superdataworker.controllers.CustomerController;
import com.example.superdataworker.models.Customer;
import com.example.superdataworker.responses.ResponseMessage;
import com.example.superdataworker.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CustomerControllerTest {

    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController = new CustomerController(customerService);
    }

    @Test
    public void testGetAllCustomers() {
        // Tạo danh sách giả lập của khách hàng
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("1", "John", "Doe","Ho Chi Minh", 4, "Active"));
        customers.add(new Customer("2", "Jane", "Smith", "Ho Chi Minh", 4, "Active"));

        // Giả lập phương thức getAllCustomers của CustomerService
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Gọi phương thức trên controller
        ResponseEntity<ResponseMessage> response = customerController.getAllCustomers();

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(customers, response.getBody().getData());
    }

    @Test
    public void testUploadFileCustomersFromCSV() throws IOException {
        // Tạo một mẫu file CSV giả lập để kiểm thử
        byte[] csvContent = "customer_id,first_name,last_name\n1,John,Doe\n2,Jane,Smith".getBytes();

        // Tạo đối tượng MockMultipartFile từ mẫu file CSV
        MultipartFile csvFile = new MockMultipartFile(
                "csvFile", // Thay "csvFile" bằng tên tham số trong phương thức controller
                "sample.csv", // Tên file
                "text/csv", // Loại file
                csvContent // Dữ liệu của file
        );

        // Giả lập phương thức uploadFileCustomersFromCSV của CustomerService
        when(customerService.uploadFileCustomersFromCSV(csvFile)).thenReturn(
                ResponseEntity.ok("Upload file success.")
        );

        // Gọi phương thức trên controller và kiểm tra kết quả
        ResponseEntity<String> response = customerController.uploadFileCustomersFromCSV(csvFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Upload file success.", response.getBody());
    }
    // Thêm các test case khác tùy theo yêu cầu của bạn

}

package com.example.superdataworker.services;

import com.example.superdataworker.models.Apartment;
import com.example.superdataworker.models.Customer;
import com.example.superdataworker.repositories.ApartmentRepository;
import com.example.superdataworker.repositories.ContractRepository;
import com.example.superdataworker.repositories.CustomerRepository;
import com.example.superdataworker.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class ContractServiceTest {

    private ContractService contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        contractService = new ContractServiceImpl(contractRepository, customerRepository, apartmentRepository);
    }

    @Test
    public void testUploadFileContractsFromCSV_Success() throws IOException {
        // Mock data for CSV file
        String csvData = "ContractId,EndDate,StartDate,ApartmentId,CustomerId\n" +
                "C001,01/01/2023,01/12/2022,A101,C101\n" +
                "C002,01/03/2023,01/02/2023,A102,C102\n";

        MultipartFile multipartFile = TestUtils.createMultipartFile("contracts.csv", csvData.getBytes());

        // Mock data for existing customers and apartments
        Customer customer1 = new Customer("C101", "John", "Doe", "123 Main St", 30, "Active");
        Apartment apartment1 = new Apartment("A101", "456 Elm St", "200", 1000);

        Mockito.when(customerRepository.findByCustomerId("C101")).thenReturn(customer1);
        Mockito.when(apartmentRepository.findByApartmentId("A101")).thenReturn(apartment1);

        ResponseEntity<String> response = contractService.uploadFileContractsFromCSV(multipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Upload file success.", response.getBody());
    }

    @Test
    public void testUploadFileContractsFromCSV_InvalidData() throws IOException {
        // Mock data for CSV file with invalid data
        String csvData = "ContractId,EndDate,StartDate,ApartmentId,CustomerId\n" +
                "C001,01/01/2023,01-12-2022,A101,C101\n" + // Invalid date format
                "C002,01/03/2023,01/02/2023,A102,C102\n";

        MultipartFile multipartFile = TestUtils.createMultipartFile("contracts.csv", csvData.getBytes());

        ResponseEntity<String> response = contractService.uploadFileContractsFromCSV(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid date format in CSV line."));
    }

    @Test
    public void testUploadFileContractsFromCSV_CustomerNotFound() throws IOException {
        // Mock data for CSV file with non-existing customer
        String csvData = "ContractId,EndDate,StartDate,ApartmentId,CustomerId\n" +
                "C001,01/01/2023,01/12/2022,A101,C101\n" +
                "C002,01/03/2023,01/02/2023,A102,C102\n";

        MultipartFile multipartFile = TestUtils.createMultipartFile("contracts.csv", csvData.getBytes());

        // Mock existing apartment
        Apartment apartment1 = new Apartment("A101", "456 Elm St", "300", 1000);

        Mockito.when(apartmentRepository.findByApartmentId("A101")).thenReturn(apartment1);
        // Customer with ID "C101" does not exist in the database

        ResponseEntity<String> response = contractService.uploadFileContractsFromCSV(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("CustomerId not exist."));
    }

    @Test
    public void testUploadFileContractsFromCSV_ApartmentNotFound() throws IOException {
        // Mock data for CSV file with non-existing apartment
        String csvData = "ContractId,EndDate,StartDate,ApartmentId,CustomerId\n" +
                "C001,01/01/2023,01/12/2022,A101,C101\n" +
                "C002,01/03/2023,01/02/2023,A102,C102\n";

        MultipartFile multipartFile = TestUtils.createMultipartFile("contracts.csv", csvData.getBytes());

        // Mock existing customer
        Customer customer1 = new Customer("C101", "John", "Doe", "123 Main St", 30, "Active");

        Mockito.when(customerRepository.findByCustomerId("C101")).thenReturn(customer1);
        // Apartment with ID "A101" does not exist in the database

        ResponseEntity<String> response = contractService.uploadFileContractsFromCSV(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("ApartmentId not exist."));
    }

    @Test
    public void testUploadFileContractsFromCSV_DuplicateContractId() throws IOException {
        // Mock data for CSV file with duplicate ContractId
        String csvData = "ContractId,EndDate,StartDate,ApartmentId,CustomerId\n" +
                "C001,01/01/2023,01/12/2022,A101,C101\n" +
                "C001,01/03/2023,01/02/2023,A102,C102\n"; // Duplicate ContractId

        MultipartFile multipartFile = TestUtils.createMultipartFile("contracts.csv", csvData.getBytes());

        // Mock existing customer and apartment
        Customer customer1 = new Customer("C101", "John", "Doe", "123 Main St", 30, "Active");
        Apartment apartment1 = new Apartment("A101", "456 Elm St", "2000", 100);

        Mockito.when(customerRepository.findByCustomerId("C101")).thenReturn(customer1);
        Mockito.when(apartmentRepository.findByApartmentId("A101")).thenReturn(apartment1);
        Mockito.when(contractRepository.existsById("C001")).thenReturn(true);

        ResponseEntity<String> response = contractService.uploadFileContractsFromCSV(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("ContractId existed."));
    }
}

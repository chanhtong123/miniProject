package com.example.superdataworker.services;

import com.example.superdataworker.models.Apartment;
import com.example.superdataworker.repositories.ApartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

public class ApartmentServiceTest {

    private ApartmentServiceImpl apartmentService;

    @Mock
    private ApartmentRepository apartmentRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        apartmentService = new ApartmentServiceImpl(apartmentRepository);
    }

    @Test
    public void testGetAllApartments() {
        // Mock data
        Apartment apartment1 = new Apartment("A101", "123 Main St", "200", 1000);
        Apartment apartment2 = new Apartment("B202", "456 Elm St", "100", 1500);
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(apartment1);
        apartments.add(apartment2);

        // Mock apartmentRepository behavior
        Mockito.when(apartmentRepository.findAll()).thenReturn(apartments);

        // Call the service method
        List<Apartment> result = apartmentService.getAllApartments();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals(apartment1, result.get(0));
        assertEquals(apartment2, result.get(1));
    }

    @Test
    public void testSaveApartment() {
        // Mock data
        Apartment apartment = new Apartment("C303", "789 Oak St", "200", 2000);

        // Mock apartmentRepository behavior
        Mockito.when(apartmentRepository.save(apartment)).thenReturn(apartment);

        // Call the service method
        Apartment savedApartment = apartmentService.save(apartment);

        // Verify the result
        assertEquals(apartment, savedApartment);
    }

    @Test
    public void testUploadFileApartmentsFromCSV_ValidFile() throws IOException {
        // Mock CSV data
        String csvData = "apartmentId,address,numberOfRoom,rentalPrice\n" +
                "A101,123 Main St,2,1000.00\n" +
                "B202,456 Elm St,3,1500.00\n";
        MockMultipartFile file = new MockMultipartFile("file", "apartments.csv", "text/csv", csvData.getBytes());

        // Mock apartmentRepository behavior
        Mockito.when(apartmentRepository.existsById(Mockito.anyString())).thenReturn(false);

        // Call the service method
        ResponseEntity<String> response = apartmentService.uploadFileApartmentsFromCSV(file);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Upload file success.", response.getBody());
    }

    @Test
    public void testUploadFileApartmentsFromCSV_InvalidFile() throws IOException {
        // Mock invalid CSV data
        String csvData = "apartmentId,address,numberOfRoom,rentalPrice\n" +
                "A101,123 Main St,2\n" +  // Missing rentalPrice
                "B202,456 Elm St,3,1500.00\n";
        MockMultipartFile file = new MockMultipartFile("file", "apartments.csv", "text/csv", csvData.getBytes());

        // Call the service method
        ResponseEntity<String> response = apartmentService.uploadFileApartmentsFromCSV(file);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Line 1: Invalid number of columns in CSV line.\n" +
                "Line 1: RentalPrice empty.\n", response.getBody());
    }

    // Add more test cases for different scenarios...
}

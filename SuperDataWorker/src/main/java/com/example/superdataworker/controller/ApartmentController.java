package com.example.superdataworker.controller;

import com.example.superdataworker.repository.ApartmentRepository;
import com.example.superdataworker.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentRepository apartmentRepository, ApartmentService apartmentService) {
        this.apartmentRepository = apartmentRepository;
        this.apartmentService = apartmentService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            apartmentService.importApartmentsFromCSV(csvFile);
            return ResponseEntity.status(HttpStatus.OK).body("Dữ liệu đã được nhập thành công từ tệp CSV vào table Apartment.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi nhập dữ liệu từ tệp CSV vào table Apartment: " + e.getMessage());
        }
    }
}

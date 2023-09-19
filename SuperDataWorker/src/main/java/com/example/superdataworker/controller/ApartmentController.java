package com.example.superdataworker.controller;

import com.example.superdataworker.model.Apartment;
import com.example.superdataworker.repository.ApartmentRepository;
import com.example.superdataworker.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/getAllApartment")
    public List<Apartment> getAllApartment(){
        return apartmentRepository.getAllApartment();
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFileCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            apartmentService.uploadFileApartmentsFromCSV(csvFile);
            return ResponseEntity.status(HttpStatus.OK).body("Dữ liệu đã được nhập thành công từ tệp CSV vào table Apartment.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi nhập dữ liệu từ tệp CSV vào table Apartment: " + e.getMessage());
        }
    }
}

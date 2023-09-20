package com.example.superdataworker.controllers;

import com.example.superdataworker.models.Apartment;
import com.example.superdataworker.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/apartment")
public class ApartmentController {


    private final ApartmentService apartmentService;

    @Autowired
    public ApartmentController( ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @GetMapping("/get-all-apartment")
    public List<Apartment> getAllApartment(){
        return apartmentService.getAllApartment();
    }

    @PostMapping("/up-load-file")
    public ResponseEntity<String> uploadFileCustomersFromCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            ResponseEntity<String> response = apartmentService.uploadFileApartmentsFromCSV(csvFile);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        }
    }
}

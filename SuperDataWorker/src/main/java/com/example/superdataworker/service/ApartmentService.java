package com.example.superdataworker.service;

import com.example.superdataworker.model.Apartment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ApartmentService {
     List<Apartment> getAllApartments();
     Apartment save(Apartment apartment);

     ResponseEntity<String> uploadFileApartmentsFromCSV(MultipartFile csvFile) throws IOException;
}

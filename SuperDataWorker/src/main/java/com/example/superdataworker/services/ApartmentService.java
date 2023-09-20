package com.example.superdataworker.services;

import com.example.superdataworker.models.Apartment;
import com.example.superdataworker.repositorys.ApartmentRepository;
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
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public List<Apartment> getAllApartment(){
        return apartmentRepository.findAll();
    }

    public Apartment save(Apartment apartment){
        return apartmentRepository.save(apartment);
    }

    @Transactional
    public ResponseEntity<String> uploadFileApartmentsFromCSV(MultipartFile csvFile) throws IOException {
        List<String> errorMessages = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1);

            while ((nextLine = csvReader.readNext()) != null) {
                String apartmentId = nextLine[0];
                String address = nextLine[1];
                String rentalPrice = nextLine[3];
                int numberOfRoom = Integer.parseInt(nextLine[2]);


                if(apartmentId ==null || apartmentId.isEmpty()){
                    errorMessages.add("ApartmentId empty.");
                }
                if(address == null || address.isEmpty()){
                    errorMessages.add("address empty.");
                }
                if (rentalPrice == null || rentalPrice.isEmpty()){
                    errorMessages.add("rentalPrice empty.");
                }
                if (numberOfRoom <0){
                    errorMessages.add("invalid numberOfRoom value.");
                }
                if(apartmentRepository.existsById(apartmentId)){
                    errorMessages.add("ApartmentId existed");
                }

                if (!errorMessages.isEmpty()) {
                    continue;
                }

                Apartment apartment = new Apartment();
                apartment.setApartmentId(apartmentId);
                apartment.setAddress(address);
                apartment.setRentalPrice(rentalPrice);
                apartment.setNumberOfRoom(numberOfRoom);

                apartmentRepository.save(apartment);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.join("\n", errorMessages));
        }


        return ResponseEntity.ok("upload file success.");
    }
}

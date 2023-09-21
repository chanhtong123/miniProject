package com.example.superdataworker.service;

import com.example.superdataworker.model.Apartment;
import com.example.superdataworker.repository.ApartmentRepository;
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
public class ApartmentServiceImpl implements ApartmentService {


    @Autowired
    private ApartmentRepository apartmentRepository;
    public List<Apartment> getAllApartments(){
        return apartmentRepository.findAll();
    }

    public Apartment save(Apartment apartment){
        return apartmentRepository.save(apartment);
    }

    @Transactional
    public ResponseEntity<String> uploadFileApartmentsFromCSV(MultipartFile csvFile) throws IOException {
        List<String> errorMessages = new ArrayList<>();
        List<Apartment> validApartments = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1); // Bỏ qua dòng tiêu đề

            int lineNumber = 1; // Số dòng bắt đầu từ 1

            while ((nextLine = csvReader.readNext()) != null) {
                lineNumber++; // Tăng số dòng sau mỗi lần đọc

                if (nextLine.length < 4) {
                    // Đảm bảo có ít nhất 4 cột trong dòng CSV trước khi truy cập
                    errorMessages.add("Line " + lineNumber + ": Invalid number of columns in CSV line.\n");
                    continue;
                }

                String apartmentId = nextLine[0];
                String address = nextLine[1];
                String numberOfRoomStr = nextLine[2];
                String rentalPrice = nextLine[3];

                // Kiểm tra các điều kiện lỗi
                boolean hasError = false;

                if (apartmentId == null || apartmentId.isEmpty()) {
                    errorMessages.add("Line " + lineNumber + ": ApartmentId empty.\n");
                    hasError = true;
                }
                if (address == null || address.isEmpty()) {
                    errorMessages.add("Line " + lineNumber + ": Address empty.\n");
                    hasError = true;
                }
                if (rentalPrice == null || rentalPrice.isEmpty()) {
                    errorMessages.add("Line " + lineNumber + ": RentalPrice empty.\n");
                    hasError = true;
                }

                int numberOfRoom = 0;
                try {
                    numberOfRoom = Integer.parseInt(numberOfRoomStr);
                    if (numberOfRoom < 0) {
                        errorMessages.add("Line " + lineNumber + ": Invalid numberOfRoom value.\n");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    errorMessages.add("Line " + lineNumber + ": Invalid numberOfRoom value.\n");
                    hasError = true;
                }

                if (apartmentRepository.existsById(apartmentId)) {
                    errorMessages.add("Line " + lineNumber + ": ApartmentId existed.\n");
                    hasError = true;
                }

                if (hasError) {
                    continue; // Bỏ qua hàng nếu có lỗi
                }

                // Tạo đối tượng Apartment và thêm vào danh sách validApartments
                Apartment apartment = new Apartment();
                apartment.setApartmentId(apartmentId);
                apartment.setAddress(address);
                apartment.setRentalPrice(rentalPrice);
                apartment.setNumberOfRoom(numberOfRoom);

                validApartments.add(apartment);
            }

            // Lưu các đối tượng hợp lệ vào cơ sở dữ liệu
            apartmentRepository.saveAll(validApartments);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.join("", errorMessages));
        }

        return ResponseEntity.ok("Upload file success.");
    }


}

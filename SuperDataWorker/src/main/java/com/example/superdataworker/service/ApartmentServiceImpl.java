package com.example.superdataworker.services;

import com.example.superdataworker.model.Apartment;
import com.example.superdataworker.repository.ApartmentRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;


    @Transactional
    @Override
    public void uploadFileApartmentsFromCSV(MultipartFile csvFile) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            csvReader.skip(1); // Bỏ qua dòng tiêu đề

            while ((nextLine = csvReader.readNext()) != null) {
                String apartmentId = nextLine[0];
                String address = nextLine[1];
                String rentalPrice = nextLine[2];
                int numberOfRoom = 0; // Giá trị mặc định

                try {
                    numberOfRoom = Integer.parseInt(nextLine[3]);
                } catch (NumberFormatException e) {
                    // Xử lý trường hợp không thể chuyển đổi numberOfRoom thành số nguyên
                    numberOfRoom = 0; // Hoặc một giá trị mặc định khác tùy theo trường hợp của bạn
                }

                // Tạo đối tượng Apartment và lưu vào cơ sở dữ liệu
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
    }
    @Override
    public List<Apartment> getAllApartment() {

        return apartmentRepository.findAll();
    }
}

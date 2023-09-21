package com.example.superdataworker.controller;

import com.example.superdataworker.response.ResponseMessage;
import com.example.superdataworker.service.ApartmentService;
import com.example.superdataworker.service.ContractService;
import com.example.superdataworker.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {
    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContractService contractService;


    private final Logger logger = (Logger) LogManager.getLogger(MainController.class);

    @GetMapping("/")
    public String viewHome(Model model) {
        model.addAttribute("listApartments", apartmentService.getAllApartments());
        model.addAttribute("listCustomers", customerService.getAllCustomers());
        model.addAttribute("ListContracts", contractService.getAllContracts());

        return "index";
    }


    @PostMapping("/upload-test")
    public ResponseEntity<ResponseMessage> uploadFiles(MultipartFile[] csvFile) {
        logger.info("Start to save data from files");

        List<String> errorMessages = new ArrayList<>(); // Sử dụng danh sách để tổng hợp tất cả thông báo lỗi

        boolean success = true;

        try {
            for (MultipartFile file : csvFile) {
                String fileName = file.getOriginalFilename();
                if (fileName.startsWith("Customer")) {
                    ResponseEntity<String> result = customerService.uploadFileCustomersFromCSV(file);
                    if (result.getStatusCodeValue() != 200) {
                        success = false;
                        errorMessages.add(result.getBody()); // Lấy thông báo lỗi từ ResponseEntity
                    }
                } else if (fileName.startsWith("Apartment")) {
                    ResponseEntity<String> result = apartmentService.uploadFileApartmentsFromCSV(file);
                    if (result.getStatusCodeValue() != 200) {
                        success = false;
                        errorMessages.add(result.getBody()); // Lấy thông báo lỗi từ ResponseEntity
                    }
                } else if (fileName.startsWith("Contract")) {
                    ResponseEntity<String> result = contractService.uploadFileContractsFromCSV(file);
                    if (result.getStatusCodeValue() != 200) {
                        success = false;
                        errorMessages.add(result.getBody()); // Lấy thông báo lỗi từ ResponseEntity
                    }
                }
            }

            if (success) {
                return ResponseEntity.ok(
                        ResponseMessage.builder()
                                .statusCode(200)
                                .message("Saved data from files successfully")
                                .data(null)
                                .build()
                );
            } else {
                return ResponseEntity.status(201)
                        .body(
                                ResponseMessage.builder()
                                        .statusCode(201)
                                        .message("One or more files failed to process.")
                                        .data(errorMessages) // Truyền thông báo lỗi về client
                                        .build()
                        );
            }
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.internalServerError()
                    .body(
                            ResponseMessage.builder()
                                    .statusCode(500)
                                    .message(e.getMessage())
                                    .data(null)
                                    .build()
                    );
        }
    }

    @PostMapping("/upload")
    public String uploadFiles(MultipartFile[] csvFile, Model model) {
        // Xử lý tệp và lấy thông báo lỗi
        List<String> errorMessages = new ArrayList<>();

        try {
            for (MultipartFile file : csvFile) {
                String fileName = file.getOriginalFilename();
                if (fileName.startsWith("Customer")) {
                    // process customer file
                    ResponseEntity<String> data = customerService.uploadFileCustomersFromCSV(file);
                    if (data != null && data.getStatusCode() != HttpStatus.OK) {
                        errorMessages.add(data.getBody()); // Thêm thông báo lỗi vào danh sách lỗi
                    }
                } else if (fileName.startsWith("Apartment")) {
                    // process apartment file
                    ResponseEntity<String> data = apartmentService.uploadFileApartmentsFromCSV(file);
                    if (data != null && data.getStatusCode() != HttpStatus.OK) {
                        errorMessages.add(data.getBody()); // Thêm thông báo lỗi vào danh sách lỗi
                    }
                } else if (fileName.startsWith("Contract")) {
                    // process contract file
                    ResponseEntity<String> data = contractService.uploadFileContractsFromCSV(file);
                    if (data != null && data.getStatusCode() != HttpStatus.OK) {
                        errorMessages.add(data.getBody()); // Thêm thông báo lỗi vào danh sách lỗi
                    }
                }
            }
        } catch (Exception e) {
            errorMessages.add(e.getMessage());
        }

        // Cập nhật danh sách dữ liệu trong model
        model.addAttribute("listApartments", apartmentService.getAllApartments());
        model.addAttribute("listCustomers", customerService.getAllCustomers());
        model.addAttribute("ListContracts", contractService.getAllContracts());

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages); // Truyền thông báo lỗi vào Model
            return "index"; // Trả về trang tải tệp nếu có lỗi
        } else {
            // Gọi lại các phương thức để lấy dữ liệu mới sau khi upload thành công
            model.addAttribute("successMessage", "Upload file successfully");

            return "index"; // Tên trang HTML Thymeleaf cho kết quả thành công
        }
    }

}



package com.example.superdataworker.services;

import com.example.superdataworker.models.Apartment;
import com.example.superdataworker.models.Contract;
import com.example.superdataworker.models.Customer;
import com.example.superdataworker.repositorys.ApartmentRepository;
import com.example.superdataworker.repositorys.ContractRepository;
import com.example.superdataworker.repositorys.CustomerRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {

}
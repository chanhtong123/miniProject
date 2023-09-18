package com.example.superdataworker.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Apartment")
@Data
public class Apartment {
    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ApartmentId", columnDefinition = "VARCHAR(36)")
    private String apartmentId;

    @Column(name = "Address")
    private String address;

    @Column(name = "RentalPrice")
    private String rentalPrice;

    @Column (name = "NumberOfRoom")
    private int numberOfRoom;

}


package com.example.superdataworker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "apartment")
@Data
public class Apartment {
    @Id
    @Column(name = "ApartmentId", nullable = false, length = 50)
    private String apartmentId;

    @Column(name = "Address", length = 200)
    private String address;

    @Column(name = "RentalPrice", length = 50)
    private String rentalPrice;

    @Column (name = "NumberOfRoom", length = 200)
    private int numberOfRoom;

}


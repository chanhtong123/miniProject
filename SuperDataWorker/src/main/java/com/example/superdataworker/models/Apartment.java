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
@Table(name = "Apartment", schema="SuperDataWorker")
@Data
public class Apartment {
    @Id
    @Column(name = "apartmentId", nullable = false, length = 50)
    private String apartmentId;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "rentalPrice", length = 50)
    private String rentalPrice;

    @Column (name = "numberOfRoom", length = 200)
    private int numberOfRoom;

}


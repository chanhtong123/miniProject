package com.example.superdataworker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Contract", schema="SuperDataWorker")
@Data
public class Contract {
    @Id
    @Column(name ="contractId", nullable = false, length = 50)
    private String contractId;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "apartmentId")
    private Apartment apartment;

    @Column(name = "startDate" )
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;


}

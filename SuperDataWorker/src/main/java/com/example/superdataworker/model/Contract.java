package com.example.superdataworker.model;

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
@Table(name = "contract")
@Data
public class Contract {
    @Id
    @Column(name ="ContractId", nullable = false, length = 50)
    private String contractId;

    @ManyToOne
    @JoinColumn(name = "CustomerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "ApartmentId")
    private Apartment apartment;

    @Column(name = "StartDate" )
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;


}

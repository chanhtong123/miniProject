package com.example.superdataworker.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Contract")
@Data
public class Contract {
    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ContractId", columnDefinition = "VARCHAR(36)")
    private String contractId;

    @Column(name = "CustomerId")
    private String customerId;

    @Column(name = "ApartmentId")
    private String apartmentId;

    @Column(name = "StartDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "EndDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;


}

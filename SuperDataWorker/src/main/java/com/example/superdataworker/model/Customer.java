package com.example.superdataworker.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Customer")
@Data
public class Customer {
    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "CustomerId", columnDefinition = "VARCHAR(36)")
    private String customerId;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Address")
    private String address;

    @Column(name = "Age")
    private int age;

    @Column(name = "Status")
    private String status;


}

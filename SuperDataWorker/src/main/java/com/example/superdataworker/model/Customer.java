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
@Table(name = "customer")
@Data
public class Customer {
    @Id
    @Column(name="CustomerId", nullable = false, length = 50)
    private String customerId;

    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Column(name = "LastName", length = 50)
    private String lastName;

    @Column(name = "Address", length = 50)
    private String address;

    @Column(name = "Age", length = 50)
    private int age;

    @Column(name = "Status", length = 50)
    private String status;


}

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
@Data
@Table(name = "Customer", schema="SuperDataWorker")
public class Customer {
    @Id
    @Column(name="customerId", nullable = false, length = 50)
    private String customerId;

    @Column(name = "firstName", length = 50)
    private String firstName;

    @Column(name = "lastName", length = 50)
    private String lastName;

    @Column(name = "address", length = 50)
    private String address;

    @Column(name = "age", length = 50)
    private int age;

    @Column(name = "status", length = 50)
    private String status;


}

package com.aadil.grpc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class EmployeeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double salary;
    private String address;
    private boolean isActive;
    private byte[] profilePicture;
    private LocalDateTime joinDate;

    public void setIsActive(boolean isActive) {
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

}
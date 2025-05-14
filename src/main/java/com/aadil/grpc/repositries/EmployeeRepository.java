package com.aadil.grpc.repositries;

import com.aadil.grpc.entities.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeDetails, Integer> {
}

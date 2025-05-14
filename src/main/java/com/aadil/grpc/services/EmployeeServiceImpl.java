package com.aadil.grpc.services;


import com.aadil.grpc.*;
import com.aadil.grpc.entities.EmployeeDetails;
import com.aadil.grpc.exceptions.EmployeeNotFoundException;
import com.aadil.grpc.exceptions.GrpcExceptionHandler;
import com.aadil.grpc.repositries.EmployeeRepository;
import com.aadil.grpc.utils.GrpcDateTimeUtil;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class EmployeeServiceImpl extends EmployeeServiceGrpc.EmployeeServiceImplBase {

    private final EmployeeRepository employeeRepository;

    @Override
    public void addEmployee(Employee request, StreamObserver<Employee> responseObserver) {

        EmployeeDetails employeeDetails = EmployeeDetails.builder()
                .name(request.getName())
                .salary(request.getSalary())
                .address(request.getAddress())
                .isActive(request.getIsActive())
                .profilePicture(request.getProfilePicture().toByteArray())
                .joinDate(LocalDateTime.now())
                .build();
        employeeRepository.save(employeeDetails);

        Employee response = Employee.newBuilder()
                .setName(employeeDetails.getName())
                .setSalary(employeeDetails.getSalary())
                .setAddress(employeeDetails.getAddress())
                .setIsActive(employeeDetails.getIsActive())
                .setProfilePicture(ByteString.copyFrom(employeeDetails.getProfilePicture()))
                .setJoinDate(GrpcDateTimeUtil.toGrpcTimestamp(employeeDetails.getJoinDate()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEmployeeById(EmployeeIdRequest request, StreamObserver<Employee> responseObserver) {
        try {
            Optional<EmployeeDetails> detailsOptional = employeeRepository.findById(request.getId());
            detailsOptional.ifPresentOrElse(
                    employeeDetails -> {
                        Employee response = Employee.newBuilder()
                                .setId(employeeDetails.getId())
                                .setName(employeeDetails.getName())
                                .setSalary(employeeDetails.getSalary())
                                .setAddress(employeeDetails.getAddress())
                                .setIsActive(employeeDetails.getIsActive())
                                .setProfilePicture(ByteString.copyFrom(employeeDetails.getProfilePicture()))
                                .setJoinDate(GrpcDateTimeUtil.toGrpcTimestamp(employeeDetails.getJoinDate()))
                                .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    },
                    () -> {
                        throw new EmployeeNotFoundException("Employee not found with id: " + request.getId());
                    }
            );
        } catch (Exception e) {
            GrpcExceptionHandler.handleException(e, responseObserver);
        }
    }

    @Override
    public void getAllEmployees(Empty request, StreamObserver<EmployeeList> responseObserver) {
        try {
            List<EmployeeDetails> allEmployees = employeeRepository.findAll();

            List<Employee> employeeList = allEmployees.stream().map(employee -> {
                return Employee.newBuilder()
                        .setId(employee.getId())
                        .setName(employee.getName())
                        .setSalary(employee.getSalary())
                        .setAddress(employee.getAddress())
                        .setIsActive(employee.getIsActive())
                        .setProfilePicture(employee.getProfilePicture() != null
                                ? ByteString.copyFrom(employee.getProfilePicture())
                                : ByteString.EMPTY)
                        .setJoinDate(GrpcDateTimeUtil.toGrpcTimestamp(employee.getJoinDate()))
                        .build();
            }).toList();

            EmployeeList employeeListResponse = EmployeeList.newBuilder()
                    .addAllEmployee(employeeList)
                    .build();

            responseObserver.onNext(employeeListResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            GrpcExceptionHandler.handleException(e, responseObserver);
        }
    }

    @Override
    public void deleteEmployeeById(EmployeeIdRequest request, StreamObserver<DeleteEmployeeResponse> responseObserver) {
        try {
            int id = request.getId();
            if (!employeeRepository.existsById(id)) {
                throw new EmployeeNotFoundException("Employee not found with id: " + id);
            }
            employeeRepository.deleteById(id);
            DeleteEmployeeResponse response = DeleteEmployeeResponse.newBuilder()
                    .setMessage("Employee deleted successfully with id: " + id)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            GrpcExceptionHandler.handleException(e, responseObserver);
        }
    }

    @Override
    public void updateEmployeeById(Employee request, StreamObserver<Employee> responseObserver) {
        try {
            Optional<EmployeeDetails> detailsOptional = employeeRepository.findById(request.getId());
            detailsOptional.ifPresentOrElse(details -> {
                details.setName(request.getName());
                details.setSalary(request.getSalary());
                details.setAddress(request.getAddress());
                details.setIsActive(request.getIsActive());
                details.setProfilePicture(request.getProfilePicture().toByteArray());
                details.setJoinDate(GrpcDateTimeUtil.toLocalDateTime(request.getJoinDate()));

                // Save updated entity
                employeeRepository.save(details);

                // Build gRPC response
                Employee response = Employee.newBuilder()
                        .setId(details.getId())
                        .setName(details.getName())
                        .setSalary(details.getSalary())
                        .setAddress(details.getAddress())
                        .setIsActive(details.getIsActive())
                        .setProfilePicture(ByteString.copyFrom(details.getProfilePicture()))
                        .setJoinDate(GrpcDateTimeUtil.toGrpcTimestamp(details.getJoinDate()))
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }, () -> {
                throw new EmployeeNotFoundException("Employee not found with id: " + request.getId());
            });

        } catch (Exception e) {
            GrpcExceptionHandler.handleException(e, responseObserver);
        }
    }

}

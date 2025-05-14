package com.aadil.grpc.exceptions;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GrpcExceptionHandler {

    public static void handleException(Exception e, StreamObserver<?> responseObserver) {
        if (e instanceof EmployeeNotFoundException) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asException());
        } else {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Unexpected error occurred")
                    .augmentDescription(e.getMessage())
                    .withCause(e)
                    .asException());
        }
    }
}

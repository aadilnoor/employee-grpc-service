package com.aadil.grpc.utils;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class GrpcDateTimeUtil {

    // Convert gRPC Timestamp to LocalDateTime
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) return null;
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // Convert LocalDateTime to gRPC Timestamp
    public static Timestamp toGrpcTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Timestamps.fromMillis(
                localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
        );
    }
}

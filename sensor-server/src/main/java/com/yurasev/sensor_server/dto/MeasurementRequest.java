package com.yurasev.sensor_server.dto;

import lombok.Data;

@Data
public class MeasurementRequest {
    private double value;
    private boolean raining;
}

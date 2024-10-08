package com.yurasev.sensor_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeasurementDto {
    @NotNull
    private double temperature;

    @NotNull
    private boolean raining;
}

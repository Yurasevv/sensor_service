package com.yurasev.sensor_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDto {
    @NotNull
    @Min(value = -100)
    @Max(value = 100)
    private double temperature;

    @NotNull
    private boolean raining;
}

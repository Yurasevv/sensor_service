package com.yurasev.sensor_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SensorDto {
    @NotNull
    @Size(min = 3, max = 30, message = "Name should be between 3 and 30 characters")
    private String name;
}

package com.yurasev.sensor_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Sensor sensor;

    @NotNull
    @Min(value = -100)
    @Max(value = 100)
    private double temperature;

    @NotNull
    private boolean raining;

    @NotNull
    private LocalDateTime timestamp;
}

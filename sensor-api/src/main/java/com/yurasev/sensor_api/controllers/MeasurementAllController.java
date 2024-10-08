package com.yurasev.sensor_api.controllers;

import com.yurasev.sensor_api.dto.MeasurementDto;
import com.yurasev.sensor_api.exceptions.SensorErrorResponse;
import com.yurasev.sensor_api.exceptions.SensorException;
import com.yurasev.sensor_api.services.MeasurementService;
import com.yurasev.sensor_api.util.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensors/measurements")
public class MeasurementAllController {

    private final MeasurementService measurementService;
    private final DtoMapper dtoMapper;

    public MeasurementAllController(MeasurementService measurementService, DtoMapper dtoMapper) {
        this.measurementService = measurementService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<MeasurementDto>> getRecentMeasurements() {
        List<MeasurementDto> measurements = measurementService.getRecentMeasurements().stream()
                .map(dtoMapper::convertToDto)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(measurements);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

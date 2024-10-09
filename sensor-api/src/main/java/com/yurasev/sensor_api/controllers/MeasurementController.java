package com.yurasev.sensor_api.controllers;

import com.yurasev.sensor_api.dto.MeasurementDto;
import com.yurasev.sensor_api.exceptions.MeasurementErrorResponse;
import com.yurasev.sensor_api.exceptions.MeasurementException;
import com.yurasev.sensor_api.exceptions.SensorErrorResponse;
import com.yurasev.sensor_api.exceptions.SensorException;
import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.services.MeasurementService;
import com.yurasev.sensor_api.util.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yurasev.sensor_api.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/api/sensors/{key}/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final DtoMapper dtoMapper;

    public MeasurementController(MeasurementService measurementService,DtoMapper dtoMapper) {
        this.measurementService = measurementService;
        this.dtoMapper = dtoMapper;
    }

    @Operation(summary = "Добавление измерений сенсора")
    @PostMapping
    public ResponseEntity<MeasurementDto> addMeasurement(
            @PathVariable String key,
            @RequestBody @Valid MeasurementDto measurementDto,
            BindingResult bindingResult) {

        Measurement measurementToAdd = dtoMapper.convertToEntity(measurementDto);

        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult, measurementToAdd);
        }

        Measurement measurement = measurementService.addMeasurement(key, measurementToAdd);
        return ResponseEntity.ok(dtoMapper.convertToDto(measurement));

    }

    @Operation(summary = "Получения последних 20 измерений сенсора")
    @GetMapping
    public ResponseEntity<List<MeasurementDto>> getLastMeasurements(@PathVariable String key) {

        List<MeasurementDto> measurements = measurementService.getLastMeasurements(key).stream()
                .map(dtoMapper::convertToDto)
                .toList();
        return ResponseEntity.ok(measurements);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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

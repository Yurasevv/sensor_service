package com.yurasev.sensor_api.controllers;

import com.yurasev.sensor_api.dto.SensorDto;
import com.yurasev.sensor_api.exceptions.SensorErrorResponse;
import com.yurasev.sensor_api.exceptions.SensorException;
import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.services.SensorService;
import com.yurasev.sensor_api.util.DtoMapper;
import com.yurasev.sensor_api.util.SensorValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.yurasev.sensor_api.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final DtoMapper dtoMapper;
    private final SensorValidator sensorValidator;

    public SensorController(SensorService sensorService, DtoMapper dtoMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.dtoMapper = dtoMapper;
        this.sensorValidator = sensorValidator;
    }

    @Operation(summary = "Регистрация сенсора")
    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registerSensor(
            @RequestBody @Valid SensorDto sensorDto,
            BindingResult bindingResult) {

        sensorValidator.validate(sensorDto, bindingResult);

        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult, dtoMapper.convertToEntity(sensorDto));
        }

        Sensor sensor = sensorService.registerSensor(sensorDto.getName());
        return ResponseEntity.ok(Map.of("key", sensor.getKey()));
    }

    @Operation(summary = "Получение активных сенсоров. active = true")
    @GetMapping
    public ResponseEntity<List<SensorDto>> getActiveSensors() {
        List<SensorDto> sensors = sensorService.getActiveSensors().stream()
                .map(dtoMapper::convertToDto)
                .toList();
        return ResponseEntity.ok(sensors);
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

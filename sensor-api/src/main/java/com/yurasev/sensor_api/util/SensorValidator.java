package com.yurasev.sensor_api.util;

import com.yurasev.sensor_api.dto.SensorDto;
import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.services.SensorService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {
    private final SensorService sensorService;

    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDto sensor = (SensorDto) target;

        if (sensorService.findByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "", "Thin name is already taken");
        }
    }
}

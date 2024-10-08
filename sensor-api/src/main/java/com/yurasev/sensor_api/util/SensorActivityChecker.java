package com.yurasev.sensor_api.util;

import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.services.MeasurementService;
import com.yurasev.sensor_api.services.SensorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorActivityChecker {

    private final SensorService sensorService;
    private final MeasurementService measurementService;

    public SensorActivityChecker(SensorService sensorService, MeasurementService measurementService) {
        this.sensorService = sensorService;
        this.measurementService = measurementService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkSensorActivity() {
        List<Sensor> sensors = sensorService.getActiveSensors();
        for (Sensor sensor : sensors) {
            List<Measurement> measurements = measurementService.getRecentMeasurementsByName(sensor.getName());
            if (measurements.isEmpty()) {
                sensorService.updateSensorActivity(sensor);
            }
        }
    }
}
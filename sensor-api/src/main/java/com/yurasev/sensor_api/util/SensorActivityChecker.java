package com.yurasev.sensor_api.util;

import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.services.SensorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorActivityChecker {

    private final SensorService sensorService;

    public SensorActivityChecker(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkSensorActivity() {
        List<Sensor> sensors = sensorService.getActiveSensors();
        for (Sensor sensor : sensors) {
            // TODO
            // Логика проверки активности (на основе последних данных)
            // Если данных не было больше минуты, обновить статус активности
            sensorService.updateSensorActivity(sensor);
        }
    }
}
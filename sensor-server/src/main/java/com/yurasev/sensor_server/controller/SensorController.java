package com.yurasev.sensor_server.controller;

import com.yurasev.sensor_server.services.SensorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class SensorController {

    private final SensorService sensorService;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${sensor.name}")
    private String sensorName;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Scheduled(initialDelay = 1000, fixedDelay = Long.MAX_VALUE)
    public void registerSensor() {
        sensorService.registerSensor(serverUrl, sensorName);
    }

    @Scheduled(initialDelay = 15000, fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(3000, 15000)}")
    public void sendMeasurement() {
        sensorService.sendMeasurement(serverUrl, sensorName);
    }
}

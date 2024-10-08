package com.yurasev.sensor_api.services;

import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.repos.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public Sensor registerSensor(String name) {

        if (sensorRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Sensor with this name already exists");
        }

        Sensor sensor = new Sensor();
        sensor.setName(name);
        sensor.setKey(UUID.randomUUID().toString());
        sensor.setActive(true);
        return sensorRepository.save(sensor);
    }

    public Optional<Sensor> findByName(String name) {
        return sensorRepository.findByName(name);
    }

    public List<Sensor> getActiveSensors() {
        return sensorRepository.findByActiveTrue();
    }

    @Transactional
    public void updateSensorActivity(Sensor sensor) {
        sensor.setActive(false);
        sensorRepository.save(sensor);
    }
}

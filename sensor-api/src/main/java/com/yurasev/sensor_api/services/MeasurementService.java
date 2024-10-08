package com.yurasev.sensor_api.services;

import com.yurasev.sensor_api.exceptions.SensorException;
import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.repos.MeasurementRepository;
import com.yurasev.sensor_api.repos.SensorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    public MeasurementService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public Measurement addMeasurement(String key, Measurement measurementToAdd) {

        Optional<Sensor> sensorOpt = sensorRepository.findByKey(key);
        if (sensorOpt.isEmpty()) {
            throw new SensorException("Cant find sensor with key " + key);
        }

        Sensor sensor = sensorOpt.get();
        Measurement measurement = new Measurement();
        measurement.setSensor(sensor);
        measurement.setTemperature(measurementToAdd.getTemperature());
        measurement.setRaining(measurementToAdd.isRaining());
        measurement.setTimestamp(LocalDateTime.now());

        return measurementRepository.save(measurement);
    }

    public List<Measurement> getLastMeasurements(String key) {
        Optional<Sensor> sensorOpt = sensorRepository.findByKey(key);
        if (sensorOpt.isEmpty()) {
            throw new SensorException("Cant find sensor with key " + key);
        }
        Sensor sensor = sensorOpt.get();
        return measurementRepository.findTop20BySensorOrderByTimestampDesc(sensor);
    }

    public List<Measurement> getRecentMeasurements() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        return measurementRepository.findByTimestampAfter(oneMinuteAgo);
    }
}

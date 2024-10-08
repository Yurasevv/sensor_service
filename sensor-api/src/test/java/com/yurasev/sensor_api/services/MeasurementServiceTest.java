package com.yurasev.sensor_api.services;

import com.yurasev.sensor_api.exceptions.SensorException;
import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.repos.MeasurementRepository;
import com.yurasev.sensor_api.repos.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private MeasurementService measurementService;

    private Sensor sensor;
    private Measurement measurement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sensor = new Sensor();
        sensor.setId(1);
        sensor.setKey("test-key");
        sensor.setName("Test Sensor");

        measurement = new Measurement();
        measurement.setTemperature(25.0);
        measurement.setRaining(false);
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setSensor(sensor);
    }

    @Test
    void addMeasurement_WhenSensorExists_ShouldSaveMeasurement() {

        when(sensorRepository.findByKey("test-key")).thenReturn(Optional.of(sensor));
        when(measurementRepository.save(any(Measurement.class))).thenReturn(measurement);

        Measurement newMeasurement = new Measurement();
        newMeasurement.setTemperature(25.0);
        newMeasurement.setRaining(false);

        Measurement savedMeasurement = measurementService.addMeasurement("test-key", newMeasurement);

        assertNotNull(savedMeasurement);
        assertEquals(25.0, savedMeasurement.getTemperature());
        assertFalse(savedMeasurement.isRaining());

        verify(measurementRepository, times(1)).save(any(Measurement.class));
    }

    @Test
    void addMeasurement_WhenSensorDoesNotExist_ShouldThrowException() {

        when(sensorRepository.findByKey("invalid-key")).thenReturn(Optional.empty());

        Measurement newMeasurement = new Measurement();
        newMeasurement.setTemperature(25.0);
        newMeasurement.setRaining(false);

        SensorException exception = assertThrows(SensorException.class, () -> {
            measurementService.addMeasurement("invalid-key", newMeasurement);
        });

        assertEquals("Cant find sensor with key invalid-key", exception.getMessage());

        verify(measurementRepository, never()).save(any(Measurement.class));
    }

    @Test
    void getLastMeasurements_WhenSensorExists_ShouldReturnMeasurements() {

        when(sensorRepository.findByKey("test-key")).thenReturn(Optional.of(sensor));

        when(measurementRepository.findTop20BySensorOrderByTimestampDesc(sensor))
                .thenReturn(List.of(measurement));

        List<Measurement> measurements = measurementService.getLastMeasurements("test-key");

        assertNotNull(measurements);
        assertEquals(1, measurements.size());
        assertEquals(25.0, measurements.get(0).getTemperature());

        verify(measurementRepository, times(1)).findTop20BySensorOrderByTimestampDesc(sensor);
    }

    @Test
    void getLastMeasurements_WhenSensorDoesNotExist_ShouldThrowException() {

        when(sensorRepository.findByKey("invalid-key")).thenReturn(Optional.empty());

        SensorException exception = assertThrows(SensorException.class, () -> {
            measurementService.getLastMeasurements("invalid-key");
        });

        assertEquals("Cant find sensor with key invalid-key", exception.getMessage());

        verify(measurementRepository, never()).findTop20BySensorOrderByTimestampDesc(any(Sensor.class));
    }

    @Test
    void getRecentMeasurements_ShouldReturnRecentMeasurements() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        when(measurementRepository.findByTimestampAfter(oneMinuteAgo))
                .thenReturn(List.of(measurement));

        List<Measurement> recentMeasurements = measurementService.getRecentMeasurements();

        assertNotNull(recentMeasurements);
        assertEquals(1, recentMeasurements.size());
        assertEquals(25.0, recentMeasurements.get(0).getTemperature());

        verify(measurementRepository, times(1)).findByTimestampAfter(oneMinuteAgo);
    }
}

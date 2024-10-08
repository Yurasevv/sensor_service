package com.yurasev.sensor_api.services;

import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.repos.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorService sensorService;

    private Sensor sensor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sensor = new Sensor();
        sensor.setId(1);
        sensor.setName("Test Sensor");
        sensor.setKey(UUID.randomUUID().toString());
        sensor.setActive(true);
    }

    @Test
    void registerSensor_WhenSensorNameIsUnique_ShouldRegisterAndReturnSensor() {

        when(sensorRepository.findByName("Test Sensor")).thenReturn(Optional.empty());

        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);

        Sensor registeredSensor = sensorService.registerSensor("Test Sensor");

        assertNotNull(registeredSensor);
        assertEquals("Test Sensor", registeredSensor.getName());
        assertNotNull(registeredSensor.getKey());
        assertTrue(registeredSensor.isActive());

        verify(sensorRepository, times(1)).save(any(Sensor.class));
    }

    @Test
    void registerSensor_WhenSensorNameExists_ShouldThrowException() {

        when(sensorRepository.findByName("Test Sensor")).thenReturn(Optional.of(sensor));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sensorService.registerSensor("Test Sensor");
        });

        assertEquals("Sensor with this name already exists", exception.getMessage());

        verify(sensorRepository, never()).save(any(Sensor.class));
    }

    @Test
    void findByName_WhenSensorExists_ShouldReturnSensor() {

        when(sensorRepository.findByName("Test Sensor")).thenReturn(Optional.of(sensor));

        Optional<Sensor> foundSensor = sensorService.findByName("Test Sensor");

        assertTrue(foundSensor.isPresent());
        assertEquals("Test Sensor", foundSensor.get().getName());

        verify(sensorRepository, times(1)).findByName("Test Sensor");
    }

    @Test
    void findByName_WhenSensorDoesNotExist_ShouldReturnEmptyOptional() {

        when(sensorRepository.findByName("Nonexistent Sensor")).thenReturn(Optional.empty());

        Optional<Sensor> foundSensor = sensorService.findByName("Nonexistent Sensor");

        assertTrue(foundSensor.isEmpty());

        verify(sensorRepository, times(1)).findByName("Nonexistent Sensor");
    }

    @Test
    void getActiveSensors_ShouldReturnListOfActiveSensors() {

        when(sensorRepository.findByActiveTrue()).thenReturn(List.of(sensor));

        List<Sensor> activeSensors = sensorService.getActiveSensors();

        assertNotNull(activeSensors);
        assertEquals(1, activeSensors.size());
        assertTrue(activeSensors.get(0).isActive());

        verify(sensorRepository, times(1)).findByActiveTrue();
    }

    @Test
    void updateSensorActivity_ShouldSetSensorToInactive() {

        sensorService.updateSensorActivity(sensor);

        assertFalse(sensor.isActive());

        verify(sensorRepository, times(1)).save(sensor);
    }
}
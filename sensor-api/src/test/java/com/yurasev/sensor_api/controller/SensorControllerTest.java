package com.yurasev.sensor_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurasev.sensor_api.controllers.SensorController;
import com.yurasev.sensor_api.dto.SensorDto;
import com.yurasev.sensor_api.exceptions.SensorException;
import com.yurasev.sensor_api.models.Sensor;
import com.yurasev.sensor_api.repos.SensorRepository;
import com.yurasev.sensor_api.services.SensorService;
import com.yurasev.sensor_api.util.DtoMapper;
import com.yurasev.sensor_api.util.SensorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorController.class)
public class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SensorService sensorService;

    @MockBean
    private SensorRepository sensorRepository;

    @MockBean
    private DtoMapper dtoMapper;

    @MockBean
    private SensorValidator sensorValidator;

    @InjectMocks
    private SensorController sensorController;

    private Sensor sensor;

    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setName("TestSensor");
        sensor.setKey("test-key-123");
        sensor.setActive(true);
    }

    @Test
    void registerSensor_ValidSensor_ShouldReturnKey() throws Exception {
        SensorDto validDto = new SensorDto("ValidSensor");
        Sensor sensor = new Sensor();
        sensor.setName("ValidSensor");
        sensor.setKey("test-key-123");

        when(sensorService.registerSensor(anyString())).thenReturn(sensor);

        mockMvc.perform(post("/api/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("test-key-123"));

        verify(sensorService, times(1)).registerSensor(anyString());
    }

    @Test
    void registerSensor_InvalidSensor_ShouldReturnBadRequest() throws Exception {
        SensorDto invalidDto = new SensorDto("");  // Некорректное имя (пустая строка)

        mockMvc.perform(post("/api/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(sensorService, never()).registerSensor(anyString());
    }

    @Test
    void getActiveSensors_ShouldReturnListOfSensors() throws Exception {
        Sensor sensor1 = new Sensor();
        sensor1.setName("Sensor1");
        sensor1.setKey("key1");
        sensor1.setActive(true);

        Sensor sensor2 = new Sensor();
        sensor2.setName("Sensor2");
        sensor2.setKey("key2");
        sensor2.setActive(true);

        List<Sensor> sensors = List.of(sensor1, sensor2);

        when(sensorRepository.findByActiveTrue()).thenReturn(sensors);
        when(sensorService.getActiveSensors()).thenReturn(sensors);

        when(dtoMapper.convertToDto(sensor1)).thenReturn(new SensorDto("Sensor1"));
        when(dtoMapper.convertToDto(sensor2)).thenReturn(new SensorDto("Sensor2"));

        mockMvc.perform(get("/api/sensors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sensor1"))
                .andExpect(jsonPath("$[1].name").value("Sensor2"));

        verify(sensorService, times(1)).getActiveSensors();
    }

    @Test
    void registerSensor_SensorAlreadyExists_ShouldReturnBadRequest() throws Exception {
        SensorDto sensorDto = new SensorDto("ExistingSensor");

        doThrow(new SensorException("Sensor with this name already exists"))
                .when(sensorService).registerSensor(anyString());

        mockMvc.perform(post("/api/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sensor with this name already exists"));

        verify(sensorService, times(1)).registerSensor(anyString());
    }
}

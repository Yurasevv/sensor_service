package com.yurasev.sensor_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurasev.sensor_api.controllers.MeasurementController;
import com.yurasev.sensor_api.dto.MeasurementDto;
import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.services.MeasurementService;
import com.yurasev.sensor_api.util.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MeasurementController.class)
public class MeasurementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private DtoMapper dtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMeasurement_ValidMeasurement_ShouldReturnOk() throws Exception {
        MeasurementDto measurementDto = new MeasurementDto(24.5, false);
        Measurement measurement = new Measurement();

        when(dtoMapper.convertToEntity(any(MeasurementDto.class))).thenReturn(measurement);
        when(measurementService.addMeasurement(anyString(), any(Measurement.class))).thenReturn(measurement);
        when(dtoMapper.convertToDto(any(Measurement.class))).thenReturn(measurementDto);

        mockMvc.perform(post("/api/sensors/testKey/measurements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(24.5))
                .andExpect(jsonPath("$.raining").value(false));

        verify(measurementService, times(1)).addMeasurement(eq("testKey"), any(Measurement.class));
    }

    @Test
    void getLastMeasurements_ValidKey_ShouldReturnMeasurements() throws Exception {
        MeasurementDto measurementDto1 = new MeasurementDto(24.5, false);
        MeasurementDto measurementDto2 = new MeasurementDto(18.0, true);

        when(measurementService.getLastMeasurements(anyString())).thenReturn(List.of(new Measurement(), new Measurement()));
        when(dtoMapper.convertToDto(any(Measurement.class))).thenReturn(measurementDto1, measurementDto2);

        mockMvc.perform(get("/api/sensors/testKey/measurements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].temperature").value(24.5))
                .andExpect(jsonPath("$[0].raining").value(false))
                .andExpect(jsonPath("$[1].temperature").value(18.0))
                .andExpect(jsonPath("$[1].raining").value(true));

        verify(measurementService, times(1)).getLastMeasurements(eq("testKey"));
    }

}

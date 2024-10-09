package com.yurasev.sensor_api.controller;

import com.yurasev.sensor_api.controllers.MeasurementAllController;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasurementAllController.class)
class MeasurementAllControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private DtoMapper dtoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRecentMeasurements_WhenMeasurementsExist_ShouldReturnMeasurementsList() throws Exception {
        MeasurementDto measurementDto1 = new MeasurementDto(25.0, false);
        MeasurementDto measurementDto2 = new MeasurementDto(18.5, true);
        when(measurementService.getRecentMeasurements()).thenReturn(List.of(new Measurement(), new Measurement()));
        when(dtoMapper.convertToDto(any(Measurement.class))).thenReturn(measurementDto1, measurementDto2);

        mockMvc.perform(get("/api/sensors/measurements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temperature").value(25.0))
                .andExpect(jsonPath("$[0].raining").value(false))
                .andExpect(jsonPath("$[1].temperature").value(18.5))
                .andExpect(jsonPath("$[1].raining").value(true));

        verify(measurementService, times(1)).getRecentMeasurements();
    }

    @Test
    void getRecentMeasurements_WhenNoMeasurementsExist_ShouldReturnEmptyList() throws Exception {
        when(measurementService.getRecentMeasurements()).thenReturn(List.of());

        mockMvc.perform(get("/api/sensors/measurements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(measurementService, times(1)).getRecentMeasurements();
    }
}

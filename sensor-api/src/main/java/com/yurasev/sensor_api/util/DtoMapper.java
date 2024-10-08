package com.yurasev.sensor_api.util;

import com.yurasev.sensor_api.dto.MeasurementDto;
import com.yurasev.sensor_api.dto.SensorDto;
import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.models.Sensor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SensorDto convertToDto(Sensor sensor) {
        return modelMapper.map(sensor, SensorDto.class);
    }

    public Sensor convertToEntity(SensorDto sensorDto) {
        return modelMapper.map(sensorDto, Sensor.class);
    }

    public MeasurementDto convertToDto(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDto.class);
    }

    public Measurement convertToEntity(MeasurementDto measurementDto) {
        return modelMapper.map(measurementDto, Measurement.class);
    }
}

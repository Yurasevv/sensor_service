package com.yurasev.sensor_server.services;

import com.yurasev.sensor_server.dto.MeasurementRequest;
import com.yurasev.sensor_server.dto.SensorRegistrationRequest;
import com.yurasev.sensor_server.dto.SensorRegistrationResponse;
import com.yurasev.sensor_server.exceptions.SensorException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class SensorService {

    private final RestTemplate restTemplate;
    private String sensorKey;
    private final Random random = new Random();

    public SensorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void registerSensor(String serverUrl, String sensorName) {
        SensorRegistrationRequest request = new SensorRegistrationRequest();
        request.setName(sensorName);


        SensorRegistrationResponse response = restTemplate.postForObject(
                serverUrl + "/sensors/registration",
                request,
                SensorRegistrationResponse.class);

        if (response != null) {
            sensorKey = response.getKey();
            System.out.println("Sensor registered with key: " + sensorKey);
        } else {
            throw new SensorException("Failed to register sensor");
        }

    }

    public void sendMeasurement(String serverUrl, String sensorName) {

        if (sensorKey == null) {
            registerSensor(serverUrl, sensorName);
            throw new SensorException("Sensor is not registered");
        }

        MeasurementRequest measurementRequest = new MeasurementRequest();
        measurementRequest.setTemperature(generateTemperature());
        measurementRequest.setRaining(random.nextBoolean());

        restTemplate.postForObject(
                serverUrl + "/sensors/" + sensorKey + "/measurements",
                measurementRequest,
                String.class);

        System.out.println("Measurement sent: " + measurementRequest);
    }

    private double generateTemperature() {
        return -100 + (random.nextDouble() * 200);
    }
}

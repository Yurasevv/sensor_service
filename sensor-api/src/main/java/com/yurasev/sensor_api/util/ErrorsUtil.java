package com.yurasev.sensor_api.util;

import com.yurasev.sensor_api.exceptions.MeasurementException;
import com.yurasev.sensor_api.exceptions.SensorException;
import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.models.Sensor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorsUtil {
    public static void returnErrorsToClient(BindingResult bindingResult, Measurement m) {

        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" -- ").append(error.getDefaultMessage())
                    .append(";");
        }

        throw new MeasurementException(errorMsg.toString());
    }

    public static void returnErrorsToClient(BindingResult bindingResult, Sensor s) {

        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" -- ").append(error.getDefaultMessage())
                    .append(";");
        }

        throw new SensorException(errorMsg.toString());
    }
}

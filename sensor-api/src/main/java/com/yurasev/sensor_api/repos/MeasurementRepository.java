package com.yurasev.sensor_api.repos;

import com.yurasev.sensor_api.models.Measurement;
import com.yurasev.sensor_api.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    List<Measurement> findTop20BySensorOrderByTimestampDesc(Sensor sensor);
    List<Measurement> findByTimestampAfter(LocalDateTime timestamp);
}

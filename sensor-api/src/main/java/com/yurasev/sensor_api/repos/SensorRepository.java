package com.yurasev.sensor_api.repos;

import com.yurasev.sensor_api.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    Optional<Sensor> findByName(String name);
    Optional<Sensor> findByKey(String key);
    List<Sensor> findByActiveTrue();
}

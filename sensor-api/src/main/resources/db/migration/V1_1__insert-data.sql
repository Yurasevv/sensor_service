INSERT INTO sensor (name, key, active)
VALUES ('SensorNew', '8bcb5ffa-ff4d-4214-a727-bb01ab90ceaa', TRUE);

INSERT INTO measurement (sensor_id, temperature, raining, timestamp)
VALUES (1, 24.5, FALSE, NOW());

INSERT INTO measurement (sensor_id, temperature, raining, timestamp)
VALUES (1, 22.3, TRUE, NOW() - INTERVAL '2 minutes');

INSERT INTO measurement (sensor_id, temperature, raining, timestamp)
VALUES (1, 25.7, FALSE, NOW() - INTERVAL '3 minutes');

INSERT INTO measurement (sensor_id, temperature, raining, timestamp)
VALUES (1, 20.0, TRUE, NOW() - INTERVAL '4 minutes');
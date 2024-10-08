CREATE TABLE sensor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    key VARCHAR NOT NULL UNIQUE,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE measurement (
    id BIGSERIAL PRIMARY KEY,
    sensor_id INT NOT NULL
        REFERENCES sensor(id)
        ON DELETE CASCADE,
    temperature DOUBLE PRECISION NOT NULL CHECK (temperature >= -100 AND temperature <= 100),
    raining BOOLEAN NOT NULL,
    timestamp TIMESTAMP NOT NULL
);


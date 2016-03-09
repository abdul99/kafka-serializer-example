package com.nibado.example.kafka;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SensorReading {
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Sensor sensor;
    private final long time;
    private final double value;

    public SensorReading(Sensor sensor, long time, double value) {
        this.sensor = Sensor.guardNotNull(sensor);
        this.time = time;
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public long getTime() {
        return time;
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s,%s,%s,%.4f", getLocalDateTime().format(DT_FORMAT), sensor.getId(), sensor.getType().toString(), value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorReading that = (SensorReading) o;

        if (time != that.time) return false;
        if (Double.compare(that.value, value) != 0) return false;
        return sensor.equals(that.sensor);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = sensor.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

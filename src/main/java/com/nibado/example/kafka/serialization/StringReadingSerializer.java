package com.nibado.example.kafka.serialization;

import com.nibado.example.kafka.Sensor;
import com.nibado.example.kafka.SensorReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Closeable;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

/**
 * (De)serializes SensorReadings from/to strings.
 */
public class StringReadingSerializer implements Closeable, AutoCloseable, Serializer<SensorReading>, Deserializer<SensorReading> {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String s, SensorReading sensorReading) {
        String line = String.format(Locale.ROOT, "%s,%s,%s,%.4f", sensorReading.getTime(), sensorReading.getSensor().getId(), sensorReading.getSensor().getType().toString(), sensorReading.getValue());
        return line.getBytes(CHARSET);
    }

    @Override
    public SensorReading deserialize(String topic, byte[] bytes) {
        try {
            String[] parts = new String(bytes, CHARSET).split(",");

            long time = Long.parseLong(parts[0]);
            Sensor.Type type = Sensor.Type.valueOf(parts[2]);
            double value = Double.parseDouble(parts[3]);

            return new SensorReading(new Sensor(parts[1], type), time, value);
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Error reading bytes", e);
        }
    }

    @Override
    public void close() {

    }
}

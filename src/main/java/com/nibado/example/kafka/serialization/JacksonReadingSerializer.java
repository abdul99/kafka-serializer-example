package com.nibado.example.kafka.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.nibado.example.kafka.Sensor;
import com.nibado.example.kafka.SensorReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import static com.nibado.example.kafka.serialization.JacksonReadingSerializer.SerializationHelper.from;

/**
 * (De)serializes SensorReadings using Jackson. Supports both JSON and Smile.
 */
public class JacksonReadingSerializer implements Closeable, AutoCloseable, Serializer<SensorReading>, Deserializer<SensorReading> {
    private ObjectMapper mapper;

    public JacksonReadingSerializer() {
        this(null);
    }

    public JacksonReadingSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public static JacksonReadingSerializer defaultConfig() {
        return new JacksonReadingSerializer(new ObjectMapper());
    }

    public static JacksonReadingSerializer smileConfig() {
        return new JacksonReadingSerializer(new ObjectMapper(new SmileFactory()));
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        if(mapper == null) {
            if("true".equals(map.get("value.serializer.jackson.smile"))) {
                mapper = new ObjectMapper(new SmileFactory());
            }
            else {
                mapper = new ObjectMapper();
            }
        }
    }

    @Override
    public byte[] serialize(String s, SensorReading sensorReading) {
        try {
            return mapper.writeValueAsBytes(from(sensorReading));
        }
        catch(JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public SensorReading deserialize(String s, byte[] bytes) {
        try {
            return mapper.readValue(bytes, SerializationHelper.class).to();
        }
        catch(IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public void close() {
        mapper = null;
    }

    public static class SerializationHelper {
        public String id;
        public Sensor.Type type;
        public long time;
        public double value;

        public static SerializationHelper from(SensorReading reading) {
            SerializationHelper helper = new SerializationHelper();
            helper.id = reading.getSensor().getId();
            helper.type = reading.getSensor().getType();
            helper.time = reading.getTime();
            helper.value = reading.getValue();

            return helper;
        }

        public SensorReading to() {
            return new SensorReading(new Sensor(id, type), time, value);
        }
    }
}

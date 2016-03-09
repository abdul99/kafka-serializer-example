package com.nibado.example.kafka.serialization;

import com.nibado.example.kafka.SensorReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

public class StringReadingSerializerTest extends AbstractSerializerTest {

    @Override
    public Deserializer<SensorReading> getDeserializer() {
        return new StringReadingSerializer();
    }

    @Override
    public Serializer<SensorReading> getSerializer() {
        return new StringReadingSerializer();
    }
}
package com.nibado.example.kafka.serialization;

import com.nibado.example.kafka.SensorReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

public class KryoReadingSerializerTest extends AbstractSerializerTest {

    @Override
    public Deserializer<SensorReading> getDeserializer() {
        return new KryoReadingSerializer();
    }

    @Override
    public Serializer<SensorReading> getSerializer() {
        return new KryoReadingSerializer();
    }
}
package com.nibado.example.kafka.serialization;

import com.nibado.example.kafka.Sensor;
import com.nibado.example.kafka.SensorReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public abstract class AbstractSerializerTest {
    private Serializer<SensorReading> serializer;
    private Deserializer<SensorReading> deserializer;
    private List<SensorReading> testSet;

    public abstract Deserializer<SensorReading> getDeserializer();
    public abstract Serializer<SensorReading> getSerializer();

    public String[] getTopics() {
        return new String[] {"test"};
    }

    public AbstractSerializerTest() {
        testSet = new ArrayList<>(1000);
        for(int i = 0;i < 1000;i++) {
            Sensor.Type type = Sensor.Type.values()[i % Sensor.Type.values().length];
            String id = type.toString().toLowerCase() + "-" + i;
            long time = System.currentTimeMillis() + i * 1000;
            double value = 10.0 + i;

            testSet.add(new SensorReading(new Sensor(id, type), time, value));
        }
    }

    @Before
    public void setup() {
        serializer = getSerializer();
        deserializer = getDeserializer();


    }

    @Test
    public void testCorrect() {
        for(String topic : getTopics()) {
            testSet.forEach(r -> {
                SensorReading other = deserializer.deserialize(topic, serializer.serialize(topic, r));
                assertThat(other).isNotSameAs(r);
                assertThat(other).isEqualTo(r);
            });
        }
    }

    @Test
    public void testDeserializeGarbage() {
        byte[] bytes = serializer.serialize("", testSet.get(0));
        new Random(0).nextBytes(bytes);

        try {
            deserializer.deserialize("", bytes);
            fail("Should not reach");
        }
        catch(IllegalArgumentException e) {

        }
    }
}

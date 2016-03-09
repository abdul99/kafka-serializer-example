package com.nibado.example.kafka.serialization;

import com.nibado.example.kafka.SensorReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class JacksonReadingSerializerTest extends AbstractSerializerTest {
    private boolean smile;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { true }, { false }
        });
    }

    public JacksonReadingSerializerTest(boolean smile) {
        this.smile = smile;
    }

    @Override
    public Deserializer<SensorReading> getDeserializer() {
        if(smile) {
            return JacksonReadingSerializer.smileConfig();
        }
        else {
            return JacksonReadingSerializer.defaultConfig();
        }
    }

    @Override
    public Serializer<SensorReading> getSerializer() {
        if(smile) {
            return JacksonReadingSerializer.smileConfig();
        }
        else {
            return JacksonReadingSerializer.defaultConfig();
        }
    }
}
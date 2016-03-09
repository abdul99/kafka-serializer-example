package com.nibado.example.kafka.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.nibado.example.kafka.Sensor;
import com.nibado.example.kafka.SensorReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Closeable;
import java.util.Map;

/**
 * (De)serializes SensorReadings using Kryo.
 */
public class KryoReadingSerializer implements Closeable, AutoCloseable, Serializer<SensorReading>, Deserializer<SensorReading> {
    private ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.addDefaultSerializer(SensorReading.class, new KryoInternalSerializer());
            return kryo;
        };
    };

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String s, SensorReading sensorReading) {
        ByteBufferOutput output = new ByteBufferOutput(100);
        kryos.get().writeObject(output, sensorReading);
        return output.toBytes();
    }

    @Override
    public SensorReading deserialize(String s, byte[] bytes) {
        try {
            return kryos.get().readObject(new ByteBufferInput(bytes), SensorReading.class);
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Error reading bytes",e);
        }
    }

    @Override
    public void close() {

    }

    private static class KryoInternalSerializer extends com.esotericsoftware.kryo.Serializer<SensorReading> {
        @Override
        public void write(Kryo kryo, Output output, SensorReading sensorReading) {
            output.writeString(sensorReading.getSensor().getId());
            output.writeString(sensorReading.getSensor().getType().toString());
            output.writeLong(sensorReading.getTime(), true);
            output.writeDouble(sensorReading.getValue());
        }

        @Override
        public SensorReading read(Kryo kryo, Input input, Class<SensorReading> aClass) {
            String id = input.readString();
            Sensor.Type type = Sensor.Type.valueOf(input.readString());

            return new SensorReading(new Sensor(id, type), input.readLong(true), input.readDouble());
        }
    }
}

package com.nibado.example.kafka;

public class Sensor {
    private final String id;
    private final Type type;

    public Sensor(String id, Type type) {
        this.id = guardNotNull(id);
        this.type = guardNotNull(type);
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        TEMP,
        LIGHT,
        HUMIDITY
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sensor sensor = (Sensor) o;

        if (!id.equals(sensor.id)) return false;
        return type == sensor.type;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public static <T> T guardNotNull(T o) {
        if(o == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        }

        return o;
    }
}

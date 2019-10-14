package com.atherys.battlegrounds.serialize;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationTypeSerializer implements TypeSerializer<Duration> {
    @Override
    public Duration deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        ChronoUnit unit = ChronoUnit.valueOf(value.getNode("unit").getString("MILLIS"));
        long amount = value.getNode("amount").getLong();

        return Duration.of(amount, unit);
    }

    @Override
    public void serialize(TypeToken<?> type, Duration obj, ConfigurationNode value) throws ObjectMappingException {
        // A Duration's original time units are not saved and as such by default durations will be saved in milliseconds
        value.getNode("unit").setValue(ChronoUnit.MILLIS);
        value.getNode("amount").setValue(obj.get(ChronoUnit.MILLIS));
    }
}

package com.atherys.battlegrounds.serialize;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class LocationTypeSerializer implements TypeSerializer<Location<World>> {
    @Override
    public Location<World> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String world = value.getNode("world").getString();
        double x = value.getNode("x").getDouble();
        double y = value.getNode("y").getDouble();
        double z = value.getNode("z").getDouble();

        Optional<World> w = Sponge.getServer().getWorld(world);
        if (!w.isPresent()) throw new ObjectMappingException("World '" + world + "' does not exist.");

        return new Location<>(w.get(), x, y, z);
    }

    @Override
    public void serialize(TypeToken<?> type, Location<World> obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("world").setValue(obj.getExtent().getName());
        value.getNode("x").setValue(obj.getPosition().getX());
        value.getNode("y").setValue(obj.getPosition().getY());
        value.getNode("z").setValue(obj.getPosition().getZ());
    }
}

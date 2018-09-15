package com.atherys.battlegrounds.model;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@ConfigSerializable
public class RespawnPoint {

    @Setting("location")
    private Location<World> location;

    @Setting("radius")
    private double radius;

    private RespawnPoint() {}

    public RespawnPoint(Location<World> location, double radius) {
        this.location = location;
        this.radius = radius;
    }

    public Location<World> getLocation() {
        return location;
    }

    public double getRadius() {
        return radius;
    }

    public Location<World> getRandomPointWithin() {
        double x = location.getX() + radius * Math.cos( Math.random() * 360.0 );
        double z = location.getZ() + radius * Math.sin( Math.random() * 360.0 );
        double y = location.getExtent().getHighestYAt( (int) x, (int) z );
        return new Location<>( location.getExtent(), x, y, z );
    }
}

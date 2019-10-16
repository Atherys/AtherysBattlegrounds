package com.atherys.battlegrounds.model;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class RespawnPoint {

    private Location<World> location;

    private double radius;

    public RespawnPoint() {
    }

    public RespawnPoint(Location<World> location, double radius) {
        this.location = location;
        this.radius = radius;
    }

    public Location<World> getLocation() {
        return location;
    }

    public void setLocation(Location<World> location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

//    public Location<World> getRandomPointWithin() {
//        double x = location.getX() + radius * Math.cos( Math.random() * 360.0 );
//        double z = location.getZ() + radius * Math.sin( Math.random() * 360.0 );
//        double y = location.getExtent().getHighestYAt( (int) x, (int) z );
//        return new Location<>( location.getExtent(), x, y, z );
//    }
}

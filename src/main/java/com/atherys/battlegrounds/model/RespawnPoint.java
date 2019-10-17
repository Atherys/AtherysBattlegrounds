package com.atherys.battlegrounds.model;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RespawnPoint that = (RespawnPoint) o;
        return Double.compare(that.radius, radius) == 0 &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, radius);
    }

    @Override
    public String toString() {
        return "RespawnPoint{" +
                "location=" + location +
                ", radius=" + radius +
                '}';
    }
}

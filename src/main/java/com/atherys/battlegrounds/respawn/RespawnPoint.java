package com.atherys.battlegrounds.respawn;

import com.atherys.battlegrounds.managers.RespawnManager;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@ConfigSerializable
public class RespawnPoint {

    @Setting
    private Location<World> location;
    @Setting
    private float radius;

    public Location<World> getLocation() {
        return location;
    }

    public float getRadius() {
        return radius;
    }

    public void respawn( Player player ) {
        RespawnManager.getInstance().queueRespawn( new Respawn( this, player ) );
    }

    public Location<World> getRandomPointWithin() {
        double x = location.getX() + radius * Math.cos( Math.random() * 360.0 );
        double z = location.getZ() + radius * Math.sin( Math.random() * 360.0 );
        double y = location.getExtent().getHighestYAt( (int) x, (int) z );
        return new Location<>( location.getExtent(), x, y, z );
    }
}

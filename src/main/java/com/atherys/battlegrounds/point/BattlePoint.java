package com.atherys.battlegrounds.point;

import com.atherys.battlegrounds.respawn.RespawnPoint;
import com.atherys.battlegrounds.team.Team;
import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.UUID;

@ConfigSerializable
public class BattlePoint {

    @Setting
    private UUID uuid;

    @Setting
    private Text name;
    @Setting
    private TextColor color;

    @Setting
    private Location<World> origin;
    @Setting
    private float outerRadius;
    @Setting
    private float innerRadius;

    @Setting
    private Team team;

    @Setting
    private List<RespawnPoint> respawnPoints;

    public UUID getUUID() {
        return uuid;
    }

    public Text getName() {
        return name;
    }

    public Location<World> getOrigin() {
        return origin;
    }

    public float getOuterRadius() {
        return outerRadius;
    }

    public float getInnerRadius() {
        return innerRadius;
    }

    public boolean contains( Location<World> location ) {
        double cx = origin.getX();
        double cy = origin.getY();
        double cz = origin.getZ();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return origin.getExtent().equals( location.getExtent() ) &&
                (
                        Math.pow( x-cx, 2 ) + Math.pow( y-cy, 2 ) + Math.pow( z-cz, 2 ) < Math.pow( outerRadius, 2 )
                );
    }

    public boolean contains( World world, Vector3d position ) {
        return contains( new Location<>( world, position ) );
    }

    public boolean contains ( World world, double x, double y, double z ) {
        return contains( new Location<>( world, x, y, z ) );
    }

    public Team getTeam() {
        return team;
    }

    public List<RespawnPoint> getRespawnPoints() {
        return respawnPoints;
    }
}

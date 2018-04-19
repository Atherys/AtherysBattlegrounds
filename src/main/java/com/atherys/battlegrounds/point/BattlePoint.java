package com.atherys.battlegrounds.point;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.managers.TeamManager;
import com.atherys.battlegrounds.respawn.RespawnPoint;
import com.atherys.battlegrounds.team.Team;
import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

@ConfigSerializable
public class BattlePoint {

    @Setting
    private UUID uuid;

    @Setting
    private Text name;

    @Setting
    private Location<World> origin;
    @Setting
    private float outerRadius;
    @Setting
    private float innerRadius;

    @Setting
    private CaptureData captureData;

    @Setting
    private List<RespawnPoint> respawnPoints = new ArrayList<>();

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
        double cx = getOrigin().getX();
        double cy = getOrigin().getY();
        double cz = getOrigin().getZ();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return origin.getExtent().equals( location.getExtent() ) &&
                (
                        Math.pow( x - cx, 2 ) + Math.pow( y - cy, 2 ) + Math.pow( z - cz, 2 ) < Math.pow( getOuterRadius(), 2 )
                );
    }

    public boolean containsInner ( Location<World> location ) {
        double cx = getOrigin().getX();
        double cy = getOrigin().getY();
        double cz = getOrigin().getZ();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return origin.getExtent().equals( location.getExtent() ) &&
                (
                        Math.pow( x - cx, 2 ) + Math.pow( y - cy, 2 ) + Math.pow( z - cz, 2 ) < Math.pow( getInnerRadius(), 2 )
                );
    }

    public boolean contains( World world, Vector3d position ) {
        return contains( new Location<>( world, position ) );
    }

    public boolean contains( World world, double x, double y, double z ) {
        return contains( new Location<>( world, x, y, z ) );
    }

    public Optional<Team> getTeam() {
        return captureData.getControllingTeam();
    }

    public TextColor getColor() {
        Optional<Team> controllingTeam = captureData.getControllingTeam();
        if ( controllingTeam.isPresent() ) return controllingTeam.get().getColor();
        else return TextColors.WHITE;
    }

    public List<RespawnPoint> getRespawnPoints() {
        return respawnPoints;
    }

    public void respawn( Player player ) {
        getRespawnPoints().get( RandomUtils.nextInt( 0, getRespawnPoints().size() ) ).respawn( player );
    }

    public void tick() {
        Map<Team,Integer> capturingTeams = new HashMap<>();

        for ( Player player : Sponge.getServer().getOnlinePlayers() ) {
            if ( !containsInner( player.getLocation() ) ) return;

            Optional<Team> team = TeamManager.getInstance().getPrimaryTeam( player );
            if ( !team.isPresent() ) return;

            if ( !capturingTeams.containsKey( team.get() ) ) capturingTeams.put( team.get(), 1 );
            else capturingTeams.put( team.get(), capturingTeams.get( team.get() ) + 1 );
        }

        capturingTeams.forEach( (team,numberOfPlayers) -> {
            if ( numberOfPlayers >= AtherysBattlegrounds.getConfig().MIN_PLAYERS_PER_TEAM ) captureData.tick( this, team );
        } );
    }
}

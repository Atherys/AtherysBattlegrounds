package com.atherys.battlegrounds.config;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashSet;
import java.util.Set;

@ConfigSerializable
public class BattlepointConfig {

    @Setting("name")
    public String NAME;

    @Setting("color")
    public String COLOR;

    @Setting("position")
    public Vector3d POSITION;

    @Setting("capture-radius")
    public double CAPTURE_RADIUS;

    @Setting("respawn-radius")
    public double RESPAWN_RADIUS;

    @Setting("respawn-points")
    public Set<RespawnPointConfig> RESPAWN_POINTS = new HashSet<>();

}

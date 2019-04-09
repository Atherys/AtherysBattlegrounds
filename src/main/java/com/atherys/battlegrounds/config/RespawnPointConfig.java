package com.atherys.battlegrounds.config;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class RespawnPointConfig {

    @Setting("name")
    public String NAME;

    @Setting("position")
    public Vector3d POSITION;

    @Setting("radius")
    public double RADIUS;

}

package com.atherys.battlegrounds.managers;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.point.BattlePoint;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BattlePointManager {

    private static BattlePointManager instance = new BattlePointManager();

    List<BattlePoint> battlePoints = new ArrayList<>();

    private BattlePointManager () {
        this.battlePoints = AtherysBattlegrounds.getConfig().BATTLE_POINTS;
    }

    /**
     * This method will return a single {@link BattlePoint} to which the designated location will belong.<br>
     * The algorithm used here will take the ratios of all intersecting BattlePoint radii,
     * compare them against eachother, and return the one with the smallest ratio.
     * This ensures the location will be placed within the most appropriate radius.
     * @param location The location to be checked
     * @return An optional containing a battlepoint. If no battlepoints are intersected, this is empty.
     */
    public Optional<BattlePoint> getPointFromLocation ( Location<World> location ) {
        List<BattlePoint> points = getPointsFromLocation( location );
        if ( points.size() == 0 ) return Optional.empty();
        if ( points.size() == 1 ) return Optional.of(points.get( 0 ));

        double temp = 0;
        BattlePoint result = points.get( 0 );

        for ( BattlePoint point : points ) {
            double ratio = location.getPosition().distance( point.getOrigin().getPosition() ) / point.getOuterRadius();
            if ( ratio > temp ) {
                temp = ratio;
                result = point;
            }
        }

        return Optional.of( result );
    }

    /**
     * Returns a list of all {@link BattlePoint}s which contain this location
     * @param location the location to be checked
     * @return the battlepoints
     */
    public List<BattlePoint> getPointsFromLocation ( Location<World> location ) {
        List<BattlePoint> result = new ArrayList<>();

        battlePoints.forEach( point -> {
            if ( point.contains( location ) ) result.add( point );
        } );

        return result;
    }

    public static BattlePointManager getInstance() {
        return instance;
    }

}

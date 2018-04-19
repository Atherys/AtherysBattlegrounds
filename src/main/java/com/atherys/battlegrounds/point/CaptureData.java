package com.atherys.battlegrounds.point;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.events.BattlePointCaptureEvent;
import com.atherys.battlegrounds.team.Team;
import org.spongepowered.api.Sponge;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CaptureData {

    private Team controllingTeam;
    private Map<Team,Double> captureProgress = new HashMap<>();

    public Optional<Team> getControllingTeam() {
        return Optional.ofNullable( controllingTeam );
    }

    public double getProgress ( Team team ) {
        return captureProgress.getOrDefault( team, 0.0d );
    }

    public void setProgress ( Team team, double progress ) {
        double value = progress;
        if ( value > AtherysBattlegrounds.getConfig().MAX_CAPTURE ) value = AtherysBattlegrounds.getConfig().MAX_CAPTURE;
        if ( value < 0.0d ) value = 0.0d;
        captureProgress.put( team, value );
    }

    void tick ( BattlePoint point, Team team ) {
        double progress = getProgress( team );

        // increment this team's capture progress by CAPTURE_RATE
        setProgress( team, progress + AtherysBattlegrounds.getConfig().CAPTURE_RATE );

        // decrement all other teams' capture progress by CAPTURE_RATE
        captureProgress.forEach( (t,p) -> {
            if ( t != team ) setProgress( t, p - AtherysBattlegrounds.getConfig().CAPTURE_RATE );
        } );

        // if the controlling team is non-existant, set this as the controlling team
        if ( controllingTeam == null ) controllingTeam = team;

        // If this team's progress is higher than the current controlling team, set this as the new controlling team
        if ( getProgress( team ) > getProgress( controllingTeam ) ) {
            controllingTeam = team;

            BattlePointCaptureEvent event = new BattlePointCaptureEvent( point, controllingTeam );
            Sponge.getEventManager().post( event );
        }
    }
}

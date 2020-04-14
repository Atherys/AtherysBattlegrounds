package com.atherys.battlegrounds.listener;

import com.atherys.battlegrounds.event.BattlePointEvent;
import com.atherys.battlegrounds.facade.BattlePointFacade;
import com.atherys.battlegrounds.model.BattlePoint;
import com.atherys.battlegrounds.model.BattleTeam;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;

@Singleton
public class BattlePointListener {

    @Inject
    private BattlePointFacade battlePointFacade;

    public BattlePointListener() {
    }

    @Listener
    public void onBattlePointTick(BattlePointEvent.Tick.Post event, @First BattlePoint battlePoint) {
        battlePointFacade.updateBattlePointBossBar(battlePoint);
    }

    @Listener
    public void onBattlePointCapture(BattlePointEvent.Capture event, @First BattlePoint battlePoint, @First BattleTeam team) {
        battlePointFacade.notifyCapturedBattlePoint(battlePoint, team);
    }

    @Listener
    public void onBattlePointWarning(BattlePointEvent.Warning event, @First BattlePoint battlePoint) {
        battlePointFacade.warnCapturableBattlePoint(battlePoint);
    }

    @Listener
    public void onBattlePointCapturable(BattlePointEvent.Capturable event, @First BattlePoint battlePoint) {
        battlePointFacade.notifyCapturableBattlePoint(battlePoint);
    }
}

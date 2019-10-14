package com.atherys.battlegrounds.listener;

import com.atherys.battlegrounds.event.BattlePointEvent;
import com.atherys.battlegrounds.model.BattlePoint;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;

public class BattlePointListener {

    @Listener
    public void onBattlepointTick(BattlePointEvent.Tick event, @First BattlePoint battlepoint) {
        // AtherysBattlegrounds.getInstance().getBattlepointService().getBattlepointBossBar(battlepoint);
    }

}

package com.atherys.battlegrounds.listener;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.event.BattlepointEvent;
import com.atherys.battlegrounds.model.Battlepoint;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;

public class BattlepointListener {

    @Listener
    public void onBattlepointTick(BattlepointEvent.Tick event, @First Battlepoint battlepoint) {
        AtherysBattlegrounds.getInstance().getBattlepointService().getBattlepointBossBar(battlepoint);
    }

}

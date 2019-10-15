package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.config.BattlePointConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TeamFacade {

    @Inject
    private BattlePointConfig battlePointConfig;

    public TeamFacade() {
    }

    public void init() {

    }

}

package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.entity.Battlepoint;
import com.atherys.core.db.HibernateRepository;

public class BattlepointRepository extends HibernateRepository<Battlepoint, Long> {
    public BattlepointRepository() {
        super(Battlepoint.class);
    }
}

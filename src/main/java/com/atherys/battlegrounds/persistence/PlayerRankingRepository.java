package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.model.entity.PlayerRanking;
import com.atherys.core.db.CachedHibernateRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class PlayerRankingRepository extends CachedHibernateRepository<PlayerRanking, Integer> {
    public PlayerRankingRepository() {
        super(PlayerRanking.class);
    }
}

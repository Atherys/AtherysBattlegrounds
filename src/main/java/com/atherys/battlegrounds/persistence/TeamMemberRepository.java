package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.core.db.CachedHibernateRepository;

import java.util.UUID;

public class TeamMemberRepository extends CachedHibernateRepository<TeamMember, UUID> {
    public TeamMemberRepository() {
        super(TeamMember.class);
    }
}

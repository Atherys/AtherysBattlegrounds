package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.core.db.CachedHibernateRepository;
import com.google.inject.Singleton;

import java.util.UUID;

@Singleton
public class TeamMemberRepository extends CachedHibernateRepository<TeamMember, UUID> {
    public TeamMemberRepository() {
        super(TeamMember.class);
    }

    public String fetchTeamMemberRankName(TeamMember teamMember) {
        return "Not Yet Implemented";
    }
}

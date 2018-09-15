package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.model.TeamMember;
import com.atherys.core.database.mongo.AbstractMongoDatabaseManager;

public class TeamMemberManager extends AbstractMongoDatabaseManager<TeamMember> {

    private static final TeamMemberManager instance = new TeamMemberManager();

    protected TeamMemberManager() {
        super(AtherysBattlegrounds.getLogger(), AtherysBattlegrounds.getInstance().getDatabase(), TeamMember.class);
    }

    public void saveAll() {
        super.saveAll(getCache().values());
    }

    public static TeamMemberManager getInstance() {
        return instance;
    }
}

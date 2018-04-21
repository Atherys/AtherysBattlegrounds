package com.atherys.battlegrounds.managers;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.db.BattlegroundsDatabase;
import com.atherys.battlegrounds.team.Team;
import com.atherys.battlegrounds.team.TeamMember;
import com.atherys.core.database.mongo.AbstractMongoDatabaseManager;
import org.bson.Document;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

public class TeamMemberManager extends AbstractMongoDatabaseManager<TeamMember> {

    private static TeamMemberManager instance = new TeamMemberManager();

    private TeamMemberManager() {
        super( AtherysBattlegrounds.getLogger(), BattlegroundsDatabase.getInstance(), "teamMembers" );
    }

    public Optional<Team> getPrimaryTeam( Player player ) {
        TeamMember member = getCache().get( player.getUniqueId() );
        if ( member == null ) return Optional.empty();
        return Optional.of( member.getPrimaryTeam() );
    }

    @Override
    protected Optional<Document> toDocument ( TeamMember teamMember ) {
        Document doc = new Document( "uuid", teamMember.getUUID() );
        doc.append( "primary", teamMember.getPrimaryTeamIndex() );

        List<UUID> secondaries = new ArrayList<>();
        teamMember.getSecondaryTeams().forEach( team -> secondaries.add( team.getUUID() ) );

        doc.append( "secondaries", secondaries );

        return Optional.of( doc );
    }

    @Override
    protected Optional<TeamMember> fromDocument ( Document document ) {
        UUID uuid = document.get( "uuid", UUID.class );
        int primary = document.get( "primary", Integer.class );

        return Optional.empty();
    }

    public static TeamMemberManager getInstance() {
        return instance;
    }
}

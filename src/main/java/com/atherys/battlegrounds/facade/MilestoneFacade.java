package com.atherys.battlegrounds.facade;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.battlegrounds.BattlegroundsConfig;
import com.atherys.battlegrounds.config.MilestoneConfig;
import com.atherys.battlegrounds.model.entity.TeamMember;
import com.atherys.battlegrounds.service.TeamMemberService;
import com.atherys.battlegrounds.service.TeamService;
import com.atherys.core.economy.Economy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.spongepowered.api.text.format.TextColors.*;

@Singleton
public class MilestoneFacade {
    @Inject
    private TeamMemberService teamMemberService;

    @Inject
    private TeamService teamService;

    @Inject
    private BattlegroundsConfig config;

    @Inject
    private BattlegroundMessagingFacade msg;

    public void onTransaction(TransactionResult result) {
        if (result.getAccount() instanceof UniqueAccount && result.getCurrency().equals(config.MILESTONE_CURRENCY)) {
            UniqueAccount account = (UniqueAccount) result.getAccount();
            Optional<Player> player = Sponge.getServer().getPlayer(account.getUniqueId());
            User user = player.isPresent() ? player.get() : Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(account.getUniqueId()).get();
            checkMilestones(user, account);
        }
    }

    public void checkMilestones(User user) {
        UniqueAccount account = Economy.getAccount(user.getUniqueId()).get();
        checkMilestones(user, account);
    }

    private void checkMilestones(User user, UniqueAccount account) {
        TeamMember member =  teamMemberService.getOrCreateTeamMember(user);

        int i = -1;
        for (MilestoneConfig milestoneConfig : config.MILESTONES) {
            if (account.getBalance(config.MILESTONE_CURRENCY).compareTo(BigDecimal.valueOf(milestoneConfig.getThreshold())) >= 0) {
                i++;
            } else {
                break;
            }
        }

        teamMemberService.setMilestone(member, i);

        if (member.getMilestone() != member.getMilestonesAwarded() && user instanceof Player) {
            Task.builder().delayTicks(0).execute(() -> awardMilestones((Player) user)).submit(AtherysBattlegrounds.getInstance());
        }
    }

    public void awardMilestones(Player source) {
        TeamMember member = teamMemberService.getOrCreateTeamMember(source);

        for (int i = member.getMilestonesAwarded() + 1; i <= member.getMilestone(); i++) {
            MilestoneConfig milestoneConfig = config.MILESTONES.get(i);

            msg.info(source, DARK_GREEN, TextSerializers.FORMATTING_CODE.deserialize(milestoneConfig.getMessage()));
            teamService.distributeAwardsToMembers(milestoneConfig.getAward(), Collections.singleton(source));
        }

        teamMemberService.setAwardedMilestones(member, member.getMilestone());
    }

    public void displayMilestones(Player source) {
        TeamMember member = teamMemberService.getOrCreateTeamMember(source);
        int i = 0;
        int amount = Economy.getAccount(source.getUniqueId()).get().getBalance(config.MILESTONE_CURRENCY).intValue() - config.CURRENCY_BASE;
        Text.Builder milestones = Text.builder().append(Text.of(
                DARK_GRAY, "[]====[ ", GOLD, config.MILESTONES_TITLE,
                DARK_GREEN, " (", GOLD, amount, DARK_GREEN, ")",
                DARK_GRAY, " ]====[]", Text.NEW_LINE));

        for (MilestoneConfig milestoneConfig : config.MILESTONES) {
            if (i <= member.getMilestone()) {
                milestones.append(Text.of(GREEN, milestoneConfig.getDisplay() + " ✓"));
            } else {
                milestones.append(Text.of(DARK_GRAY, milestoneConfig.getDisplay()));
            }

            if (i < config.MILESTONES.size()) {
                milestones.append(Text.NEW_LINE);
            }
            i++;
        }

        source.sendMessage(milestones.build());
    }
}


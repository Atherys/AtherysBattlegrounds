package com.atherys.battlegrounds.facade;

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
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.math.BigDecimal;
import java.util.Collections;

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
            User user = Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(account.getUniqueId()).get();
            checkMilestones(user, account);
        }
    }

    public void checkMilestones(User user) {
        Economy.getAccount(user.getUniqueId()).ifPresent(account -> {
            checkMilestones(user, account);
        });
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

        if (i != member.getMilestone() && user instanceof Player) {
            Text message = Text.builder()
                    .append(Text.of(DARK_GREEN, "You have unlocked rewards! Click or /rewards to receive them."))
                    .onClick(TextActions.executeCallback(commandSource -> awardMilestones((Player) user)))
                    .build();

            msg.info((MessageReceiver) user, message);
        }

        teamMemberService.setMilestone(member, i);
    }

    public void awardMilestones(Player source) {
        TeamMember member = teamMemberService.getOrCreateTeamMember(source);

        for (int i = member.getMilestonesAwarded() + 1; i <= member.getMilestone(); i++) {
            MilestoneConfig milestoneConfig = config.MILESTONES.get(i);

            msg.info(source, TextSerializers.FORMATTING_CODE.deserialize(milestoneConfig.getMessage()));
            teamService.distributeAwardsToMembers(milestoneConfig.getAward(), Collections.singleton(source));
        }

        teamMemberService.setAwardedMilestones(member, member.getMilestone());
    }

    public void displayMilestones(Player source) {
        TeamMember member = teamMemberService.getOrCreateTeamMember(source);
        int i = 0;
        Text.Builder milestones = Text.builder().append(Text.of(DARK_GRAY, "[]====[ ", GOLD, config.MILESTONES_TITLE, DARK_GRAY, " ]====[]", Text.NEW_LINE));

        BigDecimal amount = Economy.getAccount(source.getUniqueId()).get().getBalance(config.MILESTONE_CURRENCY);
        milestones.append(Text.of(config.MILESTONE_CURRENCY.format(amount), Text.NEW_LINE));

        for (MilestoneConfig milestoneConfig : config.MILESTONES) {
            if (i <= member.getMilestonesAwarded() && member.getMilestonesAwarded() < member.getMilestone()) {
                milestones.append(Text.of(Text.builder()
                        .onHover(TextActions.showText(Text.of(DARK_GRAY, "Click to receive awards!")))
                        .onClick(TextActions.executeCallback(src -> awardMilestones(source)))
                        .append(Text.of(GOLD, milestoneConfig.getDisplay()))
                        .build())
                );
            } else if (i <= member.getMilestone()) {
                milestones.append(Text.of(GREEN, milestoneConfig.getDisplay() + "✓"));
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


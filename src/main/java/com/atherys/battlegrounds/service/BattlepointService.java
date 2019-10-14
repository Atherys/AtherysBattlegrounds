package com.atherys.battlegrounds.service;

public class BattlepointService {// implements CatalogRegistryModule<BattlePoint> {

//    private static final BattlepointService instance = new BattlepointService();
//
//    private Map<BattlePoint,ServerBossBar> bossBars = new HashMap<>();
//
//    private Task battlepointTask;
//
//    private BattlepointService() {
//        battlepointTask = Task.builder()
//                .delay(1, TimeUnit.SECONDS)
//                .interval(AtherysBattlegrounds.getConfig().BATTLEPOINTS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
//                .execute(this::battlepointsTick)
//                .submit(AtherysBattlegrounds.getInstance());
//    }
//
//    public Collection<Entity> getEntitiesWithinInnerRadius(BattlePoint battlepoint) {
//        return battlepoint.getLocation().getExtent().getNearbyEntities(battlepoint.getLocation().getPosition(), battlepoint.getInnerRadius());
//    }
//
//    public Team getControllingTeam(BattlePoint battlepoint) {
//        Team highestProgressTeam = Team.NONE;
//
//        for ( Map.Entry<Team,Float> entry : battlepoint.getAllTeamProgress().entrySet() ) {
//            if ( entry.getValue() > battlepoint.getTeamProgress(highestProgressTeam) ) highestProgressTeam = entry.getKey();
//        }
//
//        return highestProgressTeam;
//    }
//
//    public List<Player> getPlayersWithinBattlepoint(BattlePoint battlepoint) {
//        List<Player> players = new ArrayList<>();
//        getEntitiesWithinInnerRadius(battlepoint).forEach(entity -> {
//            if ( entity instanceof Player ) players.add((Player) entity);
//        });
//        return players;
//    }
//
//    public BossBar getBattlepointBossBar(BattlePoint battlepoint) {
//        ServerBossBar bossBar;
//
//        if ( bossBars.containsKey(battlepoint) ) bossBar = bossBars.get(battlepoint);
//        else {
//            Team controllingTeam = getControllingTeam(battlepoint);
//
//            bossBar = ServerBossBar.builder()
//                    .name(
//                            Text.of(
//                                    TextStyles.BOLD,
//                                    battlepoint.getName(),
//                                    " held by ",
//                                    controllingTeam.getColor(),
//                                    controllingTeam.getName()
//                            )
//                    )
//                    .percent(battlepoint.getTeamProgress(controllingTeam))
//                    .color(battlepoint.getColor())
//                    .overlay(BossBarOverlays.PROGRESS)
//                    .playEndBossMusic(false)
//                    .visible(true)
//                    .build();
//
//            bossBars.put(battlepoint, bossBar);
//        }
//
//        bossBar.removePlayers(bossBar.getPlayers());
//        bossBar.addPlayers(getPlayersWithinBattlepoint(battlepoint));
//
//        return bossBar;
//    }
//
//    private void tickBattlepoint(BattlePoint battlepoint) {
//        // get the entities within the inner radius
//        Collection<Entity> innerRadiusEntities = getEntitiesWithinInnerRadius(battlepoint);
//
//        innerRadiusEntities.forEach(entity -> {
//            if (!(entity instanceof Player)) return;
//
//            Player player = (Player) entity;
//            TeamMember tm = AtherysBattlegrounds.getInstance().getTeamMemberManager().getOrCreate(player.getUniqueId());
//
//            tm.getPrimary().ifPresent(primaryTeam -> {
//                this.captureBattlepoint(battlepoint, primaryTeam, AtherysBattlegrounds.getConfig().CAPTURE_AMOUNT);
//            });
//        });
//
//
//        BattlepointEvent.Tick tickEvent = new BattlepointEvent.Tick(battlepoint);
//        Sponge.getEventManager().post(tickEvent);
//    }
//
//    private void captureBattlepoint(BattlePoint battlepoint, Team team, float capAmount) {
//        battlepoint.getAllTeamProgress().forEach((t, progress) -> {
//            if (t.equals(team)) battlepoint.addTeamProgress(team, capAmount);
//            else battlepoint.removeTeamProgress(t, capAmount);
//        });
//
//        if ( battlepoint.getTeamProgress(team) == 1.0f ) {
//            BattlepointEvent.Capture captureEvent = new BattlepointEvent.Capture(battlepoint, team);
//            Sponge.getEventManager().post(captureEvent);
//        }
//    }
//
//    private void battlepointsTick() {
//        getAll().forEach(this::tickBattlepoint);
//    }
//
//    @Override
//    @Nonnull
//    public Optional<BattlePoint> getById(@Nonnull String id) {
//        for (BattlePoint point : getAll()) {
//            if (point.getId().equals(id)) return Optional.of(point);
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    @Nonnull
//    public Collection<BattlePoint> getAll() {
//        return AtherysBattlegrounds.getConfig().BATTLEPOINTS;
//    }
//
//    public static BattlepointService getInstance() {
//        return instance;
//    }
//
//    public Task getBattlepointTask() {
//        return battlepointTask;
//    }
}

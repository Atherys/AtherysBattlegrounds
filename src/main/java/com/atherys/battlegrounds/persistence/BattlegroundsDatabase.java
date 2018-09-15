package com.atherys.battlegrounds.persistence;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.core.database.mongo.AbstractMongoDatabase;

public class BattlegroundsDatabase extends AbstractMongoDatabase {

    private static final BattlegroundsDatabase instance = new BattlegroundsDatabase();

    protected BattlegroundsDatabase() {
        super(AtherysBattlegrounds.getConfig().DATABASE);
    }

    public static BattlegroundsDatabase getInstance() {
        return instance;
    }
}

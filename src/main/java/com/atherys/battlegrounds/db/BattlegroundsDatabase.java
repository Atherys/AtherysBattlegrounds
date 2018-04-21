package com.atherys.battlegrounds.db;

import com.atherys.battlegrounds.AtherysBattlegrounds;
import com.atherys.core.database.mongo.AbstractMongoDatabase;

public class BattlegroundsDatabase extends AbstractMongoDatabase {

    private static BattlegroundsDatabase instance = new BattlegroundsDatabase();

    protected BattlegroundsDatabase () {
        super( AtherysBattlegrounds.getConfig().DATABASE );
    }

    public static BattlegroundsDatabase getInstance() {
        return instance;
    }
}

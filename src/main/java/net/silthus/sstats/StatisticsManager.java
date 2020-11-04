package net.silthus.sstats;

import io.ebean.Database;

public final class StatisticsManager {

    private final Database database;

    public StatisticsManager(Database database) {
        this.database = database;
    }
}

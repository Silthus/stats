package net.silthus.sstats;

import io.ebean.Database;
import lombok.AccessLevel;
import lombok.Getter;
import net.silthus.sstats.entities.StatisticEntry;
import net.silthus.sstats.entities.Statistic;

public final class StatisticsManager {

    @Getter(AccessLevel.PACKAGE)
    private final Database database;

    public StatisticsManager(Database database) {
        this.database = database;
    }

    void initStatistics() {

        for (Statistic statisticType : Statistic.values()) {
            StatisticEntry statistic = getDatabase().find(StatisticEntry.class, statisticType.getValue());
            if (statistic == null) {
                new StatisticEntry(statisticType).save();
            }
        }
    }
}

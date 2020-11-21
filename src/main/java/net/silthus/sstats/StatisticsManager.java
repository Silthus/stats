package net.silthus.sstats;

import io.ebean.Database;
import lombok.AccessLevel;
import lombok.Getter;
import net.silthus.sstats.entities.StatisticType;
import net.silthus.sstats.entities.Statistic;

public final class StatisticsManager {

    @Getter(AccessLevel.PACKAGE)
    private final Database database;

    public StatisticsManager(Database database) {
        this.database = database;
    }

    public void initStatistics() {

        for (Statistic statisticType : Statistic.values()) {
            StatisticType statistic = getDatabase().find(StatisticType.class, statisticType.getValue());
            if (statistic == null) {
                new StatisticType(statisticType).save();
            }
        }
    }
}

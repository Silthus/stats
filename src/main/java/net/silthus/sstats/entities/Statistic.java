package net.silthus.sstats.entities;

import io.ebean.annotation.DbEnumValue;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

@Getter
public enum Statistic {

    ONLINE_TIME("Online Time", "The time in milliseconds the player has been online on the server."),
    PLAYER_COUNT("Player Count", "The number of online players on the server.");

    private final String name;
    private final String description;

    Statistic(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public StatisticEntry add(OfflinePlayer player, String key, Object value) {

        StatisticEntry statisticEntry = StatisticEntry.of(player, this);
        statisticEntry.data().put(key, value);
        statisticEntry.save();
        return statisticEntry;
    }

    public StatisticEntry increment(OfflinePlayer player, String key, long value) {

        StatisticEntry statisticEntry = StatisticEntry.of(player, this);
        long currentValue = (long) statisticEntry.data().getOrDefault(key, 0L);
        currentValue += value;
        statisticEntry.data().put(key, currentValue);
        statisticEntry.save();
        return statisticEntry;
    }

    public StatisticEntry max(String key, long value) {

        StatisticEntry entry = StatisticEntry.of(this);
        long currentCount = (long) entry.data().getOrDefault(key, 0L);
        if (value >= currentCount) {
            entry.data().put(key, value);
            entry.save();
        }
        return entry;
    }

    public StatisticEntry set(String key, Object value) {
        StatisticEntry entry = StatisticEntry.of(this);
        entry.data().put(key, value);
        entry.save();
        return entry;
    }

    public StatisticEntry get(OfflinePlayer player) {

        return StatisticEntry.of(player, this);
    }

    @DbEnumValue
    public String getValue() {
        return name();
    }
}

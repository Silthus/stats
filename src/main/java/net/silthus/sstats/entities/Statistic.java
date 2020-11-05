package net.silthus.sstats.entities;

import io.ebean.annotation.DbEnumValue;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

@Getter
public enum Statistic {

    ONLINE_TIME("Online Time", "The time in milliseconds the player has been online on the server.");

    private final String name;
    private final String description;

    Statistic(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public PlayerStatistic add(OfflinePlayer player, String key, Object value) {

        PlayerStatistic playerStatistic = PlayerStatistic.of(player, this);
        playerStatistic.data().put(key, value);
        playerStatistic.save();
        return playerStatistic;
    }

    public PlayerStatistic increment(OfflinePlayer player, String key, long value) {

        PlayerStatistic playerStatistic = PlayerStatistic.of(player, this);
        long currentValue = (long) playerStatistic.data().getOrDefault(key, 0L);
        currentValue += value;
        playerStatistic.data().put(key, currentValue);
        playerStatistic.save();
        return playerStatistic;
    }

    public PlayerStatistic get(OfflinePlayer player) {

        return PlayerStatistic.of(player, this);
    }

    @DbEnumValue
    public String getValue() {
        return name();
    }
}

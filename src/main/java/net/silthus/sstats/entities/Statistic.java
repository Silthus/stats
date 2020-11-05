package net.silthus.sstats.entities;

import io.ebean.annotation.DbEnumValue;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

@Getter
public enum Statistic {

    ONLINE_TIME("Online Time", "The time in seconds the player has been online on the server.");

    private final String name;
    private final String description;

    Statistic(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public PlayerStatistic add(OfflinePlayer player, String key, Object value) {

        PlayerStatistic playerStatistic = PlayerStatistic.of(player, this);
        playerStatistic.data().put(key, value);
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

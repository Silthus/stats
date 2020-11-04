package net.silthus.sstats.entities;

import io.ebean.annotation.DbEnumValue;
import lombok.Getter;

@Getter
public enum StatisticType {

    ONLINE_TIME("online_time", "Online Time", "The time in seconds the player has been online on the server.");

    private final String identifier;
    private final String name;
    private final String description;

    StatisticType(String identifier, String name, String description) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
    }

    @DbEnumValue
    public String getValue() {
        return name();
    }
}

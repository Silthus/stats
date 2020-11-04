package net.silthus.sstats.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sstats_statistics")
@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class Statistic {

    @Id
    private long id;
    private String identifier;
    private StatisticType type;
    private String name;
    private String description;
    private String source;
    private boolean enabled = true;

    private List<PlayerStatistic> playerStatistics = new ArrayList<>();

    public Statistic() {
    }

    public Statistic(StatisticType type, String name, String description, String source) {
        this.type = type;
        this.identifier = type.getIdentifier();
        this.name = name;
        this.description = description;
        this.source = source;
    }

    public Statistic(StatisticType type, String name, String description) {
        this(type, name, description, null);
    }

    public Statistic(StatisticType type, String name) {
        this(type, name, type.getDescription());
    }

    public Statistic(StatisticType type) {
        this(type, type.getName(), type.getDescription());
    }
}

package net.silthus.sstats.entities;

import io.ebean.Finder;
import io.ebean.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sstats_statistics")
@Getter
@Setter
@Accessors(fluent = true)
public class StatisticEntry extends Model {

    public static final Finder<String, StatisticEntry> finder = new Finder<>(StatisticEntry.class);

    @Id
    private String id;
    private String name;
    private String description;
    private String source;
    private boolean enabled = true;

    @OneToMany
    private List<PlayerStatistic> playerStatistics = new ArrayList<>();

    public StatisticEntry() {
    }

    public StatisticEntry(Statistic type, String name, String description, String source) {
        this.id = type.name();
        this.name = name;
        this.description = description;
        this.source = source;
    }

    public StatisticEntry(Statistic type, String name, String description) {
        this(type, name, description, null);
    }

    public StatisticEntry(Statistic type, String name) {
        this(type, name, type.getDescription());
    }

    public StatisticEntry(Statistic type) {
        this(type, type.getName(), type.getDescription());
    }
}

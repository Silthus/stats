package net.silthus.sstats.entities;

import io.ebean.ValuePair;
import io.ebean.annotation.DbJson;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "sstats_log")
@Getter
@Setter
@Accessors(fluent = true)
public class StatisticLog extends BaseEntity {

    @ManyToOne
    private StatisticEntry statisticEntry;
    @DbJson
    private Map<String, ValuePair> diff = new HashMap<>();

    public StatisticLog() {

    }

    public StatisticLog(StatisticEntry statisticEntry, Map<String, ValuePair> diff) {

        statisticEntry(statisticEntry);
        diff(diff);
    }
}

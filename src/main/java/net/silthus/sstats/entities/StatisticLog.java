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
    private PlayerStatistic playerStatistic;
    @DbJson
    private Map<String, ValuePair> diff = new HashMap<>();

    public StatisticLog() {

    }

    public StatisticLog(PlayerStatistic playerStatistic, Map<String, ValuePair> diff) {

        playerStatistic(playerStatistic);
        diff(diff);
    }
}

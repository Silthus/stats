package net.silthus.sstats.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.processing.Generated;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sstats_player_statistics")
@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class PlayerStatistic {

    @Id
    private long id;
    private String playerId;
    private String playerName;
    private long count;

    @ManyToOne
    private Statistic statistic;

    public PlayerStatistic() {
    }
}

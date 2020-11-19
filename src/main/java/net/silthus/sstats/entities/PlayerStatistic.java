package net.silthus.sstats.entities;

import io.ebean.Finder;
import io.ebean.ValuePair;
import io.ebean.annotation.DbJson;
import io.ebean.annotation.Transactional;
import io.ebean.text.json.EJson;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.sstats.entities.query.QPlayerStatistic;
import org.bukkit.OfflinePlayer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "sstats_player_statistics")
@Getter
@Setter
@Accessors(fluent = true)
public class PlayerStatistic extends BaseEntity {

    static {
        try {
            EJson.write(new Object());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static PlayerStatistic of(OfflinePlayer player, String type) {

        return new QPlayerStatistic()
                .playerId.eq(player.getUniqueId())
                .and()
                .statistic.id.eq(type)
                .findOneOrEmpty()
                .orElse(new PlayerStatistic(player, type));
    }

    public static PlayerStatistic of(OfflinePlayer player, Statistic type) {

        return of(player, type.getValue());
    }

    public static final Finder<UUID, PlayerStatistic> find = new Finder<>(PlayerStatistic.class);

    private UUID playerId;
    private String playerName;
    @DbJson
    private Map<String, Object> data = new HashMap<>();

    @ManyToOne
    private StatisticEntry statistic;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<StatisticLog> logs = new ArrayList<>();

    public PlayerStatistic() {
    }

    PlayerStatistic(OfflinePlayer player, String type) {
        playerId(player.getUniqueId());
        playerName(player.getName());
        statistic(StatisticEntry.finder.byId(type));
    }

    PlayerStatistic(OfflinePlayer player, Statistic type) {

        this(player, type.getValue());
    }

    public PlayerStatistic add(String key, Object value) {

        data().put(key, value);
        return this;
    }

    public <TType> Optional<TType> get(String key, Class<TType> typeClass) {

        return Optional.ofNullable(typeClass.cast(data().get(key)));
    }

    @Override
    @Transactional
    public void save() {

        Map<String, Object> oldData = getOldData();
        super.save();
        log(oldData);
    }

    @Override
    @Transactional
    public void update() {

        Map<String, Object> oldData = getOldData();
        super.update();
        log(oldData);
    }

    private Map<String, Object> getOldData() {

        if (id() == null) return new HashMap<>();

        return PlayerStatistic.find.query().where().idEq(id()).findOneOrEmpty().map(PlayerStatistic::data).orElse(new HashMap<>());
    }

    private void log(Map<String, Object> oldData) {

        Map<String, ValuePair> diff = new HashMap<>();
        if (!oldData.isEmpty()) {
            for (Map.Entry<String, Object> entry : this.data().entrySet()) {
                diff.put(entry.getKey(), new ValuePair(entry.getValue(), oldData.get(entry.getKey())));
            }
            for (Map.Entry<String, Object> entry : oldData.entrySet()) {
                if (!diff.containsKey(entry.getKey())) {
                    diff.put(entry.getKey(), new ValuePair(null, entry.getValue()));
                }
            }
        }
        new StatisticLog(this, diff).save();
    }
}

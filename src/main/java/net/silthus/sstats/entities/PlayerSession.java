package net.silthus.sstats.entities;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbEnumValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sstats_player_sessions")
@Getter
@Setter
@Accessors(fluent = true)
public class PlayerSession extends Model {

    public static final String STATS_TIME_PLAYED = "time_played";

    public static PlayerSession start(Player player) {
        PlayerSession playerSession = new PlayerSession(player);
        playerSession.save();
        return playerSession;
    }

    @Id
    private UUID id;
    private UUID playerId;
    private String playerName;
    private Instant joined;
    private Instant quit;
    private Reason reason;

    @Transient
    private Player player;

    public PlayerSession() {
    }

    public PlayerSession(Player player) {
        this.player = player;
        playerId(player.getUniqueId());
        playerName(player.getName());
        joined(Instant.now());
    }

    public void end() {
        end(Reason.QUIT);
    }

    public void end(Reason reason) {

        quit(Instant.now());
        reason(reason);
        update();

        Statistic.ONLINE_TIME.increment(player(), STATS_TIME_PLAYED, quit().toEpochMilli() - joined().toEpochMilli());
    }

    public enum Reason {
        KICK,
        BAN,
        QUIT,
        SHUTDOWN,
        UNKNOWN;

        @DbEnumValue
        public String getValue() {
            return name();
        }
    }
}

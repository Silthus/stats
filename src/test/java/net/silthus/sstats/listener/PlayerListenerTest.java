package net.silthus.sstats.listener;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.silthus.ebean.Config;
import net.silthus.ebean.EbeanWrapper;
import net.silthus.sstats.StatisticsManager;
import net.silthus.sstats.entities.*;
import net.silthus.sstats.entities.StatisticType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerListenerTest {

    private ServerMock server;
    private PlayerListener playerListener;
    private StatisticsManager statisticsManager;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        playerListener = new PlayerListener();
        this.statisticsManager = new StatisticsManager(new EbeanWrapper(Config.builder().entities(
                PlayerSession.class,
                StatisticEntry.class,
                StatisticType.class,
                StatisticLog.class
        ).build()).connect());
        this.statisticsManager.initStatistics();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("should store player played time on quit as statistic")
    void shouldSavePlayedTimeAsStatistic() {

        PlayerMock player = server.addPlayer();
        playerListener.onPlayerJoin(new PlayerJoinEvent(player, null));
        playerListener.onPlayerQuit(new PlayerQuitEvent(player, null));

        Optional<Long> actual = Statistic.ONLINE_TIME.get(player).get(PlayerSession.STATS_TIME_PLAYED, Long.class);
        assertThat(actual).isPresent();
        assertThat(actual.get()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should store world specific online time on world change")
    void shouldStoreWorldOnlineTime() {

        PlayerMock player = server.addPlayer();
        playerListener.onPlayerJoin(new PlayerJoinEvent(player, null));
        playerListener.onWorldChange(new PlayerChangedWorldEvent(player, player.getWorld()));

        Optional<Long> actual = Statistic.ONLINE_TIME.get(player).get(PlayerSession.STATS_WORLD_TIME_PREFIX + player.getWorld().getUID(), Long.class);
        assertThat(actual).isPresent();
        assertThat(actual.get()).isGreaterThan(0);
    }
}
package net.silthus.sstats.listener;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.silthus.sstats.entities.PlayerSession;
import net.silthus.sstats.entities.Statistic;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerListenerTest {

    private ServerMock server;
    private PlayerListener playerListener;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        playerListener = new PlayerListener();
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
        player.remove();

        Optional<Long> actual = Statistic.ONLINE_TIME.get(player).get(PlayerSession.STATS_TIME_PLAYED, Long.class);
        assertThat(actual).isPresent();
        assertThat(actual.get()).isGreaterThan(0);
    }
}
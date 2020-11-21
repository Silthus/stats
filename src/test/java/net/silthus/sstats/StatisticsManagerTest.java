package net.silthus.sstats;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.ebean.ValuePair;
import net.silthus.ebean.Config;
import net.silthus.ebean.EbeanWrapper;
import net.silthus.sstats.entities.*;
import net.silthus.sstats.entities.StatisticType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticsManagerTest {

    private StatisticsManager statisticsManager;

    private static ServerMock server;

    @BeforeAll
    static void beforeAll() {

        //        MockBukkit.loadWith(StatsPlugin.class, new File("build/resources/test/plugin.yml"));
        server = MockBukkit.mock();
    }

    @AfterAll
    static void afterAll() {

        MockBukkit.unmock();
    }

    @BeforeEach
    void setUp() {
        this.statisticsManager = new StatisticsManager(new EbeanWrapper(Config.builder().entities(
                PlayerSession.class,
                StatisticEntry.class,
                StatisticType.class,
                StatisticLog.class
        ).build()).connect());
        this.statisticsManager.initStatistics();
    }

    @Nested
    @DisplayName("initStatistics()")
    class initStatistics {

        @Test
        @DisplayName("should create statistic types")
        void shouldCreateStatisticsForAllEnumTypes() {

            statisticsManager.initStatistics();
            assertThat(statisticsManager.getDatabase().find(StatisticType.class).findList())
                    .hasSameSizeAs(Statistic.values());
        }

        @Test
        @DisplayName("should only create one entry per type")
        void shouldOnlyCreateUniqueTypes() {

            statisticsManager.initStatistics();
            statisticsManager.initStatistics();
            assertThat(statisticsManager.getDatabase().find(StatisticType.class).findList())
                    .hasSameSizeAs(Statistic.values());
        }
    }

    @Nested
    @DisplayName("database operations")
    class database {

        @Nested
        @DisplayName("PlayerStatistic")
        class StatisticEntryBean {

            @Test
            @DisplayName("should create a entry in the database")
            void shouldSavePlayerStats() {

                PlayerMock player = server.addPlayer();
                StatisticEntry statisticEntry = StatisticEntry.of(player, Statistic.ONLINE_TIME).data(Map.of("online_time", 5));
                statisticEntry.save();

                assertThat(statisticsManager.getDatabase().find(StatisticEntry.class).findList())
                        .contains(statisticEntry);
            }

            @Test
            @DisplayName("should update entry data in the database")
            void shouldUpdateEntryData() {

                PlayerMock player = server.addPlayer();
                StatisticEntry.of(player, Statistic.ONLINE_TIME).data(Map.of("online_time", 5L)).save();
                StatisticEntry.of(player, Statistic.ONLINE_TIME).data(Map.of("online_time", 10L)).save();

                assertThat(statisticsManager.getDatabase().find(StatisticEntry.class).findList())
                        .hasSize(1)
                        .first()
                        .extracting(StatisticEntry::data)
                        .isEqualTo(Map.of("online_time", 10L));
            }

            @Test
            @DisplayName("should add and update player statistic with direct access")
            void shouldAddAndUpdatePlayerStatistic() {

                PlayerMock player = server.addPlayer();
                Statistic.ONLINE_TIME.add(player, "foo", "bar")
                        .add("playtime", 10000L)
                        .save();

                assertThat(statisticsManager.getDatabase().find(StatisticEntry.class).findList())
                        .hasSize(1)
                        .first()
                        .extracting(StatisticEntry::playerId, StatisticEntry::data)
                        .contains(player.getUniqueId(), Map.of("foo", "bar", "playtime", 10000L));
            }

            @Test
            @DisplayName("should get player stats directly from database")
            void shouldGetPlayerStatsDirectly() {

                PlayerMock player = server.addPlayer();
                Statistic.ONLINE_TIME.add(player, "foo", "bar")
                        .add("playtime", 10000L)
                        .save();

                StatisticEntry statistic = Statistic.ONLINE_TIME.get(player);
                assertThat(statistic.get("playtime", Long.class))
                        .contains(10000L);
                assertThat(statistic.get("foo", String.class))
                        .contains("bar");
            }

            @Test
            @DisplayName("should auto create a log entry")
            void shouldAutoCreateLog() {

                PlayerMock player = server.addPlayer();
                StatisticEntry statisticEntry = StatisticEntry.of(player, Statistic.ONLINE_TIME).data(Map.of("online_time", 5));
                statisticEntry.save();

                List<StatisticLog> list = statisticsManager.getDatabase().find(StatisticLog.class).findList();
                assertThat(list)
                        .hasSize(1)
                        .first()
                        .extracting(StatisticLog::statisticEntry, statisticLog -> statisticLog.diff().size())
                        .contains(statisticEntry, 0);
            }

            @Test
            @DisplayName("should save diff into log")
            void shouldSaveDiffToLog() {

                PlayerMock player = server.addPlayer();
                StatisticEntry.of(player, Statistic.ONLINE_TIME)
                        .data(Map.of("online_time", 5)).save();
                StatisticEntry.of(player, Statistic.ONLINE_TIME)
                        .data(Map.of("online_time", 10, "last_logon", "today")).save();

                List<StatisticLog> list = statisticsManager.getDatabase().find(StatisticLog.class).findList();
                assertThat(list).hasSize(2);

                StatisticLog statisticLog = list.get(1);
                assertThat(statisticLog.diff().get("online_time"))
                        .extracting(ValuePair::getNewValue, ValuePair::getOldValue)
                        .contains(10, 5);
                ValuePair last_logon = statisticLog.diff().get("last_logon");
                assertThat(last_logon.getNewValue()).isEqualTo("today");
                assertThat(last_logon.getOldValue()).isNull();
            }
        }
    }
}
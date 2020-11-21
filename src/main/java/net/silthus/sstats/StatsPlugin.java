package net.silthus.sstats;

import io.ebean.Database;
import kr.entree.spigradle.annotations.PluginMain;
import lombok.Getter;
import net.silthus.ebean.Config;
import net.silthus.ebean.EbeanWrapper;
import net.silthus.sstats.entities.PlayerSession;
import net.silthus.sstats.entities.StatisticEntry;
import net.silthus.sstats.entities.StatisticType;
import net.silthus.sstats.entities.StatisticLog;
import net.silthus.sstats.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

@PluginMain
public class StatsPlugin extends JavaPlugin {

    @Getter
    private StatisticsManager statisticsManager;
    private PlayerListener playerListener;

    public StatsPlugin() {
    }

    public StatsPlugin(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        this.statisticsManager = new StatisticsManager(connectToDatabase());
        this.statisticsManager.initStatistics();

        this.playerListener = new PlayerListener();
        Bukkit.getPluginManager().registerEvents(playerListener, this);
    }

    @Override
    public void onDisable() {

        HandlerList.unregisterAll(playerListener);
        playerListener.getSessions().values().forEach(playerSession -> playerSession.end(PlayerSession.Reason.SHUTDOWN));
        playerListener.getSessions().clear();
    }

    private Database connectToDatabase() {

        Config dbConfig = Config.builder(this)
                .entities(
                        PlayerSession.class,
                        StatisticEntry.class,
                        StatisticType.class,
                        StatisticLog.class
                )
                .build();

        return new EbeanWrapper(dbConfig).getDatabase();
    }
}

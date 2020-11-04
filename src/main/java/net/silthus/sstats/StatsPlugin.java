package net.silthus.sstats;

import com.google.common.io.Resources;
import io.ebean.Database;
import kr.entree.spigradle.annotations.PluginMain;
import lombok.Getter;
import net.silthus.ebean.Config;
import net.silthus.ebean.EbeanWrapper;
import net.silthus.ebean.JarUtil;
import net.silthus.sstats.entities.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@PluginMain
public class StatsPlugin extends JavaPlugin {

    @Getter
    private StatisticsManager statisticsManager;

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
    }

    private Database connectToDatabase() {

        FileConfiguration config = getConfig();

        Config dbConfig = Config.builder()
                .driverPath(new File("lib"))
                .autoDownloadDriver(true)
                .runMigrations(true)
                .migrationClass(getClass())
                .entities(Statistic.class)
                .url(config.getString("database.url"))
                .username(config.getString("database.username"))
                .password(config.getString("database.password"))
                .driver(config.getString("database.driver"))
                .build();
        return new EbeanWrapper(dbConfig).getDatabase();
    }
}

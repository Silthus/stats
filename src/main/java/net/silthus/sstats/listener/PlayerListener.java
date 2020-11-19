package net.silthus.sstats.listener;

import lombok.Getter;
import net.silthus.sstats.entities.PlayerSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerListener implements Listener {

    @Getter
    private final Map<UUID, PlayerSession> sessions = new HashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {

        startSession(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        endSession(event.getPlayer(), PlayerSession.Reason.QUIT);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {

        endSession(event.getPlayer(), PlayerSession.Reason.KICK);
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {

        endSession(event.getPlayer(), PlayerSession.Reason.CHANGED_WORLD);
        startSession(event.getPlayer());
    }

    private void startSession(Player player) {

        sessions.put(player.getUniqueId(), PlayerSession.start(player));
    }

    private void endSession(Player player, PlayerSession.Reason reason) {

        Optional.ofNullable(sessions.remove(player.getUniqueId()))
                .ifPresent(playerSession -> playerSession.end(reason));
    }
}

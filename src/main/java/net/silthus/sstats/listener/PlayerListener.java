package net.silthus.sstats.listener;

import lombok.Getter;
import net.silthus.sstats.entities.PlayerSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

        sessions.put(event.getPlayer().getUniqueId(), PlayerSession.start(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Optional.ofNullable(sessions.remove(event.getPlayer().getUniqueId())).ifPresent(PlayerSession::end);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerKickEvent event) {

        Optional.ofNullable(sessions.remove(event.getPlayer().getUniqueId())).ifPresent(playerSession -> playerSession.end(PlayerSession.Reason.KICK));
    }
}

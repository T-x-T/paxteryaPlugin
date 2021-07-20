package com.paxterya.moderatorTools;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class SpectatorHider implements Listener {
  private final JavaPlugin plugin;

  public SpectatorHider(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onGamemodeChangeHandler(PlayerGameModeChangeEvent event) {
    Bukkit.getScheduler().runTask(plugin, () -> {
      Player player = event.getPlayer();
      if(player.getGameMode().equals(GameMode.SPECTATOR)) {
        hidePlayer(player);
      } else {
        showPlayer(player);
      }
    });
  }

  private void hidePlayer(Player playerToHide) {
    Bukkit.getOnlinePlayers().forEach((player) -> {
      if(!player.getUniqueId().equals(playerToHide.getUniqueId())) {
        player.hidePlayer(plugin, playerToHide);
      }
    });
    playerToHide.performCommand("dynmap hide");
  }

  private void showPlayer(Player playerToShow) {
    Bukkit.getOnlinePlayers().forEach((player) -> {
      if (!player.getUniqueId().equals(playerToShow.getUniqueId())) {
        player.showPlayer(plugin, playerToShow);
      }
    });
    playerToShow.performCommand("dynmap show");
  }
}

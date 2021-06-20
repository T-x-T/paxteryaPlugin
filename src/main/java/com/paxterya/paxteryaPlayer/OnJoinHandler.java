package com.paxterya.paxteryaPlayer;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinHandler implements Listener {
  private PaxteryaPlugin plugin;

  public OnJoinHandler(PaxteryaPlugin plugin){
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    System.out.println("player joined");
    plugin.getTablistNameWrapper().updateDefaultSuffix(event.getPlayer());
  }
}

package com.paxterya.paxteryaPlayer;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;

import com.sun.istack.NotNull;
import org.bukkit.entity.Player;

public class PaxteryaPlayer {

  PaxteryaPlugin plugin;
  TablistNameWrapper tablist;
  Player player;

  public PaxteryaPlayer(PaxteryaPlugin plugin, Player player){
    this.plugin = plugin;
    this.player = player;
    this.tablist = plugin.getTablistNameWrapper();
  }

  @NotNull
  public void setPrefix(String prefix){
    tablist.addPrefix(player, prefix);
  }

  @NotNull
  public void setSuffix(String suffix){
    tablist.addSuffix(player, suffix);
  }

  public void removePrefix(){
    tablist.removeAllPrefixes(player);
  }

  public void removeSuffix(){
    tablist.removeAllSuffixes(player);
  }
}

package com.paxterya.paxteryaPlayer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class TablistNameWrapper {
  private final Plugin plugin;
  private final Map<Player, String> prefixes;
  private final Map<Player, String> suffixes;

  public TablistNameWrapper(Plugin plugin) {
    this.plugin = plugin;
    this.prefixes = new HashMap<>();
    this.suffixes = new HashMap<>();
  }

  public void addPrefix(Player player, String prefix){
    prefixes.put(player, prefix);
    player.setPlayerListName(prefix + " " + player.getPlayerListName());
  }

  public void addSuffix(Player player, String suffix){
    suffixes.put(player, suffix);
    player.setPlayerListName(player.getPlayerListName() + " " + suffix);
  }

  public void removeAllPrefixes(Player player){
    prefixes.remove(player);
    String suffixToSet = suffixes.get(player);
    if(suffixToSet != null){
      player.setPlayerListName(player.getName() + " " + suffixToSet);
    }else{
      resetTablistNameToDefault(player);
    }
  }

  public void removeAllSuffixes(Player player){
    suffixes.remove(player);
    String prefixToSet = prefixes.get(player);
    if(prefixToSet != null){
      player.setPlayerListName(prefixToSet + " " + player.getName());
    }else{
      resetTablistNameToDefault(player);
    }
  }

  private void resetTablistNameToDefault(Player player){
    player.setPlayerListName(player.getName());
  }
}

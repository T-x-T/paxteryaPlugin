package com.paxterya.tablistNameWrapper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablistNameWrapper {
  private final Plugin plugin;
  private Map<Player, String> prefixes;
  private Map<Player, String> suffixes;

  public TablistNameWrapper(Plugin plugin) {
    this.plugin = plugin;
    this.prefixes = new HashMap<>();
    this.suffixes = new HashMap<>();
  }

  public void addPrefixIfNoPrefixSet(Player player, String prefix){
    if(!prefixes.containsKey(player)) addPrefix(player, prefix);
  }

  public void addPrefix(Player player, String prefix){
    prefixes.put(player, prefix);
    player.setPlayerListName(prefix + " " + player.getPlayerListName());
  }

  public void addSuffixIfNoSuffixSet(Player player, String suffix){
    if(!suffixes.containsKey(player)) addSuffix(player, suffix);
  }

  public void addSuffix(Player player, String suffix){
    suffixes.put(player, suffix);
    player.setPlayerListName(player.getPlayerListName() + " " + suffix);
  }

  public void removeAllPrefixes(Player player){
    prefixes.remove(player);
    String suffixToSet = suffixes.get(player);
    if(suffixToSet.length() > 0){
      player.setPlayerListName(player.getName() + " " + suffixToSet);
    }else{
      resetTablistNameToDefault(player);
    }
  }

  public void removeAllSuffixes(Player player){
    suffixes.remove(player);
    String prefixToSet = prefixes.get(player);
    if(prefixToSet.length() > 0){
      player.setPlayerListName(prefixToSet + " " + player.getName());
    }else{
      resetTablistNameToDefault(player);
    }
  }

  private void resetTablistNameToDefault(Player player){
    player.setPlayerListName(player.getName());
  }
}

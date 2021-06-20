package com.paxterya.paxteryaPlayer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class TablistNameWrapper {
  private final Plugin plugin;
  private final Map<Player, String> prefixes;
  private final Map<Player, String> suffixes;
  private final Map<Player, String> defaultSuffixes;

  public TablistNameWrapper(Plugin plugin) {
    this.plugin = plugin;
    this.prefixes = new HashMap<>();
    this.suffixes = new HashMap<>();
    this.defaultSuffixes = new HashMap<>();
  }

  public void addPrefix(Player player, String prefix){
    prefixes.put(player, prefix);
    updatePlayerListName(player);
  }

  public void addSuffix(Player player, String suffix){
    suffixes.put(player, suffix);
    updatePlayerListName(player);
  }

  public void removeAllPrefixes(Player player){
    prefixes.remove(player);
    updatePlayerListName(player);
  }

  public void removeAllSuffixes(Player player){
    suffixes.remove(player);
    updatePlayerListName(player);
  }

  public void updateDefaultSuffix(Player player) {
    String defaultSuffix = DefaultSuffixFromServerFetcher.getFromApi(player);
    this.defaultSuffixes.put(player, defaultSuffix);
    this.updatePlayerListName(player);
  }

  public void updatePlayerListName(Player player){
    StringBuilder newPlayerListName = new StringBuilder();

    String prefix = prefixes.get(player);
    if(prefix != null && prefix.length() > 0) {
      newPlayerListName.append(prefix);
      newPlayerListName.append(' ');
    }

    newPlayerListName.append(player.getName());

    String suffix = suffixes.get(player);
    if(suffix != null && suffix.length() > 0) {
      newPlayerListName.append(' ');
      newPlayerListName.append(suffix);
    }

    String defaultSuffix = defaultSuffixes.get(player);
    if(defaultSuffix != null && defaultSuffix.length() > 0) {
      if(suffix != null && suffix.length() > 0) {
        newPlayerListName = new StringBuilder(newPlayerListName.substring(0, newPlayerListName.length() - 1)); //Remove last ]
        newPlayerListName.append(" | ");
        newPlayerListName.append(defaultSuffix);
        newPlayerListName.append(']');
      } else {
        newPlayerListName.append(" §8[");
        newPlayerListName.append(defaultSuffix);
        newPlayerListName.append(']');
      }
    }

    player.setPlayerListName(newPlayerListName.toString());
  }
}

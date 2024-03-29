package com.paxterya.afk;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;
import com.paxterya.paxteryaPlayer.PaxteryaPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfkCore {
  private final PaxteryaPlugin plugin;
  private final Map<Player, Boolean> afkStates;
  private final Map<Player, Location> afkPositions; //The position of the player at the time when they went afk
  private final Map<Player, Long> afkTimes; //The time in millis when the player went afk
  private final List<Player> newPlayers; //Contains new players for that no "is no longer afk" messages should be sent

  public AfkCore(PaxteryaPlugin plugin){
    this.plugin = plugin;
    afkStates = new HashMap<>();
    newPlayers = new ArrayList<>();
    afkPositions = new HashMap<>();
    afkTimes = new HashMap<>();
  }

  public void setAfkState(Player player, Boolean newState){
    afkStates.put(player, newState);
    if(newState){
      broadcastAfk(player);
      setTabList(player);
      afkPositions.put(player, player.getLocation());
      afkTimes.put(player, System.currentTimeMillis());
    }else{
      broadcastUnAfk(player);
      resetTabList(player);
    }
  }

  public Boolean getAfkState(Player player){
    Boolean afkState = afkStates.get(player);
    if(afkState == null){
      setAfkState(player, false);
      afkState = false;
    }
    return afkState;
  }

  public Location getAfkLocation(Player player){
    return afkPositions.get(player);
  }

  public Boolean toggleAfkState(Player player){
    if(getAfkState(player)){
      setAfkState(player, false);
      return false;
    }else{
      setAfkState(player, true);
      return true;
    }
  }

  public void addNewPlayer(Player player){
    newPlayers.add(player);
  }

  public List<Player> getAfkPlayers(){
    List<Player> afkPlayers = new ArrayList<>();

    afkStates.forEach((player, isAfk) -> {
      if(isAfk) afkPlayers.add(player);
    });

    return afkPlayers;
  }

  public long getAfkTimeInMillisOfPlayer(Player player){
    return afkTimes.get(player);
  }

  private void broadcastAfk(Player player){
    Bukkit.getServer().broadcastMessage(String.format("§8§l%s §r§3is now afk", player.getDisplayName()));
    player.sendTitle("§4You are now AFK", "Move a bit or use /afk once you're back", 10, 200, 10);
  }

  private void broadcastUnAfk(Player player){
    if(newPlayers.contains(player)){
      newPlayers.remove(player);
      return;
    }
    String displayName = player.getDisplayName();
    double afkTime;
    if(!afkTimes.containsKey(player)){
      afkTime = 0;
    }else{
      afkTime = (System.currentTimeMillis() - afkTimes.get(player)) / 1000.0 / 60.0; //Minutes
    }

    String afkTimeStr = "";
    if(afkTime >= 60){
      afkTimeStr += String.valueOf(Math.round(afkTime / 60.0)) + "h";
      afkTime = afkTime % 60;
    }
    afkTimeStr += String.valueOf(Math.round(afkTime)) + "m";

    Bukkit.getServer().broadcastMessage(String.format("§2§l%1$s §r§3is back after %2$s", displayName, afkTimeStr));
    player.sendTitle("§2You are no longer AFK", "Idle for 5 Minutes or use /afk to go afk again", 10, 70, 10);
  }

  private void setTabList(Player player){
    new PaxteryaPlayer(plugin, player).setSuffix("§8[AFK]");
  }

  private void resetTabList(Player player){
    new PaxteryaPlayer(plugin, player).removeSuffix();
  }
}

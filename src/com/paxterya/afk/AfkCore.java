package com.paxterya.afk;

import com.paxterya.role.RoleManager;
import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfkCore {
  JavaPlugin plugin;
  Map<Player, Boolean> afkStates;
  Map<Player, Location> afkPositions; //The position of the player at the time when they went afk
  List<Player> newPlayers; //Contains new players for that no "is no longer afk" messages should be sent

  public AfkCore(JavaPlugin plugin){
    this.plugin = plugin;
    afkStates = new HashMap<>();
    newPlayers = new ArrayList<>();
    this.afkPositions = new HashMap<>();
  }

  public void setAfkState(Player player, Boolean newState){
    if(afkStates.containsKey(player)){
      afkStates.replace(player, newState);
    }else{
      afkStates.put(player, newState);
    }
    if(newState){
      broadcastAfk(player);
      setTabList(player);
      afkPositions.put(player, player.getLocation());
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


  private void broadcastAfk(Player player){
    Bukkit.getServer().broadcastMessage(String.format("§8§l%s §r§3is now afk", player.getDisplayName()));
    player.sendTitle("§4You are now AFK", "Move more than three blocks or use /afk to tell everyone you're back", 10, 200, 10);
  }

  private void broadcastUnAfk(Player player){
    if(newPlayers.contains(player)){
      newPlayers.remove(player);
      return;
    }
    Bukkit.getServer().broadcastMessage(String.format("§2§l%s §r§3is no longer afk", player.getDisplayName()));
    player.sendTitle("§2You are no longer AFK", "Welcome back! Stand still for 2 Minutes or use /afk to go afk again", 10, 70, 10);
  }

  private void setTabList(Player player){
    player.setPlayerListName(player.getPlayerListName() + " §8[AFK]");
  }

  private void resetTabList(Player player){
    RoleManager roleManager = new RoleManager(this.plugin);
    roleManager.setRole(player, roleManager.getRoleId(player));
  }
}

package com.paxterya.afk;

import com.paxterya.role.RoleManager;
import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class AfkCore {
  JavaPlugin plugin;
  Map<Player, Boolean> afkStates;

  public AfkCore(JavaPlugin plugin){
    this.plugin = plugin;
    afkStates = new HashMap<>();
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
    }else{
      broadcastUnAfk(player);
      resetTabList(player);
    }
  }

  @Nullable
  public Boolean getAfkState(Player player){
    return afkStates.get(player);
  }

  public Boolean toggleAfkState(Player player){
    if(getAfkState(player) == null){
      setAfkState(player, true);
      return true;
    }
    if(getAfkState(player)){
      setAfkState(player, false);
      return false;
    }else{
      setAfkState(player, true);
      return true;
    }
  }


  private void broadcastAfk(Player player){
    Bukkit.getServer().broadcastMessage(String.format("%s is now afk", player.getDisplayName()));
  }

  private void broadcastUnAfk(Player player){
    Bukkit.getServer().broadcastMessage(String.format("%s is no longer afk", player.getDisplayName()));
  }

  private void setTabList(Player player){
    player.setPlayerListName(player.getPlayerListName() + " [AFK]");
  }

  private void resetTabList(Player player){
    RoleManager roleManager = new RoleManager(this.plugin);
    roleManager.setRole(player, roleManager.getRoleId(player));
  }
}

package com.paxterya.afk;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class AutoAfkManager implements Listener {
  JavaPlugin plugin;
  AfkCore afkCore;

  Map<Player, Long> lastActionTimes; //The last time the player performed a relevant action
  Map<Player, Location> lastPositions; //The last position of the player

  int afkTime; //Time in ms that player can stay inactive without getting marked afk automatically

  public AutoAfkManager(JavaPlugin plugin, AfkCore afkCore){
    this.plugin = plugin;
    this.afkCore = afkCore;
    this.lastActionTimes = new HashMap<>();
    this.lastPositions = new HashMap<>();
    this.afkTime = 1000 * 60 * 2;
    updateAfkTimer();
  }

  @EventHandler
  public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
    resetLastActionTime(event.getPlayer());
  }

  @EventHandler
  public void onPlayerMovement(PlayerMoveEvent event){
    Player player = event.getPlayer();

    //Create new Location containing only coords, no mouse movement
    Location location = player.getLocation();
    location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());

    if(afkCore.getAfkState(player)){
      if(location.distance(lastPositions.get(player)) > 0 && lastPositions.get(player).distance(afkCore.getAfkLocation(player)) > 3) {
        resetLastActionTime(player);
      }
    }else{
      resetLastActionTime(player);
    }
    lastPositions.replace(player, location);
  }


  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event){
    registerPlayer(event.getPlayer());
  }

  private void resetLastActionTime(Player player){
    if(player.getGameMode() != GameMode.SURVIVAL) return;
    this.lastActionTimes.replace(player, System.currentTimeMillis());
    if(afkCore.getAfkState(player)) afkCore.setAfkState(player, false);
  }

  private void updateAfkTimer(){
    new BukkitRunnable(){

      @Override
      public void run() {
        updateAfk();
      }
    }.runTaskTimer(plugin, 0, 20);
  }

  private void updateAfk(){
    for(Player player : Bukkit.getOnlinePlayers()){
      updateAfk(player);
    }
  }

  private void updateAfk(Player player){
    if(player.getGameMode() != GameMode.SURVIVAL) return;
    if(!afkCore.getAfkState(player) && getLastActionTime(player) + afkTime <= System.currentTimeMillis()){
      afkCore.setAfkState(player, true);
    }
  }

  private Long getLastActionTime(Player player){
    Long lastActionTime = lastActionTimes.get(player);

    if(lastActionTime == null){
      registerPlayer(player);
      lastActionTime = lastActionTimes.get(player);
    }

    return lastActionTime;
  }

  private void registerPlayer(Player player){
    lastActionTimes.put(player, System.currentTimeMillis());
    lastPositions.put(player, player.getLocation());
    afkCore.addNewPlayer(player);
  }
}
package com.paxterya.afk;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public class AfkPlayerKicker {

  private final PaxteryaPlugin plugin;
  private final AfkCore afkCore;
  private BukkitTask timerTask;

  private final int kickAfterAfkMinutes;
  private final int checkIntervallInSeconds = 10;

  public AfkPlayerKicker(PaxteryaPlugin plugin, AfkCore afkCore, int kickAfterAfkMinutes){
    this.plugin = plugin;
    this.afkCore = afkCore;
    this.kickAfterAfkMinutes = kickAfterAfkMinutes;

    startTimer();
  }

  public void stop(){
    timerTask.cancel();
  }

  private void startTimer(){
    timerTask = new BukkitRunnable(){
      @Override
      public void run(){
        kickPlayersAfterTooLongAfk();
      }
    }.runTaskTimer(plugin, 0, checkIntervallInSeconds * 20);
  }

  private void kickPlayersAfterTooLongAfk(){
    afkCore.getAfkPlayers().forEach((player) -> {
      if(isAfkTimeTooHigh(player)) player.kickPlayer("You have been afk for too long :(");
    });
  }

  private boolean isAfkTimeTooHigh(Player player){
    return System.currentTimeMillis() - afkCore.getAfkTimeInMillisOfPlayer(player) > kickAfterAfkMinutes * 60 * 1000;
  }
}

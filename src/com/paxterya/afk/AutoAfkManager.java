package com.paxterya.afk;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoAfkManager {
  JavaPlugin plugin;
  AfkCore afkCore;

  public AutoAfkManager(JavaPlugin plugin, AfkCore afkCore){
    this.plugin = plugin;
    this.afkCore = afkCore;
  }
}

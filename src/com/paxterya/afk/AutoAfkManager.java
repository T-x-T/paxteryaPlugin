package com.paxterya.afk;

import org.bukkit.plugin.Plugin;

public class AutoAfkManager {
  Plugin plugin;
  AfkCore afkCore;

  public AutoAfkManager(Plugin plugin, AfkCore afkCore){
    this.plugin = plugin;
    this.afkCore = afkCore;
  }
}

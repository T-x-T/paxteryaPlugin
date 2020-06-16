package com.paxterya.afk;

import org.bukkit.plugin.Plugin;

public class AfkCommandHandler {
  Plugin plugin;
  AfkCore afkCore;

  public AfkCommandHandler(Plugin plugin, AfkCore afkCore){
    this.plugin = plugin;
    this.afkCore = afkCore;
  }
}

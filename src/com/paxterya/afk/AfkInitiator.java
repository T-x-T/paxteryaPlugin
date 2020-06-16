package com.paxterya.afk;

import org.bukkit.plugin.Plugin;

public class AfkInitiator {
  Plugin plugin;

  public AfkInitiator(Plugin plugin){
    this.plugin = plugin;

    AfkCore afkCore = new AfkCore(plugin);
    AfkCommandHandler afkCommandHandler = new AfkCommandHandler(this.plugin, afkCore);
    AutoAfkManager autoAfkManager = new AutoAfkManager(this.plugin, afkCore);
  }
}

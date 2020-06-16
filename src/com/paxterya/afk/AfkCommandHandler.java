package com.paxterya.afk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class AfkCommandHandler implements CommandExecutor {
  Plugin plugin;
  AfkCore afkCore;

  public AfkCommandHandler(Plugin plugin, AfkCore afkCore){
    this.plugin = plugin;
    this.afkCore = afkCore;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    return false;
  }
}

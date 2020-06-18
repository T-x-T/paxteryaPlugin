package com.paxterya.afk;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AfkCommandHandler implements CommandExecutor {
  JavaPlugin plugin;
  AfkCore afkCore;

  public AfkCommandHandler(JavaPlugin plugin, AfkCore afkCore){
    this.plugin = plugin;
    this.afkCore = afkCore;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    this.afkCore.toggleAfkState(Bukkit.getPlayer(commandSender.getName()));
    return true;
  }
}

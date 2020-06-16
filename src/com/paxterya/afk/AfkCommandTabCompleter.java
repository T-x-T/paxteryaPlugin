package com.paxterya.afk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class AfkCommandTabCompleter implements TabCompleter {
  Plugin plugin;

  public AfkCommandTabCompleter(Plugin plugin){
    this.plugin = plugin;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    return null;
  }
}

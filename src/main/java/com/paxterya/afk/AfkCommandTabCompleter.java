package com.paxterya.afk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AfkCommandTabCompleter implements TabCompleter {
  JavaPlugin plugin;

  public AfkCommandTabCompleter(JavaPlugin plugin){
    this.plugin = plugin;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    return new ArrayList<>();
  }
}

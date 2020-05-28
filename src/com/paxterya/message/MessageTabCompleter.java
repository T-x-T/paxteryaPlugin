package com.paxterya.message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MessageTabCompleter implements TabCompleter {

  JavaPlugin plugin;

  public MessageTabCompleter(JavaPlugin plugin){
    this.plugin = plugin;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

    if(alias.equals("r")){
      return new ArrayList<>();
    }

    if(args.length <= 1){
      return null;
    }

    return new ArrayList<>();
  }
}

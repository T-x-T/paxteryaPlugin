package com.paxterya.moderatorTools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ModeratorCommandTablistCompleter implements TabCompleter {
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if(args.length == 1) {
      ArrayList<String> list = new ArrayList<>();
      list.add("inventory");
      list.add("enderchest");
      return list;
    }

    if(args.length == 2) {
      return null;
    }
    
    return new ArrayList<>();
  }
}

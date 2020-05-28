package com.paxterya.message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageTabCompleter implements TabCompleter {

  JavaPlugin plugin;
  private static final String[] COMMANDS = { "" };

  public MessageTabCompleter(JavaPlugin plugin){
    this.plugin = plugin;

  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    final List<String> completions = new ArrayList<>();

    if(alias.equals("r")){
      return new ArrayList<>();
    }

    if(args.length <= 1){
      return null;
    }

    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);

    return completions;
  }
}

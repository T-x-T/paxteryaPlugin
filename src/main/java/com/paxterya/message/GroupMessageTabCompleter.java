package com.paxterya.message;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GroupMessageTabCompleter implements TabCompleter {
  JavaPlugin plugin;
  private static final String[] COMMANDS = { "accept", "deny", "leave", "list" };

  public GroupMessageTabCompleter(JavaPlugin plugin){
    this.plugin = plugin;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

    //Don't complete on group reply
    if(alias.equalsIgnoreCase("greply") || alias.equalsIgnoreCase("gr") || alias.equalsIgnoreCase("gm")){
      return new ArrayList<>();
    }

    //Don't complete when there is more than one argument for all sub-commands (no sub-command == inviting players)
    if(args.length > 1 && (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("list"))){
      return new ArrayList<>();
    }

    //Add player names to completion when there are no arguments given
    if(args.length == 1){
      //Create list for newCompletions and add existing commands
      List<String> newCompletions = new ArrayList<>(Arrays.asList(COMMANDS));

      //Get online players
      Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

      //Add names of online players to newCommands
      onlinePlayers.forEach((player) -> {
        newCompletions.add(player.getDisplayName());
      });

      List<String> returnCompletions = new ArrayList<>();
      StringUtil.copyPartialMatches(args[0], newCompletions, returnCompletions);
      return returnCompletions;
    }

    //Return player names when none of the sub-commands are used
    if(args.length > 1 && !(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("list"))){
      return null;
    }

    final List<String> completions = new ArrayList<>();
    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);


    return completions;
  }
}

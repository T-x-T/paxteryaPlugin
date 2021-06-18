package com.paxterya.afk;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AfkCommandHandler implements CommandExecutor {
  JavaPlugin plugin;
  AfkCore afkCore;

  public AfkCommandHandler(JavaPlugin plugin, AfkCore afkCore){
    this.plugin = plugin;
    this.afkCore = afkCore;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    switch(command.getName()) {
      case "afk": {
        this.afkCore.toggleAfkState(Bukkit.getPlayer(commandSender.getName()));
        return true;
      }

      case "getafkplayers": {
        List<Player> afkPlayers = afkCore.getAfkPlayers();
        StringBuilder output = new StringBuilder();

        afkPlayers.forEach((Player player) -> {
          output.append(player.getDisplayName());
          output.append(',');
        });
        if(output.length() > 0) output.deleteCharAt(output.length() - 1);
        commandSender.sendMessage(output.toString());

        return true;
      }

      default: {
        return false;
      }
    }
  }
}

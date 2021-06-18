package com.paxterya.paxteryaplugin;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PaxteryaCommand implements CommandExecutor {
  private final PaxteryaPlugin plugin;

  public PaxteryaCommand(PaxteryaPlugin plugin){
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!sender.isOp()) return false;

    switch(args[0].toLowerCase()){
      case "reload":
        reload();
        break;
      case "inactivesince":
        List<String> inactivePlayerNames = new ArrayList<>();
        plugin.getServer().getWhitelistedPlayers().forEach((OfflinePlayer player) -> {
          if(Instant.parse(args[1] + "T00:00:00.000Z").getEpochSecond() * 1000 > player.getLastPlayed()){
            inactivePlayerNames.add(player.getName());
          }
        });
        sender.sendMessage(String.join(";", inactivePlayerNames));
        break;
      case "lastjoined":
        StringBuilder output = new StringBuilder();
        plugin.getServer().getWhitelistedPlayers().forEach((OfflinePlayer player) -> {
          output.append(player.getName());
          output.append(':');
          output.append(player.getLastPlayed());
          output.append(';');
        });
        sender.sendMessage(output.toString());
        break;
      default:
        return false;
    }
    return true;
  }

  private void reload(){
    plugin.reloadPaxteryaConfig();
  }
}

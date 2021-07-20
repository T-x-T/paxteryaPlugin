package com.paxterya.moderatorTools;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;
import com.paxterya.role.Roles;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeratorCommand implements CommandExecutor {

  private final PaxteryaPlugin plugin;
  private final Roles roles;

  public ModeratorCommand(PaxteryaPlugin plugin) {
    this.plugin = plugin;
    this.roles = plugin.getRoles();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Player senderPlayer = Bukkit.getPlayer(sender.getName());
    if(!senderPlayer.getGameMode().equals(GameMode.SPECTATOR)) {
      sender.sendMessage("This command only works in spectator mode!");
      return true;
    }

    switch(args[0].toLowerCase()){
      case "inventory": {
        if(args.length < 2) {
          sender.sendMessage("You need to specify a player too");
          break;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if(target == null) {
          sender.sendMessage("Player not found");
          break;
        }
        senderPlayer.openInventory(target.getInventory());
        break;
      }
      case "enderchest": {
        if (args.length < 2) {
          sender.sendMessage("You need to specify a player too");
          break;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
          sender.sendMessage("Player not found");
          break;
        }
        senderPlayer.openInventory(target.getEnderChest());
        break;
      }
    }
    return true;
  }
  
}

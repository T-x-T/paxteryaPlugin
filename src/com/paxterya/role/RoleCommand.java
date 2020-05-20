package com.paxterya.role;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class RoleCommand implements CommandExecutor {

  JavaPlugin plugin;
  RoleManager roleManager;

  public RoleCommand(JavaPlugin plugin) {
    this.plugin = plugin;
    roleManager = new RoleManager(plugin);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    //Check if sender is op
    if(!sender.isOp()) {
      //Sender is not op
      sender.sendMessage("You are not allowed to do that!");
      return true;
    }

    //Check if the sender used the correct argument
    if(!args[0].equals("get")) {
      //No valid argument given
      return false;
    }
    //Check if the supplied username is valid

    //Get whitelist
    Set<OfflinePlayer> whitelist = Bukkit.getWhitelistedPlayers();

    //Iterate over whitelist to find player
    Boolean playerExists = false;
    OfflinePlayer target = null;
    for(OfflinePlayer offlinePlayer: whitelist){
      if(offlinePlayer.getName().equals(args[1])) {
        playerExists = true;
        target = offlinePlayer;
      }
    }

    if(!playerExists) {
      sender.sendMessage("Player wasn't found :(");
      return true;
    }

    if(args[0].equals("get")){
      sender.sendMessage(roleManager.getRole(target));
    }else{
      sender.sendMessage("something went horribly wrong");
    }


    //Just return true, because when we got here everything should be fine
    return true;
  }
}

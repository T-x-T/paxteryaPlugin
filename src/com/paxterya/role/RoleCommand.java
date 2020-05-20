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

    //Check if the sender used a correct argument
    if(!(args[0].equals("set") || args[0].equals("get"))) {
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

    //Check which sub-command got used and call correct function
    if(args[0].equals("set")){

      //Check if supplied roleName exists
      if(!(args[2].equalsIgnoreCase("player") || args[2].equalsIgnoreCase("mayor") || args[2].equalsIgnoreCase("mod") || args[2].equalsIgnoreCase("admin") || args[2].equalsIgnoreCase("cool"))){
        sender.sendMessage("Valid roles are player, cool, mayor, mod or admin! " + args[2] + " is NOT valid!");
        return true;
      }

      sender.sendMessage(roleManager.setRole(target, args[2].toLowerCase()));
    }else{
      if(args[0].equals("get")){
        sender.sendMessage(roleManager.getRole(target));
      }else{
        sender.sendMessage("something went horribly wrong");
      }
    }

    //Just return true, because when we got here everything should be fine
    return true;
  }
}

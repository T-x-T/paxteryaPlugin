package com.paxterya.role;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Set;

public class RoleCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    //Check if sender is op
    if(sender.isOp()){
      //Check if the sender used a correct argument
      if(args[0].equals("set") || args[0].equals("get")){
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

        if(playerExists){
          sender.sendMessage("Player found :)");

          //Check which sub-command got used and call correct function
          if(args[0].equals("set")){
            setRole(target, args[2]);
          }else{
            if(args[0].equals("get")){
              getRole(target);
            }else{
              sender.sendMessage("something went horribly wrong");
            }
          }
        }else{
          sender.sendMessage("Player wasn't found :(");
        }
      }else{
        //No valid argument given
        return false;
      }
    }else{
      //Sender is not op
      sender.sendMessage("You are not allowed to do that!");
      return true;
    }
    return true;
  }
  //Sets the role of a single player
  private String setRole(OfflinePlayer player, String newRoleID){
    String output = "";

    return output;
  }

  //Gets the role of a singe player
  private String getRole(OfflinePlayer player){
    String output = "";

    return output;
  }
}

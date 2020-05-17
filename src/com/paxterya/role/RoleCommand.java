package com.paxterya.role;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
          //Check which sub-command got used and call correct function
          if(args[0].equals("set")){
            sender.sendMessage(setRole(target, args[2]));
          }else{
            if(args[0].equals("get")){
              sender.sendMessage(getRole(target, sender));
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

    //Set up local variables
    String output = "";
    String path = "./plugins/paxterya/roles/";
    String strToWrite = newRoleID;

    //Check if directory exists, create if not
    File directory = new File(path);
    if(!directory.exists()){
      directory.mkdirs();
    }

    //Write file to directory with newRoleID as content
    path += player.getUniqueId();
    try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"))) {
      writer.write(strToWrite);
    } catch (IOException e) {
      e.printStackTrace();
    }

    output += "The role of player " + player.getName() + " is " + newRoleID;
    return output;
  }

  //Gets the role of a singe player
  private String getRole(OfflinePlayer player, CommandSender sender){

    //Set up local variables
    String output = "";
    String path = "./plugins/paxterya/roles/" + player.getUniqueId();
    String roleStr = "0";

    //Read the contents of the file
    try {
      roleStr = new String(Files.readAllBytes(Paths.get(path)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Parse str to int
    int role = Integer.parseInt(roleStr);

    //Form and return output
    output += "The role of player " + player.getName() + " is " + role;
    return output;
  }
}

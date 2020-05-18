package com.paxterya.role;

import org.bukkit.OfflinePlayer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class RoleManager {
  RoleManager() {

  }

  //Sets the role of a single player
  protected String setRole(OfflinePlayer player, String newRoleName){
    //Convert newRoleName to newRoleID
    int newRoleID = convert(newRoleName);

    //Set up local variables
    String output = "";
    String path = "./plugins/paxterya/roles/";
    String strToWrite = String.valueOf(newRoleID);

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

    //Set prefix in tab-list
    if(player.isOnline()){
      setPrefix(player, newRoleID);
      output = "The role of player " + player.getName() + " is " + newRoleName;
    }else{
      output = "The prefix of the player won't be updated when the player isn't online! Saved changes to disk anyways.";
    }

    return output;
  }

  //Gets the role of a singe player
  protected String getRole(OfflinePlayer player){

    //Set up local variables
    String output = "";
    String path = "./plugins/paxterya/roles/" + player.getUniqueId();
    String roleStr = "0";

    //Check if file exists, create if not
    File fileToTest = new File(path);
    if(!fileToTest.isFile()){
      return player.getName() + " has no recorded role";
    }

    //Read the contents of the file
    try {
      roleStr = new String(Files.readAllBytes(Paths.get(path)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Parse str to int
    int roleID = Integer.parseInt(roleStr);

    //Convert roleID to roleName
    String roleName = convert(roleID);

    //Form and return output
    output += "The role of player " + player.getName() + " is " + roleName;
    return output;
  }

  private void setPrefix(OfflinePlayer player, int roleID){
    player.getPlayer().setPlayerListName(getPretty(roleID) + " " + player.getName());
  }

  protected String convert(int roleID){
    String output;

    switch (roleID){
      case 3:
        output = "player";
        break;
      case 5:
        output = "mayor";
        break;
      case 7:
        output = "mod";
        break;
      case 9:
        output = "admin";
        break;

      default:
        output = "false";
        break;
    }

    return output;
  }

  protected int convert(String roleName){
    int output;

    switch (roleName){
      case "player":
        output = 3;
        break;
      case "mayor":
        output = 5;
        break;
      case "mod":
        output = 7;
        break;
      case "admin":
        output = 9;
        break;

      default:
        output = 0;
        break;
    }

    return output;
  }

  private String getPretty(int roleID){
    String output = "";

    switch (roleID){
      case 5:
        output = "[§6mayor§r]";
        break;
      case 7:
        output = "[§cmod§r]";
        break;
      case 9:
        output = "[§4admin§r]";
        break;

      default:
        output = "false";
        break;
    }

    return output;
  }
}

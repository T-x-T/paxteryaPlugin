package com.paxterya.role;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class RoleManager {

  private JavaPlugin plugin;

  RoleManager(JavaPlugin plugin) {
    this.plugin = plugin;
  }


  //Sets the role of a single player
  protected String setRole(OfflinePlayer player, String newRoleName){
    //Convert newRoleName to newRoleID
    int newRoleID = getId(newRoleName);

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
    String roleName = getName(roleID);

    //Form and return output
    output += "The role of player " + player.getName() + " is " + roleName;
    return output;
  }

  private void setPrefix(OfflinePlayer player, int roleID){
    //Get prefix
    String prefix = getPrefix(roleID);

    //Check if the prefix has a length, if not just set the players name (this is important to clear any preexisting prefixes)
    if(prefix.length() == 0) {
      player.getPlayer().setPlayerListName(player.getName());
      return;

    }

    //Prefix has length, set it
    player.getPlayer().setPlayerListName(prefix + " " + player.getName());
  }

  protected String getName(int roleID){
    //Read in the list from the config
    List<Map<String, String>> roles = (List<Map<String, String>>) plugin.getConfig().getList("roles");

    //Iterate over the list and return the name of the Map that has the same id as roleID
    for(Map<String, String> element : roles) if(element.get("id").equals(String.valueOf(roleID))) return element.get("name");

    //When we get here, we didn't find a match
    return "false";
  }

  protected int getId(String roleName){
    //Read in the list from the config
    List<Map<String, String>> roles = (List<Map<String, String>>) plugin.getConfig().getList("roles");

    //Iterate over the list and return the name of the Map that has the same id as roleID
    for(Map<String, String> element : roles) if(element.get("name").equals(roleName)) return Integer.parseInt(element.get("id"));

    //When we get here, we didn't find a match
    return 0;
  }

  private String getPrefix(int roleID){
    //Read in the list from the config
    List<Map<String, String>> roles = (List<Map<String, String>>) plugin.getConfig().getList("roles");

    //Iterate over the list and return the name of the Map that has the same id as roleID
    for(Map<String, String> element : roles) if(element.get("id").equals(String.valueOf(roleID)) && element.containsKey("prefix")) return element.get("prefix");

    //When we get here, we didn't find a match
    return "";
  }
}

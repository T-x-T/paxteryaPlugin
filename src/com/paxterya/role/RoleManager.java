package com.paxterya.role;

import org.bukkit.OfflinePlayer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class RoleManager {
  public RoleManager() {

  }

  //Sets the role of a single player
  protected String setRole(OfflinePlayer player, String newRoleName){
    //Convert newRoleName to newRoleID
    RoleManager roleManager = new RoleManager();
    int newRoleID = roleManager.convert(newRoleName);

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

    output += "The role of player " + player.getName() + " is " + newRoleName;
    return output;
  }

  //Gets the role of a singe player
  protected String getRole(OfflinePlayer player){

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
    int roleID = Integer.parseInt(roleStr);

    //Convert roleID to roleName
    RoleManager roleManager = new RoleManager();
    String roleName = roleManager.convert(roleID);

    //Form and return output
    output += "The role of player " + player.getName() + " is " + roleName;
    return output;
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
}

package com.paxterya.role;

import com.paxterya.tablistNameWrapper.TablistNameWrapper;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class RoleManager {

  private final Plugin plugin;
  private final String roleFolderPath;

  protected RoleManager(Plugin plugin) {
    this.plugin = plugin;
    this.roleFolderPath = "./plugins/paxterya/roles/";
  }


  //Sets the role of a single player
  protected void setRole(Player player, int newRoleID){
    String path = roleFolderPath;
    String strToWrite = String.valueOf(newRoleID);

    //Check if directory exists, create if not
    File directory = new File(path);
    if(!directory.exists()){
      directory.mkdirs();
    }

    //Write file to directory with newRoleID as content
    path += player.getUniqueId();
    try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
      writer.write(strToWrite);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Set prefix in tab-list and set permissions
    if(player.isOnline()) {
      setPrefix(player, newRoleID);
      setPermissions(player, newRoleID);
    }
  }

  private void saveNewRole(Player player, int newRoleID){

  }

  //Gets the role of a singe player
  protected String getRole(OfflinePlayer player){

    //Set up local variables
    String output = "";
    String path = roleFolderPath + player.getUniqueId();
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

  private void setPrefix(Player player, int roleID){
    new TablistNameWrapper(plugin).addPrefixIfNoPrefixSet(player, getPrefix(roleID));
  }

  protected String getName(int roleID){
    try{
      Map<String, String> config = getConfigOfRoleID(roleID);
      return config.get("name");
    }catch (NullPointerException e){
      return "false";
    }
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
    try{
      Map<String, String> config = getConfigOfRoleID(roleID);
      return config.get("prefix");
    }catch (NullPointerException e){
      return "";
    }
  }

  private void setPermissions(Player player, int newRoleID){
    List<String> permissions = getAllPermissionsByRoleID(newRoleID);
    PermissionAttachment attachment = player.addAttachment(plugin);
    permissions.forEach((permission) -> {
      attachment.setPermission(permission, true);
    });
    player.recalculatePermissions();
    boolean opStatus = player.isOp();
    player.setOp(true);
    player.setOp(false);
    player.setOp(opStatus);
  }

  private List<String> getAllPermissionsByRoleID(int roleID){
    List<String> permissions = new ArrayList<String>();
    for(int i = roleID; i >= 0; i--){
      try{
        permissions.addAll(getPermissionsByRoleID(i));
      }catch (NullPointerException ignored){}
    }
    return permissions;
  }

  private List<String> getPermissionsByRoleID(int roleID) throws NullPointerException {
    List<?> roles = plugin.getConfig().getList("roles");
    for(Object element : roles){
      Map<String, String> stringMap = (Map<String, String>)element;
      if(stringMap.get("id").equals(String.valueOf(roleID))){
        Map<String, List<String>> listMap = (Map<String, List<String>>)element;
        return listMap.get("permissions");
      }
    }
    throw new NullPointerException();
  }

  private Map<String, String> getConfigOfRoleID(int roleID) throws NullPointerException{
    List<Map<String, String>> roles = (List<Map<String, String>>) plugin.getConfig().getList("roles");
    for(Map<String, String> element : roles) if(element.get("id").equals(String.valueOf(roleID))) return element;
    throw new NullPointerException();
  }
}

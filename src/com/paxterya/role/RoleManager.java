package com.paxterya.role;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;
import com.paxterya.tablistNameWrapper.TablistNameWrapper;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RoleManager {

  private final PaxteryaPlugin plugin;
  private Map<Player, Integer> playerRoleMappings;
  private final TablistNameWrapper tablistNameWrapper;

  protected RoleManager(PaxteryaPlugin plugin) {
    this.plugin = plugin;
    this.playerRoleMappings = new HashMap<>();
    this.tablistNameWrapper = this.plugin.getTablistNameWrapper();
  }

  //Sets the role of a single player
  protected void setRole(Player player, int newRoleID){
    playerRoleMappings.put(player, newRoleID);
    setPrefix(player, newRoleID);
    setPermissions(player, newRoleID);
  }

  private void setPrefix(Player player, int roleID){
    tablistNameWrapper.addPrefixIfNoPrefixSet(player, getPrefix(roleID));
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
    permissions.forEach((permission) -> attachment.setPermission(permission, true));
    fixTabAutocompleteBug(player);
  }

  private void fixTabAutocompleteBug(Player player){
    player.recalculatePermissions();
    boolean opStatus = player.isOp();
    player.setOp(true);
    player.setOp(false);
    player.setOp(opStatus);
  }

  private List<String> getAllPermissionsByRoleID(int roleID){
    List<String> permissions = new ArrayList<>();
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

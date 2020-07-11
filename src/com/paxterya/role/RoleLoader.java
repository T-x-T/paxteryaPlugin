package com.paxterya.role;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

class RoleLoader {

  private static PaxteryaPlugin plugin;

  private RoleLoader(){}

  protected static List<Role> getAllRolesFromConfig(PaxteryaPlugin inputPlugin){
    plugin = inputPlugin;
    try {
      return getAllConfigEntriesAndParseThem();
    }catch (NullPointerException e){
      plugin.getLogger().log(Level.WARNING, "Failed loading all roles from config.yml");
      throw new NullPointerException();
    }
  }

  private static List<Role> getAllConfigEntriesAndParseThem() throws NullPointerException{
    List<Role> allRoles = new ArrayList<>();
    List<Map<String, ?>> configEntries = getAllConfigEntries();
    if(configEntries == null) throw new NullPointerException();
    for(Map<String, ?> configEntry : configEntries){
      allRoles.add(createRoleObjectFromConfig(configEntry));
    }
    return allRoles;
  }

  private static List<Map<String, ?>> getAllConfigEntries(){
    return (List<Map<String, ?>>) plugin.getConfig().getList("roles");
  }

  private static Role createRoleObjectFromConfig(Map<String, ?> configEntry){
    return new Role(plugin, Integer.parseInt((String)configEntry.get("id")), (String)configEntry.get("prefix"), (String)configEntry.get("name"), (List<String>)configEntry.get("permissions"));
  }
}

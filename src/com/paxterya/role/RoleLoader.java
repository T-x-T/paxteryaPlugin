package com.paxterya.role;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;

import java.util.*;
import java.util.logging.Level;

class RoleLoader {

  private static PaxteryaPlugin plugin;

  private RoleLoader(){}

  protected static List<Role> getAllRolesFromConfig(PaxteryaPlugin inputPlugin){
    plugin = inputPlugin;
    try {
      return getAllConfigEntriesAndParseThem();
    }catch (NullPointerException e){
      e.printStackTrace();
      plugin.getLogger().log(Level.WARNING, "Failed loading all roles from config.yml");
      throw new NullPointerException();
    }
  }

  private static List<Role> getAllConfigEntriesAndParseThem() throws NullPointerException{
    List<Role> allRoles = new ArrayList<>();
    List<Map<String, ?>> configEntries = getAllConfigEntries();
    configEntries = applyLowerPermissions(configEntries);
    if(configEntries == null) throw new NullPointerException();

    for(Map<String, ?> configEntry : configEntries){
      allRoles.add(createRoleObjectFromConfig(configEntry));
    }
    return allRoles;
  }

  private static List<Map<String, ?>> getAllConfigEntries(){
    return (List<Map<String, ?>>) plugin.getConfig().getList("roles");
  }

  private static List<Map<String, ?>> applyLowerPermissions(List<Map<String, ?>> configEntries){
    Map<Integer, List<String>> allPermissions = getPermissions(configEntries);
    SortedMap<Integer, List<String>> allPermissionsSorted = new TreeMap<>(allPermissions);

    List<String> permissionsCache = new ArrayList<>();
    List<Map<String, ?>> newConfigEntries = new ArrayList<>();
    for(int currentRole = 1; currentRole <= 9; currentRole++){
      if(allPermissionsSorted.get(currentRole) != null) {

        for(Map<String, ?> configEntry : configEntries){
          if(Integer.parseInt((String) configEntry.get("id")) == currentRole){
            permissionsCache.forEach((permission) -> ((List<String>)configEntry.get("permissions")).add(permission));
            newConfigEntries.add(configEntry);
          }
        }

        permissionsCache.addAll(allPermissionsSorted.get(currentRole));
      }
    }

    return newConfigEntries;
  }

  private static Map<Integer, List<String>> getPermissions(List<Map<String, ?>> configEntries){
    Map<Integer, List<String>> allPermissions = new HashMap<>();
    for(Map<String, ?> configEntry : configEntries){
      allPermissions.put(Integer.parseInt((String)configEntry.get("id")), (List<String>)configEntry.get("permissions"));
    }
    return allPermissions;
  }

  private static Role createRoleObjectFromConfig(Map<String, ?> configEntry){
    return new Role(plugin, Integer.parseInt((String)configEntry.get("id")), (String)configEntry.get("prefix"), (String)configEntry.get("name"), (List<String>)configEntry.get("permissions"));
  }
}

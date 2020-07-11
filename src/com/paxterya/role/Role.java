package com.paxterya.role;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;

import java.util.List;

public class Role {

  PaxteryaPlugin plugin;

  private int id;
  private String prefix;
  private String name;
  private List<String> permissions;

  public Role(PaxteryaPlugin plugin, int id, String prefix, String name, List<String> permissions){
    this.plugin = plugin;
    this.id = id;
    this.prefix = prefix;
    this.name = name;
    this.permissions = permissions;
  }

  public String getPrefix(){
    return prefix;
  }

  public String getName(){
    return name;
  }

  public List<String> getPermissions(){
    return permissions;
  }
}

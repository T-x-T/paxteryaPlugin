package com.paxterya.role;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;

import java.security.InvalidParameterException;
import java.util.List;

public class Role {

  PaxteryaPlugin plugin;

  private final int id;
  private final String prefix;
  private final String name;
  private final List<String> permissions;

  public Role(PaxteryaPlugin plugin, int id, String prefix, String name, List<String> permissions){
    if(!isRoleIdValid(id)) throw new InvalidParameterException("The given id is not valid");
    this.plugin = plugin;
    this.id = id;
    this.prefix = prefix;
    this.name = name;
    this.permissions = permissions;
  }

  private boolean isRoleIdValid(int id){
    return id >= 0 && id <= 9;
  }

  public int getId(){
    return id;
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

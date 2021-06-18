package com.paxterya.role;

import com.paxterya.paxteryaplugin.PaxteryaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Roles {

  private PaxteryaPlugin plugin;
  private List<Role> roles;

  public Roles(PaxteryaPlugin plugin){
    this.plugin = plugin;
    loadAllRolesFromConfig();
  }

  private void loadAllRolesFromConfig(){
    try{
      this.roles = RoleLoader.getAllRolesFromConfig(plugin);
    }catch(NullPointerException e){
      this.roles = new ArrayList<>();
    }
  }


  public Role getRoleById(int id){
    for(Role role : roles){
      if(role.getId() == id){
        return role;
      }
    }
    throw new NullPointerException("No role with the given id found");
  }

  public Role getRoleByName(String name){
    for(Role role : roles){
      if(role.getName().equalsIgnoreCase(name)){
        return role;
      }
    }
    throw new NullPointerException("No role with the given name found");
  }
}

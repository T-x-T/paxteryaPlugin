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

  }

  public Role getRoleByName(String name){

  }
}

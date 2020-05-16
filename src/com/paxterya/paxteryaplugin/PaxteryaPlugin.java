package com.paxterya.paxteryaplugin;

import com.paxterya.role.RoleCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.paxterya.role.RoleManager;

public class PaxteryaPlugin extends JavaPlugin {

  //Gets called once when plugin gets enabled
  @Override
  public void onEnable(){
    //Initialize the role manager
    RoleManager roleManager = new RoleManager();

    //Initialize the role command
    this.getCommand("role").setExecutor(new RoleCommand());
  }

  @Override
  //Get called once when plugin gets disabled
  public void onDisable(){

  }

}

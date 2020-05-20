package com.paxterya.paxteryaplugin;

import com.paxterya.role.RoleCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class PaxteryaPlugin extends JavaPlugin {

  public PaxteryaPlugin(){

  }

  //Gets called once when plugin gets enabled
  @Override
  public void onEnable(){

    //Save default config
    this.saveDefaultConfig();

    //Initialize the role package
    RoleCommand roleCommand = new RoleCommand(this);
    this.getCommand("role").setExecutor(roleCommand);

  }

  @Override
  //Get called once when plugin gets disabled
  public void onDisable(){

  }

}

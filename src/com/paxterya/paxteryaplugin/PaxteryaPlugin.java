package com.paxterya.paxteryaplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class PaxteryaPlugin extends JavaPlugin {

  //Gets called once when plugin gets enabled
  @Override
  public void onEnable(){
    System.out.println("plugin got enabled hard");
  }

  @Override
  //Get called once when plugin gets disabled
  public void onDisable(){
    System.out.println("plugin got disabled troll style");
  }

}

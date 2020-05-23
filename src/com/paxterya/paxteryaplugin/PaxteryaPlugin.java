package com.paxterya.paxteryaplugin;

import com.paxterya.message.GroupMessageCommand;
import com.paxterya.message.MessageCommand;
import com.paxterya.role.RoleCommand;
import com.paxterya.role.RoleUpdater;
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
    RoleUpdater roleUpdater = new RoleUpdater(this);
    this.getServer().getPluginManager().registerEvents(roleUpdater, this);



    //Initialize message package
    MessageCommand messageCommand = new MessageCommand(this);
    this.getCommand("msg").setExecutor(messageCommand);
    this.getCommand("dm").setExecutor(messageCommand);
    this.getCommand("w").setExecutor(messageCommand);
    this.getCommand("r").setExecutor(messageCommand);

    GroupMessageCommand groupMessageCommand = new GroupMessageCommand(this);
    this.getCommand("groupdm").setExecutor(groupMessageCommand);
    this.getCommand("gdm").setExecutor(groupMessageCommand);
    this.getCommand("greply").setExecutor(groupMessageCommand);
    this.getCommand("gmsg").setExecutor(groupMessageCommand);
    this.getCommand("gr").setExecutor(groupMessageCommand);

  }

  @Override
  //Get called once when plugin gets disabled
  public void onDisable(){

  }

}

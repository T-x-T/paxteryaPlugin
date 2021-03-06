package com.paxterya.paxteryaplugin;

import com.paxterya.afk.AfkCommandHandler;
import com.paxterya.afk.AfkCommandTabCompleter;
import com.paxterya.afk.AfkCore;
import com.paxterya.afk.AutoAfkManager;
import com.paxterya.message.GroupMessageCommand;
import com.paxterya.message.GroupMessageTabCompleter;
import com.paxterya.message.MessageCommand;
import com.paxterya.message.MessageTabCompleter;
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

    MessageTabCompleter messageTabCompleter = new MessageTabCompleter(this);
    this.getCommand("msg").setTabCompleter(messageTabCompleter);
    this.getCommand("dm").setTabCompleter(messageTabCompleter);
    this.getCommand("w").setTabCompleter(messageTabCompleter);
    this.getCommand("r").setTabCompleter(messageTabCompleter);


    GroupMessageCommand groupMessageCommand = new GroupMessageCommand(this);
    this.getCommand("groupdm").setExecutor(groupMessageCommand);
    this.getCommand("gdm").setExecutor(groupMessageCommand);
    this.getCommand("greply").setExecutor(groupMessageCommand);
    this.getCommand("gmsg").setExecutor(groupMessageCommand);
    this.getCommand("gr").setExecutor(groupMessageCommand);
    this.getCommand("gm").setExecutor(groupMessageCommand);

    GroupMessageTabCompleter groupMessageTabCompleter = new GroupMessageTabCompleter(this);
    this.getCommand("groupdm").setTabCompleter(groupMessageTabCompleter);
    this.getCommand("gdm").setTabCompleter(groupMessageTabCompleter);
    this.getCommand("greply").setTabCompleter(groupMessageTabCompleter);
    this.getCommand("gmsg").setTabCompleter(groupMessageTabCompleter);
    this.getCommand("gr").setTabCompleter(groupMessageTabCompleter);
    this.getCommand("gm").setTabCompleter(groupMessageTabCompleter);


    //Initialize afk package
    AfkCore afkCore = new AfkCore(this);
    AutoAfkManager autoAfkManager = new AutoAfkManager(this, afkCore);

    this.getCommand("afk").setExecutor(new AfkCommandHandler(this, afkCore));
    this.getCommand("afk").setTabCompleter(new AfkCommandTabCompleter(this));

    this.getServer().getPluginManager().registerEvents(autoAfkManager, this);
  }

  @Override
  //Get called once when plugin gets disabled
  public void onDisable(){

  }

}

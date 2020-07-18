package com.paxterya.paxteryaplugin;

import com.paxterya.afk.AfkCommandHandler;
import com.paxterya.afk.AfkCommandTabCompleter;
import com.paxterya.afk.AfkCore;
import com.paxterya.afk.AutoAfkManager;
import com.paxterya.message.GroupMessageCommand;
import com.paxterya.message.GroupMessageTabCompleter;
import com.paxterya.message.MessageCommand;
import com.paxterya.message.MessageTabCompleter;
import com.paxterya.paxteryaPlayer.TablistNameWrapper;
import com.paxterya.role.PlayerRoleUpdater;
import com.paxterya.role.Roles;
import com.paxterya.chatWordReplacer.ChatWordReplacer;
import org.bukkit.plugin.java.JavaPlugin;

public class PaxteryaPlugin extends JavaPlugin {

  private TablistNameWrapper tablistNameWrapper;
  private Roles allRoles;

  public PaxteryaPlugin(){
    this.tablistNameWrapper = new TablistNameWrapper(this);
  }

  //Gets called once when plugin gets enabled
  @Override
  public void onEnable(){
    this.tablistNameWrapper = new TablistNameWrapper(this);
    this.allRoles = new Roles(this);
    //Save default config
    this.saveDefaultConfig();

    //Initialize the role package
    PlayerRoleUpdater roleUpdater = new PlayerRoleUpdater(this);
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


    //Initialize wordReplacer
    ChatWordReplacer chatWordReplacer = new ChatWordReplacer(this);
    this.getServer().getPluginManager().registerEvents(chatWordReplacer, this);
  }

  @Override
  //Get called once when plugin gets disabled
  public void onDisable(){

  }

  public TablistNameWrapper getTablistNameWrapper(){
    return tablistNameWrapper;
  }

  public Roles getRoles(){
    return allRoles;
  }

}

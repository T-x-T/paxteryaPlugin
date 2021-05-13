package com.paxterya.paxteryaplugin;

import com.paxterya.afk.*;
import com.paxterya.chatWordReplacer.WordReplacer;
import com.paxterya.message.GroupMessageCommand;
import com.paxterya.message.GroupMessageTabCompleter;
import com.paxterya.message.MessageCommand;
import com.paxterya.message.MessageTabCompleter;
import com.paxterya.paxteryaPlayer.TablistNameWrapper;
import com.paxterya.role.PlayerRoleUpdater;
import com.paxterya.role.Roles;
import com.paxterya.chatWordReplacer.ChatWordReplacer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class PaxteryaPlugin extends JavaPlugin {

  private TablistNameWrapper tablistNameWrapper;
  private Roles allRoles;
  private AfkCore afkCore;
  private AfkPlayerKicker afkPlayerKicker;

  public PaxteryaPlugin(){

  }

  //Gets called once when plugin gets enabled
  @Override
  public void onEnable(){
    this.saveDefaultConfig();

    this.tablistNameWrapper = new TablistNameWrapper(this);
    this.allRoles = new Roles(this);

    //Initialize the role package
    PlayerRoleUpdater roleUpdater = new PlayerRoleUpdater(this);
    this.getServer().getPluginManager().registerEvents(roleUpdater, this);

    //Initialize wordReplacer. Other classes that are dependant on it will have it injected as a param.
    WordReplacer wordReplacer = new WordReplacer(this);

    //Initialize message package
    MessageCommand messageCommand = new MessageCommand(this, wordReplacer);
    this.getCommand("msg").setExecutor(messageCommand);
    this.getCommand("dm").setExecutor(messageCommand);
    this.getCommand("w").setExecutor(messageCommand);
    this.getCommand("r").setExecutor(messageCommand);

    MessageTabCompleter messageTabCompleter = new MessageTabCompleter(this);
    this.getCommand("msg").setTabCompleter(messageTabCompleter);
    this.getCommand("dm").setTabCompleter(messageTabCompleter);
    this.getCommand("w").setTabCompleter(messageTabCompleter);
    this.getCommand("r").setTabCompleter(messageTabCompleter);


    GroupMessageCommand groupMessageCommand = new GroupMessageCommand(this, wordReplacer);
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
    afkCore = new AfkCore(this);
    AutoAfkManager autoAfkManager = new AutoAfkManager(this, afkCore);
    afkPlayerKicker = new AfkPlayerKicker(this, afkCore, this.getConfig().getInt("afkKickTimeMinutes"));

    this.getCommand("afk").setExecutor(new AfkCommandHandler(this, afkCore));
    this.getCommand("afk").setTabCompleter(new AfkCommandTabCompleter(this));
    this.getCommand("getafkplayers").setExecutor(new AfkCommandHandler(this, afkCore));

    this.getServer().getPluginManager().registerEvents(autoAfkManager, this);


    //Initialize chatWordReplacer
    ChatWordReplacer chatWordReplacer = new ChatWordReplacer(this, wordReplacer);
    this.getServer().getPluginManager().registerEvents(chatWordReplacer, this);

    //Initialize reload
    PaxteryaCommand reloadCommand = new PaxteryaCommand(this);
    this.getCommand("paxterya").setExecutor(reloadCommand);
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

  public void reloadPaxteryaConfig(){
    afkPlayerKicker.stop();
    this.reloadConfig();
    PaxteryaPlugin plugin = this;
    new BukkitRunnable(){
      @Override
      public void run() {
        afkPlayerKicker = new AfkPlayerKicker(plugin, afkCore, plugin.getConfig().getInt("afkKickTimeMinutes"));
        allRoles = new Roles(plugin);
      }
    }.runTaskLater(this, 20);
  }

}

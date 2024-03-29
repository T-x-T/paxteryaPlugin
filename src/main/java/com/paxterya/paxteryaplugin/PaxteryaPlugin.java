package com.paxterya.paxteryaplugin;

import com.paxterya.afk.*;
import com.paxterya.base.BaseCommand;
import com.paxterya.base.BaseManager;
import com.paxterya.chatWordReplacer.ChatWordReplacer;
import com.paxterya.chatWordReplacer.NavigationCommand;
import com.paxterya.chatWordReplacer.WordReplacer;
import com.paxterya.dynmap.BaseDrawer;
import com.paxterya.dynmap.RegionDrawer;
import com.paxterya.message.GroupMessageCommand;
import com.paxterya.message.GroupMessageTabCompleter;
import com.paxterya.message.MessageCommand;
import com.paxterya.message.MessageTabCompleter;
import com.paxterya.moderatorTools.ModeratorCommand;
import com.paxterya.moderatorTools.ModeratorCommandTablistCompleter;
import com.paxterya.moderatorTools.SpectatorHider;
import com.paxterya.paxteryaPlayer.OnJoinHandler;
import com.paxterya.paxteryaPlayer.TablistNameWrapper;
import com.paxterya.region.RegionChangeListener;
import com.paxterya.region.RegionManager;
import com.paxterya.role.PlayerRoleUpdater;
import com.paxterya.role.Roles;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class PaxteryaPlugin extends JavaPlugin {

  private TablistNameWrapper tablistNameWrapper;
  private Roles allRoles;
  private AfkCore afkCore;
  private AfkPlayerKicker afkPlayerKicker;
  private RegionManager regionManager;
  private BaseManager baseManager;

  public PaxteryaPlugin(){

  }

  //Gets called once when plugin gets enabled
  @Override
  public void onEnable(){
    this.saveDefaultConfig();
    initLogger();

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

    //Initialize paxteryaPlayerOnJoinHandler
    OnJoinHandler onJoinHandler = new OnJoinHandler(this);
    this.getServer().getPluginManager().registerEvents(onJoinHandler, this);

    //Initialize reload
    PaxteryaCommand reloadCommand = new PaxteryaCommand(this);
    this.getCommand("paxterya").setExecutor(reloadCommand);


    //Regions
    regionManager = new RegionManager(this);
    this.getServer().getPluginManager().registerEvents(new RegionChangeListener(), this);
    RegionDrawer.drawRegionsLater(this, regionManager.getRegions(), 16);

    //ModeratorTools
    SpectatorHider spectatorHider = new SpectatorHider(this);
    this.getServer().getPluginManager().registerEvents(spectatorHider, this);
    ModeratorCommand moderatorCommand = new ModeratorCommand(this);
    this.getCommand("moderator").setExecutor(moderatorCommand);
    ModeratorCommandTablistCompleter moderatorCommandTablistCompleter = new ModeratorCommandTablistCompleter();
    this.getCommand("moderator").setTabCompleter(moderatorCommandTablistCompleter);

    //Navigation
    NavigationCommand navigationCommand = new NavigationCommand();
    this.getCommand("navigation").setExecutor(navigationCommand);
    this.getCommand("navigation").setTabCompleter(new NavigationCommand.TabCompleter());

    //Bases
    BaseManager.init(this);
    baseManager = BaseManager.instance;
    BaseCommand baseCommand = new BaseCommand(baseManager);
    this.getCommand("base").setExecutor(baseCommand);
    this.getCommand("base").setTabCompleter(new BaseCommand.TabCompleter());
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
    initLogger();
    PaxteryaPlugin plugin = this;
    new BukkitRunnable(){
      @Override
      public void run() {
        afkPlayerKicker = new AfkPlayerKicker(plugin, afkCore, plugin.getConfig().getInt("afkKickTimeMinutes"));
        allRoles = new Roles(plugin);
      }
    }.runTaskLater(this, 20);

    regionManager.reload(this);
    RegionDrawer.drawRegionsLater(this, regionManager.getRegions(), 16);

    baseManager.reload(this);
    BaseDrawer.drawBasesLater(this, baseManager.getBases(), 32);
  }

  private void initLogger() {
    Level level = Level.parse(getConfig().getString("logLevel", "INFO"));
    if (level == null) level = Level.INFO;
    Bukkit.getLogger().setLevel(level);
    Bukkit.getLogger().log(level, "[paxterya] Log level set to " + level.getName());
  }

}

package com.paxterya.paxteryaplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PaxteryaCommand implements CommandExecutor {
  private final PaxteryaPlugin plugin;

  public PaxteryaCommand(PaxteryaPlugin plugin){
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!sender.isOp()) return false;

    switch(args[0].toLowerCase()){
      case "reload":
        reload();
        break;
      default:
        return false;
    }
    return true;
  }

  private void reload(){
    plugin.reloadPaxteryaConfig();
  }
}

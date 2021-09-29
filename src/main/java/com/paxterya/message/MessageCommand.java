package com.paxterya.message;

import com.paxterya.chatWordReplacer.WordReplacer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MessageCommand implements CommandExecutor {

  JavaPlugin plugin;
  DmSender dmSender;
  WordReplacer wordReplacer;
  Map<Player, String> lastReceived;
  boolean shouldReplaceWords;

  public MessageCommand(JavaPlugin plugin, WordReplacer wordReplacer){
    this.plugin = plugin;
    this.wordReplacer = wordReplacer;
    this.dmSender = new DmSender(true, false);
    this.lastReceived = new HashMap<>();
    this.shouldReplaceWords = plugin.getConfig().getBoolean("replace_words_in.dm");
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    //Get sender as Player
    Player sender = Bukkit.getPlayer(commandSender.getName());

    if(label.equals("msg") || label.equals("w") || label.equals("dm")){
      //Check if message was provided
      if(args.length < 2){
        commandSender.sendMessage("Please provide the name of another player and a message");
        return false;
      }

      //Get recipient
      Player recipient = Bukkit.getPlayer(args[0]);

      //create the message by joining arguments from 1 to args.length
      String message = StringUtils.join(args," ",1, args.length);
      //if (shouldReplaceWords) message = wordReplacer.replaceWords(message, sender);

      //Check if supplied player exists
      if(recipient != null){
        msgCommand(sender, recipient, message);
      }else{
        sender.sendMessage("Player " + args[0] + " not found :(");
      }
      return true;
    }

    if(label.equals("r")){
      //Abort if user didn'r provide message
      if(args.length < 1){
        sender.sendMessage("Why do you try to reply without a message?");
        return false;
      }

      //create the message by joining arguments from 1 to args.length
      String message = StringUtils.join(args, " ");
      //if (shouldReplaceWords) message = wordReplacer.replaceWords(message, sender);

      rCommand(sender, message);
      return true;
    }

    return true;
  }

  private void msgCommand(Player sender, Player recipient, String msg){
    dmSender.sendMsg(sender, recipient, msg);

    //Update lastReceived table to make /r work
    lastReceived.put(recipient, sender.getName());
  }

  private void rCommand(Player sender, String msg){
    //Get the recipient from lastReceived
    String recipientStr = lastReceived.get(sender);
    if(recipientStr == null){
      sender.sendMessage("Couldn't find a player that could be right :(");
      return;
    }
    Player recipient = Bukkit.getPlayer(recipientStr);

    //Stop here if the recipient isn't valid
    if(recipient == null) return;

    //Send the message
    msgCommand(sender, recipient, msg);
  }

}

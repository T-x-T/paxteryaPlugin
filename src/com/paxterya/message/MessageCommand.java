package com.paxterya.message;

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

  Map<Player, String> lastReceived;

  public MessageCommand(JavaPlugin plugin){
    this.plugin = plugin;
    this.lastReceived = new HashMap<>();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if(label.equals("msg") || label.equals("w") || label.equals("dm")){
      //Check if message was provided
      if(args.length < 2){
        sender.sendMessage("Please provide the name of another player and a message");
        return false;
      }

      //Get recipient
      Player recipient = Bukkit.getPlayer(args[0]);

      //Get message
      String message = "";
      for(int i = 1; i < args.length; i++){
        message += args[i];
        message += " ";
      }

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

      //Get message
      String message = "";
      for(int i = 0; i < args.length; i++){
        message += args[i];
        message += " ";
      }

      rCommand(sender, message);
      return true;
    }

    return true;
  }

  private void msgCommand(CommandSender sender, Player recipient, String msg){
    String finalMsg = "";

    //Build the string to send to the recipient
    finalMsg += "[§5DM§r] §9§l";
    finalMsg += sender.getName();
    finalMsg += "§7§o whispered: §r";
    finalMsg += msg;

    //Send the message to the recipient
    recipient.sendMessage(finalMsg);

    //Also send verification to sender
    finalMsg = "";
    finalMsg += "[§5DM§r]";
    finalMsg += "§7§o you whispered to §9§l";
    finalMsg += recipient.getName();
    finalMsg += "§r: ";
    finalMsg += msg;

    sender.sendMessage(finalMsg);

    //Update lastReceived table to make /r work
    lastReceived.put(recipient, sender.getName());
  }

  private void rCommand(CommandSender sender, String msg){
    //Get the recipient from lastReceived
    String recipientStr = lastReceived.get(Bukkit.getPlayer(sender.getName()));
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

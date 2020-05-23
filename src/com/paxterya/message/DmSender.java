package com.paxterya.message;

import org.bukkit.entity.Player;

public class DmSender {

  private final boolean sendConfirmation;
  private final boolean isGroupDm;

  public DmSender(boolean sendConfirmation, boolean isGroupDm){
    this.sendConfirmation = sendConfirmation;
    this.isGroupDm = isGroupDm;
  }

  public void sendMsg(Player sender, Player recipient, String msg){
    String finalMsg = "";

    //Build the string to send to the recipient
    if(this.isGroupDm){
      finalMsg += "[§5GDM§r] §9§l";
    }else{
      finalMsg += "[§5DM§r] §9§l";
    }
    finalMsg += sender.getName();
    finalMsg += "§7§o whispered: §r";
    finalMsg += msg;

    //Send the message to the recipient
    recipient.sendMessage(finalMsg);

    if(!this.sendConfirmation) return;

    //Also send verification to sender
    finalMsg = "";
    if(this.isGroupDm){
      finalMsg += "[§5GDM§r] §9§l";
    }else{
      finalMsg += "[§5DM§r] §9§l";
    }
    finalMsg += "§7§o you whispered to §9§l";
    finalMsg += recipient.getName();
    finalMsg += "§r: ";
    finalMsg += msg;

    sender.sendMessage(finalMsg);
  }
}

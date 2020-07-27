package com.paxterya.message;

import com.paxterya.chatWordReplacer.WordReplacer;
import net.md_5.bungee.api.chat.*;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMessageCommand  implements CommandExecutor {

  JavaPlugin plugin;

  private DmSender dmSender;
  private WordReplacer wordReplacer;
  //The Integer is the ID of the groupdm and the player is the creator
  Map<Integer, Player> groupDms;
  //Maps which players have been invited to which groupdm, only stores the last invite that hasnt been acted on
  Map<Player, Integer> invites;
  //Maps which groupdm ID a specific player is in right now
  Map<Player, Integer> participants;

  private boolean shouldReplaceWords;
  public GroupMessageCommand(JavaPlugin plugin, WordReplacer wordReplacer){
    this.plugin = plugin;

    this.dmSender = new DmSender(false, true);
    this.wordReplacer = wordReplacer;

    this.groupDms = new HashMap<>();
    this.invites = new HashMap<>();
    this.participants = new HashMap<>();
    this.shouldReplaceWords = plugin.getConfig().getBoolean("replace_words_in.dm");
  }

  public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args){

    //Check if arguments are supplied
    if(args.length < 1){
      return false;
    }
    Player sender = Bukkit.getPlayer(commandSender.getName());

    //Check if its the send message command
    if(label.equalsIgnoreCase("gmsg") || label.equalsIgnoreCase("greply") || label.equalsIgnoreCase("gr") || label.equalsIgnoreCase("gm")){
      //create the message by joining the arguments
      String message = StringUtils.join(args," ");
      if (shouldReplaceWords) message = wordReplacer.replaceWords(message, sender);
      sendGroupDM(sender, message);
      return true;
    }

    //Sub-command accept
    if(args[0].equalsIgnoreCase("accept")){
      acceptInvite(sender);
      return true;
    }

    //Sub-command deny
    if(args[0].equalsIgnoreCase("deny")){
      denyInvite(sender);
      return true;
    }

    //Sub-command leave
    if(args[0].equalsIgnoreCase("leave")){
      leaveGroupDm(sender);
      return true;
    }

    //Sub-command list
    if(args[0].equalsIgnoreCase("list")){
      listParticipants(sender);
      return true;
    }



    //Seems like sender wants to create a new group dm; Go through args to find player names
    List<Player> playerList = new ArrayList<Player>();
    for (String arg : args) {
      if (Bukkit.getPlayer(arg) != null) {
        playerList.add(Bukkit.getPlayer(arg));
      }
    }

    //Check if we found players
    if(playerList.size() > 0){
      createGroupDM(Bukkit.getPlayer(sender.getName()), playerList.toArray(new Player[0]));
      return true;
    }

    //If we got here, the sender probably didn't supply correct arguments
    return false;
  }





  private void createGroupDM(Player creator, Player[] invitedPlayers){
    //Check if player already is in group DM
    if(participants.containsKey(creator) && !groupDms.containsValue(creator)){
      creator.sendMessage("§4You are already in a group DM that you didn't create!§r Please use §l/gdm leave§r before creating your own");
      return;
    }

    //Create ID for group DM
    int gdmID = groupDms.size() + 1;

    //Send invitations the invitedPlayers
    StringBuilder playerStr = new StringBuilder();
    for (Player invitedPlayer : invitedPlayers) {
      createInvite(creator, invitedPlayer, gdmID);
      playerStr.append(invitedPlayer.getName());
      playerStr.append("§r, §9§l");
    }

    String strToSend = "§b";
    if(groupDms.containsValue(creator)){
      strToSend += "Invited new players to group DM";
    }else{
      strToSend += "Created a new group DM and invited players: ";
    }
    strToSend += ": §9§l" + playerStr;
    strToSend = strToSend.substring(0, strToSend.length() - 8);

    creator.sendMessage(strToSend);

    //Save the group DM if it doesnt exist already
    if(!groupDms.containsValue(creator)){
      groupDms.put(gdmID, creator);
      participants.put(creator, gdmID);
    }
  }

  private void createInvite(Player creator, Player invitedPlayer, int gdmID){
    invites.put(invitedPlayer, gdmID);

    invitedPlayer.spigot().sendMessage(new ComponentBuilder("§6You got invited into a group DM from §9§l" + creator.getDisplayName() + "§r! ")
            .append(new TextComponent("§a§l[Accept] "))
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Accept the invite")}))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gdm accept"))

            .append(new TextComponent("§c§l[Deny] "))
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Deny the invite")}))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gdm deny"))
            .create()
    );
  }

  private void acceptInvite(Player player){
    if(!hasOpenInvite(player)){
      player.sendMessage("§4You have no open Invites");
      return;
    }
    participants.put(player, invites.get(player));
    player.sendMessage("§bAccepted invite, welcome in the group DM");
    sendGroupDM(player, "I just joined \\o");
  }

  private void denyInvite(Player player){
    if(!hasOpenInvite(player)){
      player.sendMessage("§4You have no open Invites");
      return;
    }
    invites.remove(player);
    player.sendMessage("§bYou just denied the invite :(");
  }

  private boolean hasOpenInvite(Player player){
    return invites.containsKey(player);
  }

  private void leaveGroupDm(Player player){
    participants.remove(player);
    player.sendMessage("§bYou left the group dm");
  }

  private void listParticipants(Player sender){
    //Stop if sender isn't in group dm
    if(!participants.containsKey(sender)){
      sender.sendMessage("§4This command only makes sense when you are in a group DM!");
      return;
    }

    StringBuilder msg = new StringBuilder();

    msg.append("§bThe following players are in the same group DM as you: §9§l");

    List<Player> recipientList = new ArrayList<Player>();
    participants.forEach((player, id) -> {
      if(id.equals(participants.get(sender))){
        msg.append(player.getName());
        msg.append(" ");
      }
    });

    sender.sendMessage(msg.toString());
  }

  private void sendGroupDM(Player sender, String msg){
    //Check if sender is in group dm
    if(!participants.containsKey(sender)){
      sender.sendMessage("§4You are not in a group dm!§r Check §l/help gdm");
      return;
    }

    //Get an array of players to send the message to
    List<Player> recipientList = new ArrayList<Player>();
    participants.forEach((player, id) -> {
      if(id.equals(participants.get(sender))) recipientList.add(player);
    });
    Player[] recipients = recipientList.toArray(new Player[0]);

    for(Player recipient : recipients){
      if(recipient != null) dmSender.sendMsg(sender, recipient, msg);
    }
  }
}

package com.paxterya.chatWordReplacer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class ChatWordReplacer implements Listener {
    private Map<String, Object> replacerMap;
    private JavaPlugin plugin;

    public ChatWordReplacer(JavaPlugin plugin) {
        this.plugin = plugin;
        this.replacerMap = (Map<String, Object>) plugin.getConfig().getList("replacer_rules").get(0);
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        //F a s t
        String newMessage = "";
        boolean edited = false;
        int pos = 0, end;
        String sub;
        String msg = event.getMessage();

        while ((end = msg.indexOf(' ', pos)) >= 0) {
            sub = msg.substring(pos, end);
            if (replacerMap.containsKey(sub)) {
                newMessage += getFormattedReplacement((String) replacerMap.get(sub), event.getPlayer());
                edited = true;
            } else {
                newMessage += sub + " ";
            }
            pos = end + 1;
        }
        sub = msg.substring(pos);
        if (replacerMap.containsKey(sub)) {
            newMessage += getFormattedReplacement((String) replacerMap.get(sub), event.getPlayer());
            edited = true;
        } else {
            newMessage += sub + " ";
        }
        if (edited) {
            event.setMessage(newMessage);
        }
    }

    private String getFormattedReplacement(String value, Player player) {
        switch (value) {
            case "PLAYER_COORDS": return PlayerInfo.coordsAsString(player) + " ";
            case "PLAYER_FULLCOORDS": return  PlayerInfo.fullCoordsAsString(player) + " ";
            case "PLAYER_TOOL": return PlayerInfo.heldToolAsString(player) + " ";
            case "PLAYER_DIAMONDS": return PlayerInfo.enderChestDiamondsAsString(player) + " ";
            case "PLAYER_SPAWN": return PlayerInfo.spawnPointAsString(player) + " ";
            default: return value + " ";
        }
    }
}

package com.paxterya.chatWordReplacer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatWordReplacer implements Listener {
    private Map<String, Object> replacerMap;
    private JavaPlugin plugin;

    public ChatWordReplacer(JavaPlugin plugin) {
        this.plugin = plugin;
        ConfigurationSection c = plugin.getConfig().getConfigurationSection("roles");
        this.replacerMap = (Map<String, Object>) plugin.getConfig().getList("replacer_rules").get(0);
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        StringBuilder newMessageBuilder = new StringBuilder();
        //edited is atomic because the stream could in theory be executed in parallel
        AtomicBoolean edited = new AtomicBoolean(false);
        Arrays.stream(event.getMessage().split("\\W+")).forEach(word -> {
            //Process each word in the message. Since there can be multiple keywords in the message, each word has to be checked.
            if (replacerMap.containsKey(word)) {
                newMessageBuilder.append(getFormattedReplacement((String) replacerMap.get(word), event.getPlayer()));
                edited.set(true);
            } else {
                newMessageBuilder.append(word + " ");
            }
        });
        if (edited.get()) {
            //if there is something to edit, change the message
            event.setMessage(newMessageBuilder.toString());
        }
    }

    private String getFormattedReplacement(String value, Player player) {
        switch (value) {
            case "PLAYER_COORDS": return PlayerInfo.coordsAsString(player);
            case "PLAYER_FULLCOORDS": return PlayerInfo.fullCoordsAsString(player);
            case "PLAYER_TOOL": return PlayerInfo.heldToolAsString(player);
            case "PLAYER_DIAMONDS": return PlayerInfo.enderChestDiamondsAsString(player);
            default: return value + " ";
        }
    }
}

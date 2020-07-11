package com.paxterya.wordReplacer;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class WordReplacer {
    private Map<String, Object> replacerMap;

    public WordReplacer(JavaPlugin plugin) {
        ConfigurationSection c = plugin.getConfig().getConfigurationSection("roles");
        this.replacerMap = (Map<String, Object>) plugin.getConfig().getList("replacer_rules").get(0);
    }

    public Listener getListener() {
        return new Listener() {

            @EventHandler
            public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
                StringBuilder newMessageBuilder = new StringBuilder();
                AtomicBoolean edited = new AtomicBoolean(false);
                Arrays.stream(event.getMessage().split("\\W+")).forEach(word -> {
                    if (replacerMap.containsKey(word)) {
                        newMessageBuilder.append(getValue((String) replacerMap.get(word), event.getPlayer()));
                        edited.set(true);
                    } else {
                        newMessageBuilder.append(word + " ");
                    }
                });
                if (edited.get()) {
                    event.setMessage(newMessageBuilder.toString());
                }
            }
        };
    }

    private String getValue(String value, Player player) {
        switch (value) {
            case "PLAYER_COORDS": return String.format(
                                            "%s%s %s%s ",
                                            ChatColor.LIGHT_PURPLE,
                                            (int) player.getLocation().getX(),
                                            (int) player.getLocation().getZ(),
                                            ChatColor.RESET
                                        );

            case "PLAYER_FULLCOORDS": return String.format(
                                                "%s%s %s %s (%s)%s ",
                                                ChatColor.LIGHT_PURPLE,
                                                (int) player.getLocation().getX(),
                                                (int) player.getLocation().getY(),
                                                (int) player.getLocation().getZ(),
                                                player.getWorld().getName(),
                                                ChatColor.RESET
                                            );
            default: return value + " ";
        }
    }
}

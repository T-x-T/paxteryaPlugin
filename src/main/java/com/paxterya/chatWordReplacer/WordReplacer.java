package com.paxterya.chatWordReplacer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Level;

public class WordReplacer {
    private Map<String, String> replacerMap;
    private boolean broadcast;

    public WordReplacer(JavaPlugin plugin) {
        this.replacerMap = (Map<String, String>) plugin.getConfig().getList("word_replacer_rules").get(0);
        this.broadcast = plugin.getConfig().getBoolean("word_replacer_broadcast");
        Bukkit.getLogger().info("[WordReplacer] Broadcast mode set to " + broadcast);
    }

    public Component replaceWords(TextComponent message, Player player) {
        if (replacerMap.containsKey(message.content())) {
            Component replacement = getFormattedReplacement(replacerMap.get(message.content()), player);
            if (broadcast) {
                Bukkit.getServer().broadcast(Component.text("<" + player.getName() + "> ").append(replacement));
                return null;
            }
            return replacement;
        }
        return message;
    }

    private Component getFormattedReplacement(String value, Player player) {
        return switch (value) {
            case "PLAYER_COORDS" -> PlayerInfo.coords(player);
            case "PLAYER_FULLCOORDS" -> PlayerInfo.fullCoords(player);
            case "PLAYER_TOOL" -> PlayerInfo.heldTool(player);
            case "PLAYER_DIAMONDS" -> PlayerInfo.enderChestDiamonds(player);
            case "PLAYER_SPAWN" -> PlayerInfo.spawnPoint(player);
            case "PLAYER_PLAYTIME" -> PlayerInfo.playTime(player);
            case "PLAYER_BIOME" -> PlayerInfo.biome(player);
            case "PLAYER_SPEED" -> PlayerInfo.speed(player);
            case "PLAYER_XP" -> PlayerInfo.xp(player);
            case "PLAYER_BASE" -> PlayerInfo.base(player);
            default -> Component.text(value);
        };
    }
}

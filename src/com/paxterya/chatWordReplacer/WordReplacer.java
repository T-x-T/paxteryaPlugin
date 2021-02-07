package com.paxterya.chatWordReplacer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.regex.Pattern;

public class WordReplacer {
    private Map<String, Object> replacerMap;
    private Pattern regex;

    public WordReplacer(JavaPlugin plugin) {
        this.regex = Pattern.compile("\\b");
        this.replacerMap = (Map<String, Object>) plugin.getConfig().getList("word_replacer_rules").get(0);
    }

    public String replaceWords(String message, Player player) {
        StringBuilder newMessageBuilder = new StringBuilder();
        boolean edited = false;
        for (String word : regex.split(message)) {
            if (replacerMap.containsKey(word)) {
                newMessageBuilder.append(getFormattedReplacement((String) replacerMap.get(word), player));
                edited = true;
            } else {
                newMessageBuilder.append(word);
            }
        }
        if (edited) {
            return newMessageBuilder.toString();
        } else {
            return message;
        }
    }

    private String getFormattedReplacement(String value, Player player) {
        switch (value) {
            case "PLAYER_COORDS": return PlayerInfo.coordsAsString(player);
            case "PLAYER_FULLCOORDS": return PlayerInfo.fullCoordsAsString(player);
            case "PLAYER_COOLCOORDS": return PlayerInfo.coolCoordsAsString(player);
            case "PLAYER_TOOL": return PlayerInfo.heldToolAsString(player);
            case "PLAYER_DIAMONDS": return PlayerInfo.enderChestDiamondsAsString(player);
            case "PLAYER_SPAWN": return PlayerInfo.spawnPointAsString(player);
            case "PLAYER_PLAYTIME": return PlayerInfo.playTimeAsString(player);
            case "PLAYER_BIOME": return PlayerInfo.biomeAsString(player);
            default: return value;
        }
    }
}

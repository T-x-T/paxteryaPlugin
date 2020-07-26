package com.paxterya.chatWordReplacer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatWordReplacer implements Listener {
    private WordReplacer replacer;
    public ChatWordReplacer(JavaPlugin plugin) {
        replacer = new WordReplacer(plugin);
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        event.setMessage(
                replacer.replaceWords(event.getMessage(), event.getPlayer())
        );
    }
}

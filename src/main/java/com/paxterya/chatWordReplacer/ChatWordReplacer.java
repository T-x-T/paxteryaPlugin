package com.paxterya.chatWordReplacer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatWordReplacer implements Listener {
    private WordReplacer wordReplacer;
    private boolean shouldReplaceWords;
    public ChatWordReplacer(JavaPlugin plugin, WordReplacer wordReplacer) {
        this.wordReplacer = wordReplacer;
        this.shouldReplaceWords = plugin.getConfig().getBoolean("replace_words_in.global");
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (shouldReplaceWords)
            event.setMessage( wordReplacer.replaceWords(event.getMessage(), event.getPlayer()) );
    }
}

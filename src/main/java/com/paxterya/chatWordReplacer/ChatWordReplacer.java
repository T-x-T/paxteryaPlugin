package com.paxterya.chatWordReplacer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatWordReplacer implements Listener {
    private WordReplacer wordReplacer;
    private boolean shouldReplaceWords;
    public ChatWordReplacer(JavaPlugin plugin, WordReplacer wordReplacer) {
        this.wordReplacer = wordReplacer;
        this.shouldReplaceWords = plugin.getConfig().getBoolean("replace_words_in.global");
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncChatEvent event) {
        if (shouldReplaceWords) {
            String content = ((TextComponent) event.message()).content();
            event.message(Component.text(wordReplacer.replaceWords(content, event.getPlayer())));
        }
    }
}

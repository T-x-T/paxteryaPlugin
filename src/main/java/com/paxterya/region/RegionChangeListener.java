package com.paxterya.region;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionChangeListener implements Listener {

    private static final TextColor msgColor = TextColor.fromCSSHexString("#919191");
    private static final String defaultNameColor = "ffffff";

    @EventHandler
    public void onRegionChange(RegionChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getNewRegion() == null) {
            player.sendMessage(Component.text()
                    .append(Component.text("Leaving ").color(msgColor).decorate(TextDecoration.ITALIC))
                    .append(Component.text()
                            .content(event.getPreviousRegion().getName())
                            .color(TextColor.fromCSSHexString("#" + event.getPreviousRegion().getOrDefault("chatcolor", defaultNameColor)))
                            .decorate(TextDecoration.ITALIC)));
        } else {
            player.sendMessage(Component.text()
                    .append(Component.text("Entering ").color(msgColor).decorate(TextDecoration.ITALIC))
                    .append(Component.text()
                            .content(event.getNewRegion().getName())
                            .color(TextColor.fromCSSHexString("#" + event.getNewRegion().getOrDefault("chatcolor", defaultNameColor)))
                            .decorate(TextDecoration.ITALIC)));
        }
    }
}

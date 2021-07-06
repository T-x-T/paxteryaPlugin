package com.paxterya.region;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionChangeListener implements Listener {

    @EventHandler
    public void onRegionChange(RegionChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getNewRegion() == null) {
            player.sendMessage(Component.text()
                    .append(Component.text("Leaving ").decorate(TextDecoration.ITALIC))
                    .append(Component.text()
                            .content(event.getPreviousRegion().getName())
                            .color(event.getPreviousRegion().getColor())
                            .decorate(TextDecoration.ITALIC)));
        } else {
            player.sendMessage(Component.text()
                    .append(Component.text("Entering ").decorate(TextDecoration.ITALIC))
                    .append(Component.text()
                            .content(event.getNewRegion().getName())
                            .color(event.getNewRegion().getColor())
                            .decorate(TextDecoration.ITALIC)));
        }
    }
}

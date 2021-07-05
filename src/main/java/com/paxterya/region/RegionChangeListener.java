package com.paxterya.region;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionChangeListener implements Listener {

    @EventHandler
    public void onRegionChange(RegionChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getNewRegion() == null) {
            player.sendTitle("Leaving " + event.getPreviousRegion().getName(), "");
        } else {
            player.sendTitle("Entering " + event.getNewRegion().getName(), "");
        }
    }

}

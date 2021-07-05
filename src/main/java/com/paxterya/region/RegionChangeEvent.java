package com.paxterya.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class RegionChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Region previousRegion;
    private Region newRegion;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}

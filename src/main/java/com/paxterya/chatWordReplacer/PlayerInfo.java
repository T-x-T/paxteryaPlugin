package com.paxterya.chatWordReplacer;

import com.paxterya.base.Base;
import com.paxterya.base.BaseManager;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;

/**
 * This class contains static methods to generate information
 * about Players as Components.
 */
public class PlayerInfo {
    public static Component coords(Player player) {
        return navigableCoords(player.getLocation(), TextDecoration.ITALIC, false);
    }

    public static Component fullCoords(Player player) {
        return navigableCoords(player.getLocation(), TextDecoration.ITALIC, true);
    }

    public static Component biome(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        String biomeName = world.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ()).name();
        return Component.text(biomeName.toLowerCase().replace('_', ' ') + "§r").color(getDimensionColor(player.getLocation())).decorate(TextDecoration.BOLD);
    }

    public static TextColor getDimensionColor(Location location) {
        return switch (location.getWorld().getName()) {
            case "world" -> NamedTextColor.GREEN;
            case "world_nether" -> location.getY() < 128 ? NamedTextColor.RED : NamedTextColor.DARK_GRAY;
            case "world_the_end" -> NamedTextColor.LIGHT_PURPLE;
            default -> NamedTextColor.WHITE;
        };
    }

    public static String getDimensionName(String world) {
        switch(world) {
            case "world": return "Overworld";
            case "world_nether":return "Nether";
            case "world_the_end": return "The End";
            default: return "Unknown";
        }
    }

    public static Component getDimension(Location location) {
        return Component.text(getDimensionName(location.getWorld().getName())).color(getDimensionColor(location));
    }

    public static Component heldTool(Player player) {
        ItemStack itemStack = player.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return Component.text("§9air§r");
        String name = itemMeta.getDisplayName();
        String type = itemStack.getType().name();

        if (itemStack.getMaxStackSize() == 1) {
            StringBuilder enchants = new StringBuilder();
            if (itemMeta.hasEnchants()) {
                Iterator<Enchantment> iterator = itemMeta.getEnchants().keySet().iterator();
                Enchantment next = iterator.next();
                enchants.append(" [" + next.getName().toLowerCase().replace('_', ' ') + " " + itemMeta.getEnchants().get(next));
                while (iterator.hasNext()) {
                    next = iterator.next();
                    enchants.append(", " + next.getName().toLowerCase().replace('_', ' ') + " " + itemMeta.getEnchants().get(next));
                }
                enchants.append("]");
            }
            return Component.text("§9" + (name.isEmpty() ? type.toLowerCase().replace('_', ' ') : "§o" + name) +
                    "§r§d" + enchants.toString() + "§r");
        } else {
            return Component.text("§9" + (name.isEmpty() ? type.toLowerCase().replace('_', ' ') : "§o" + name) + " §3x " +
            itemStack.getAmount() + "§r");
        }

    }

    public static Component enderChestDiamonds(Player player) {
        int balance = 0;
        ItemStack[] itemStacks = player.getEnderChest().getContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || itemStack.getAmount() == 0) continue;
            if (itemStack.getType().equals(Material.DIAMOND)) {
                balance += itemStack.getAmount();
            } else if (itemStack.getType().equals(Material.DIAMOND_BLOCK)) {
                balance += 9 * itemStack.getAmount();
            }
        }
        return Component.text("§b" +  balance + " dia§r");
    }

    public static Component spawnPoint(Player player) {
        Location spawn = player.getBedSpawnLocation();
        if (spawn == null) {
            spawn = player.getWorld().getSpawnLocation();
        }
        return navigableCoords(spawn, TextDecoration.BOLD, true);
    }

    public static Component playTime(Player player) {
        int ticksPlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int minutesPlayed = ticksPlayed / 20 / 60;
        int hoursPlayed = minutesPlayed / 60;
        if (hoursPlayed < 1) return Component.text("§9" + minutesPlayed + " minutes§r");
        return Component.text("§9" + hoursPlayed + " hours§r");
    }

    public static Component speed(Player player) {
        return Component.text("§e" + (int) (player.getVelocity().length() * 20) + " §obps§r");
    }

    public static Component xp(Player player) {
        return Component.text("§elvl " + player.getLevel() + "§r");
    }

    public static Component base(Player player) {
        Base base = BaseManager.instance.getBaseOf(player);
        if (base == null) return spawnPoint(player);
        return navigableCoords(base.getLocation(), TextDecoration.UNDERLINED, false);
    }

    public static Component navigableCoords(Location location, TextDecoration decoration, boolean full) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String text = full ? x + " " + y + " " + z : x + " " + z;
        return Component.text(text)
                .hoverEvent(HoverEvent.showText(Component.text("Click for directions")))
                .clickEvent(ClickEvent.runCommand("/navigation " + x + " " + z + " " + location.getWorld().getName()))
                .color(getDimensionColor(location))
                .decorate(decoration);
    }
}

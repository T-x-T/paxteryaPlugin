package com.paxterya.chatWordReplacer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;

/**
 * This class contains static methods to generate information
 * about Players as pretty game-ready strings.
 */
public class PlayerInfo {
    public static String coordsAsString(Player player) {
        return  "§6"+(int) player.getLocation().getX()+" "+
                (int) player.getLocation().getZ()+"§r";
    }

    public static String fullCoordsAsString(Player player) {
        return "§6"+
                (int) player.getLocation().getX()+" "+
                (int) player.getLocation().getY()+" "+
                (int) player.getLocation().getZ()+" "+
                player.getWorld().getName()+"§r";
    }

    public static String coolCoordsAsString(Player player) {
        String color = getDimensionColor(player);
        return  color + "§o" + (int) player.getLocation().getX()+" "+
                (int) player.getLocation().getZ()+"§r";
    }

    public static String biomeAsString(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        String biomeName = world.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ()).name();
        return getDimensionColor(player) + "§l" + biomeName.toLowerCase().replace('_', ' ') + "§r";
    }

    private static String getDimensionColor(Player player) {
        switch(player.getWorld().getName()) {
            case "world": return "§a";
            case "world_nether":return player.getLocation().getY() < 128 ? "§c" : "§7";
            case "world_the_end": return "§d";
            default: return "";
        }
    }

    public static String heldToolAsString(Player player) {
        ItemStack itemStack = player.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return "§9air§r";
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
            return "§9" + (name.isEmpty() ? type.toLowerCase().replace('_', ' ') : "§o" + name) +
                    "§r§d" + enchants.toString() + "§r";
        } else {
            return "§9" + (name.isEmpty() ? type.toLowerCase().replace('_', ' ') : "§o" + name) + " §3x " +
            itemStack.getAmount() + "§r";
        }

    }

    public static String enderChestDiamondsAsString(Player player) {
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
        return "§b" +  balance + " dia§r";
    }

    public static String spawnPointAsString(Player player) {
        Location spawn = player.getBedSpawnLocation();
        if (spawn == null) {
            spawn = player.getWorld().getSpawnLocation();
        }
        return "§6§o" +  (int) spawn.getX() + " " + (int) spawn.getZ() + "§r";
    }

    public static String playTimeAsString(Player player) {
        int ticksPlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int minutesPlayed = ticksPlayed / 20 / 60;
        int hoursPlayed = minutesPlayed / 60;
        if (hoursPlayed < 1) return "§9" + minutesPlayed + " minutes§r";
        return "§9" + hoursPlayed + " hours§r";
    }

    public static String speedAsString(Player player) {
        return "§e" + (int) (player.getVelocity().length() * 20) + " §obps§r";
    }

    public static String xpAsString(Player player) {
        return "§elvl " + player.getLevel() + "§r";
    }
}
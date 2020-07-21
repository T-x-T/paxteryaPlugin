package com.paxterya.chatWordReplacer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;

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

    public static String heldToolAsString(Player player) {
        ItemStack itemStack = player.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return "§9air§r";
        String name = itemMeta.getDisplayName();
        String type = itemStack.getType().name();
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
        return "§9" +  (name.isEmpty() ? type.toLowerCase().replace('_', ' '): "§o" + name) +
                "§r§d" + enchants.toString() + "§r";

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
}

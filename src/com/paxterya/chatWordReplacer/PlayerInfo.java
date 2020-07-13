package com.paxterya.chatWordReplacer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;

public class PlayerInfo {
        public static String coordsAsString(Player player) {
        return String.format(
                "%s%s %s%s ",
                ChatColor.YELLOW,
                (int) player.getLocation().getX(),
                (int) player.getLocation().getZ(),
                ChatColor.RESET
        );
    }

    public static String fullCoordsAsString(Player player) {
        return String.format(
                "%s%s %s %s (%s)%s ",
                ChatColor.YELLOW,
                (int) player.getLocation().getX(),
                (int) player.getLocation().getY(),
                (int) player.getLocation().getZ(),
                player.getWorld().getName(),
                ChatColor.RESET
        );
    }

    public static String heldToolAsString(Player player) {
        ItemStack itemStack = player.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return String.format(
                    "%s%s%s",
                    ChatColor.LIGHT_PURPLE,
                    "air",
                    ChatColor.RESET
            );
        String name = itemMeta.getDisplayName();
        String type = itemStack.getType().name();
        StringBuilder enchants = new StringBuilder();
        if (itemMeta.hasEnchants()) {
            Iterator<Enchantment> iterator = itemMeta.getEnchants().keySet().iterator();
            Enchantment next = iterator.next();
            enchants.append("[" + next.getName().toLowerCase().replace('_', ' ') + " " + itemMeta.getEnchants().get(next));
            while (iterator.hasNext()) {
                next = iterator.next();
                enchants.append(", " + next.getName().toLowerCase().replace('_', ' ') + " " + itemMeta.getEnchants().get(next));
            }
            enchants.append("]");
        }
        return String.format(
                "%s%s%s %s%s%s ",
                ChatColor.BLUE,
                name.isEmpty() ? type.toLowerCase().replace('_', ' '): ChatColor.ITALIC + name,
                ChatColor.RESET,
                ChatColor.LIGHT_PURPLE,
                enchants.toString(),
                ChatColor.RESET
        );
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
        return String.format(
                "%s%s %s%s ",
                ChatColor.AQUA,
                balance,
                "dia",
                ChatColor.RESET
        );
    }
}

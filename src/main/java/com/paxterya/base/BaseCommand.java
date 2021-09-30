package com.paxterya.base;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor
public class BaseCommand implements CommandExecutor {

    private BaseManager manager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return false;
        Player player = Bukkit.getPlayer(sender.getName());
        if (player == null) return false;
        switch (args[0]) {
            case "set" -> {
                boolean hadOldBase = manager.getBaseOf(player) != null;
                manager.setBase(player);
                String baseName = manager.getBaseOf(player).getName();
                player.sendMessage(Component.text("[Paxterya] Success! Your new base is called '" + baseName + "'"));
                if (hadOldBase) player.sendMessage(Component.text("[Paxterya] Your old base marker will be removed if it had no other habitats"));
            }
            case "unset" -> {
                Base base = manager.getBaseOf(player);
                if (base == null) {
                    player.sendMessage(Component.text("[Paxterya] You do not have a base, so there is nothing to unset"));
                    return true;
                }
                String baseName = base.getName();
                manager.unsetBase(player);
                player.sendMessage(Component.text("[Paxterya] Success! You have left '" + baseName + "'. If you were the only habitat, the marker will be removed."));
            }
            default -> { return false; }
        }
        return true;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {

        private final List<String> options = List.of("set", "unset");

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            return options;
        }
    }
}

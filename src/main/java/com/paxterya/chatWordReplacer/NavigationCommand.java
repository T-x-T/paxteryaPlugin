package com.paxterya.chatWordReplacer;

import com.paxterya.base.BaseCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NavigationCommand implements CommandExecutor {


    static char N = (char) 0x2191;
    static char NE = (char) 0x2197;
    static char E = (char) 0x2192;
    static char SE = (char) 0x2198;
    static char S = (char) 0x2193;
    static char SW = (char) 0x2199;
    static char W = (char) 0x2190;
    static char NW = (char) 0x2196;
    static char OTHER = (char) 0x1111;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3 && args.length != 2) {
            return false;
        }

        int x, z;
        try {
            x = Integer.parseInt(args[0]);
            z = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        Player player = Bukkit.getPlayer(sender.getName());
        if (player == null) return false;

        World world = null;
        if (args.length == 3)
            world = Bukkit.getWorld(args[2]);
        if (world == null) {
            world = player.getWorld();
        }

        Location start = player.getLocation();
        Location destination = new Location(world, x, start.getY(), z);
        if (!world.equals(start.getWorld())) {
            player.sendMessage(PlayerInfo.getDimension(destination));
            return true;
        }
        Vector separation = destination.toVector().subtract(start.toVector());
        Vector topDirection = start.getDirection();
        topDirection = new Vector(topDirection.getX(), 0, topDirection.getZ()).normalize();
        double angle = angle(topDirection, separation.normalize());

        int distance = (int) start.distance(destination);
        Component navigation = Component.text(getArrow(angle) + " " + distance + " blocks").color(PlayerInfo.getDimensionColor(destination));
        player.sendMessage(navigation);
        return true;
    }

    private char getArrow(double angle) {
        if (angle > 157.5) {
            return S;
        } if (angle > 112.5) {
            return SW;
        } if (angle > 67.5) {
            return W;
        } if (angle > 22.5) {
            return NW;
        } if (angle > -22.5) {
            return N;
        } if (angle > -67.5) {
            return NE;
        } if (angle > -112.5) {
            return E;
        } if (angle > -157.5) {
            return SE;
        } else return S;
    }

    private static double angle(Vector v1, Vector v2) {
        return Math.toDegrees(v1.angle(v2)) * Math.signum(v1.crossProduct(v2).getY());
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {

        private final List<String> empty = List.of("");

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            return empty;
        }
    }
}

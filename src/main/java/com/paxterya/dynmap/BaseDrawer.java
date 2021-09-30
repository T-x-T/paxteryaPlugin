package com.paxterya.dynmap;

import com.paxterya.base.Base;
import com.paxterya.region.RegionType;
import com.paxterya.util.Circle;
import com.paxterya.util.Point2D;
import com.paxterya.util.Polygon;
import com.paxterya.util.Rectangle;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BaseDrawer {
    /**
     * Starts the dynmap base draw process after a specified delay
     * @param plugin PaxteryaPlugin
     * @param bases The top-level regions
     * @param delay The delay in ticks
     */
    public static void drawBasesLater(Plugin plugin, List<Base> bases, int delay) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> drawBases(bases), delay);
    }


    /**
     * Checks if dynmap is available and then draws the bases.
     * @param bases to draw
     */
    private static void drawBases(List<Base> bases) {
        Bukkit.getLogger().info("[paxterya] Starting dynmap bases drawing");
        Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        if (dynmap == null) {
            Bukkit.getLogger().severe("Cannot draw bases to dynmap: dynmap plugin is not loaded");
            return;
        }

        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("dynmap")) {
            Bukkit.getPluginManager().enablePlugin(dynmap);
        }

        PluginCommand markerCommand = Bukkit.getPluginCommand("dmarker");
        if (markerCommand == null) {
            Bukkit.getLogger().severe("Cannot draw bases to dynmap: dmarker command not found");
            return;
        }

        CommandSender cs = Bukkit.getConsoleSender();

        markerCommand.execute(cs, "dmarker", new String[]{"deleteset", "bases"});
        markerCommand.execute(cs, "dmarker", new String[]{"addset", "bases"});

        bases.forEach(base -> drawBase(base, markerCommand, cs));

        Bukkit.getLogger().info("[paxterya] dynmap base drawing complete");
    }


    /**
     * Draws a base marker to dynmap
     * @param base The base to draw
     * @param markerCommand The dmarker command
     * @param cs The console command sender
     */
    private static void drawBase(Base base, PluginCommand markerCommand, CommandSender cs) {
        Bukkit.getLogger().info("Drawing '" + base.getName() + "'");

        String[] args = new String[]{"add",
                "x:" + base.getLocation().getBlockX(),
                "y:" + base.getLocation().getBlockY(),
                "z:" + base.getLocation().getBlockZ(),
                "world:" + base.getLocation().getWorld().getName(),
                "id:" + base.getId(),
                "label:\"" + base.getName() + "\"",
                "set:bases", "icon:tower"};

        Bukkit.getLogger().info(StringUtils.join(args, " "));
        boolean success = markerCommand.execute(cs, "dmarker", args);

        if (!success) Bukkit.getLogger().warning("Failed to draw '" + base.getName() + "'");
    }
}

package com.paxterya.dynmap;

import com.paxterya.region.Region;
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
import java.util.logging.Level;

/**
 * Contains the static dynmap region draw method
 */
public class RegionDrawer {


    /**
     * Starts the dynmap region draw process after a specified delay
     * @param plugin PaxteryaPlugin
     * @param regions The top-level regions
     * @param delay The delay in ticks
     */
    public static void drawRegionsLater(Plugin plugin, List<Region> regions, int delay) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> drawRegions(regions), delay);
    }


    /**
     * Checks if dynmap is available and then draws the regions.
     * @param regions to draw
     */
    private static void drawRegions(List<Region> regions) {
        Bukkit.getLogger().info("[paxterya] Starting dynmap region drawing");
        Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        if (dynmap == null) {
            Bukkit.getLogger().severe("Cannot draw regions to dynmap: dynmap plugin is not loaded");
            return;
        }

        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("dynmap")) {
            Bukkit.getPluginManager().enablePlugin(dynmap);
        }

        PluginCommand markerCommand = Bukkit.getPluginCommand("dmarker");
        if (markerCommand == null) {
            Bukkit.getLogger().severe("Cannot draw regions to dynmap: dmarker command not found");
            return;
        }

        CommandSender cs = Bukkit.getConsoleSender();

        markerCommand.execute(cs, "dmarker", new String[]{"deleteset", "regions"});
        markerCommand.execute(cs, "dmarker", new String[]{"addset", "regions"});

        regions.forEach(region -> drawRegion(region, 64, markerCommand, cs));

        Bukkit.getLogger().info("[paxterya] dynmap region drawing complete");
    }


    /**
     * Draws a region to dynmap if 'draw' not set to 'false', and then recursively draws its subregions.
     * @param region The region to draw
     * @param regionY The y level at which to draw it. We want this to be bigger for subregions, so they get drawn on top
     * @param markerCommand The dmarker command
     * @param cs The console command sender
     */
    private static void drawRegion(Region region, int regionY, PluginCommand markerCommand, CommandSender cs) {
        if (region.getOrDefault("draw", "true").equalsIgnoreCase("true")) {
            Bukkit.getLogger().fine("Drawing '" + region.getId() + "'");
            if (region.getType() == RegionType.POLYGON || region.getType() == RegionType.RECTANGLE) {
                // dmap area
                List<Point2D> corners = region.getType() == RegionType.POLYGON ? ((Polygon) region.getArea()).getCorners() : ((Rectangle) region.getArea()).getCorners();
                corners.forEach(p -> {
                    String[] args = new String[]{"addcorner", p.getX() + "", regionY + "", p.getY() + "", region.getDimension()};
                    markerCommand.execute(cs, "dmarker", args);
                });

                List<String> args = new ArrayList<>(Arrays.asList("addarea", "id:" + region.getId(), "label:\"" + region.getName() + "\"", "set:regions"));

                for (Map.Entry<String, String> e : region.getArgs().entrySet()) {
                    args.add(e.getKey() + ":" + e.getValue());
                }

                Bukkit.getLogger().finer(StringUtils.join(args, " "));
                markerCommand.execute(cs, "dmarker", args.toArray(new String[0]));
            } else {
                // dmap circle
                Point2D center = ((Circle) region.getArea()).getCenter();
                double radius = ((Circle) region.getArea()).getRadius();

                List<String> args = new ArrayList<>(Arrays.asList(
                        "addcircle", "x:" + center.getX(), "y:" + regionY, "z:" + center.getY(), "world:" + region.getDimension(), "radiusx:" + radius, "radiusz:" + radius,
                        "id:" + region.getId(), "label:\"" + region.getName() + "\"", "set:regions"));

                for (Map.Entry<String, String> e : region.getArgs().entrySet()) {
                    args.add(e.getKey() + ":" + e.getValue());
                }

                Bukkit.getLogger().finer(StringUtils.join(args, " "));
                markerCommand.execute(cs, "dmarker", args.toArray(new String[0]));
            }
        }
        // draw subregions recursively
        region.getSubRegions().forEach(r -> drawRegion(r, regionY + 1, markerCommand, cs));
    }
}

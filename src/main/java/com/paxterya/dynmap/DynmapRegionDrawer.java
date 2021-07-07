package com.paxterya.dynmap;

import com.paxterya.region.Region;
import com.paxterya.region.RegionType;
import com.paxterya.util.Circle;
import com.paxterya.util.Point2D;
import com.paxterya.util.Polygon;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class DynmapRegionDrawer {

    public static void drawRegionsLater(Plugin plugin, List<Region> regions, int delay) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> drawRegions(regions), delay);
    }

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

        regions.forEach(region -> {
            if (region.getType() == RegionType.POLYGON) {
                // dmap area
                List<Point2D> corners = ((Polygon) region.getArea()).getCorners();
                corners.forEach(p -> {
                    String[] args = new String[] {"addcorner", p.getX() + "", "64", p.getY() + "", region.getDimension()};
                    markerCommand.execute(cs, "dmarker", args);
                });
                String[] args = new String[] {
                        "addarea", "id:" + region.getId(), "label:\"" + region.getName() + "\"", "set:regions", "color:" + region.getColor().substring(1), "fillcolor:" + region.getColor().substring(1)};
                Bukkit.getLogger().info(StringUtils.join(args, " "));
                markerCommand.execute(cs, "dmarker", args);
            } else {
                // dmap circle
                Point2D center = ((Circle) region.getArea()).getCenter();
                double radius = ((Circle) region.getArea()).getRadius();
                String[] args = new String[] {
                        "addcircle", "x:" + center.getX(), "y:64", "z:" + center.getY(), "world:" + region.getDimension(), "radiusx:" + radius, "radiusz:" + radius,
                        "id:" + region.getId(), "label:\"" + region.getName() + "\"", "set:regions", "color:" + region.getColor().substring(1), "fillcolor:" + region.getColor().substring(1)};
                Bukkit.getLogger().info(StringUtils.join(args, " "));
                markerCommand.execute(cs, "dmarker", args);
            }
        });

        Bukkit.getLogger().info("[paxterya] dynmap region drawing complete");
    }
}

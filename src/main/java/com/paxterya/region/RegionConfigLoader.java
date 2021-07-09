package com.paxterya.region;

import com.paxterya.util.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RegionConfigLoader {

    public static List<String> required = Arrays.asList("name", "dimension", "type", "corners", "radius", "center");

    public static List<Region> loadRegions(Plugin plugin) {
        Bukkit.getLogger().info("[paxterya] Loading regions from config");
        FileConfiguration config = loadConfigFile(plugin);
        List<Region> regions = new ArrayList<>();

        config.getKeys(false).forEach(key -> {
            Region region = loadRegion(config, key);
            if (region != null)
                regions.add(region);
        });

        // process subregions: if is a subregion, find the containing and add to its subregions
        regions.forEach(region -> {
            String containingRegion = region.getArgs().get("subregionof");
            if (containingRegion != null) {
                regions.stream().filter(containing -> containing.getId().equals(containingRegion)).forEach(containing -> containing.getSubRegions().add(region));
            }
        });
        // remove subregions from top-level regions list
        regions.removeIf(region -> region.getArgs().get("subregionof") != null);

        Bukkit.getLogger().info("[paxterya] Region loading complete");
        return regions;
    }


    private static Region loadRegion(FileConfiguration config, String key) {
        // required fields
        String name = config.getString(key + ".name");
        if (name == null) {
            Bukkit.getLogger().severe("Name for '" + key + "' not found");
            return null;
        }

        String dimension = config.getString(key + ".dimension");
        if (dimension == null) {
            Bukkit.getLogger().info("Dimension for '" + key + "' not found, defaults to 'world'");
            dimension = "world";
        }

        RegionType type;
        String typeString = config.getString(key + ".type");
        if (typeString == null) {
            Bukkit.getLogger().severe("Type for '" + key + "' not found");
            return null;
        } else if (typeString.equals("polygon")) {
            type = RegionType.POLYGON;
        } else if (typeString.equals("circle")) {
            type = RegionType.CIRCLE;
        } else if (typeString.equals("rectangle")) {
            type = RegionType.RECTANGLE;
        } else {
            Bukkit.getLogger().severe("Unknown type '"+ typeString +"' for '" + key + "'");
            return null;
        }

        Shape shape;
        if (type == RegionType.POLYGON) {
            shape = getPolygon(key, config);
        } else if (type == RegionType.CIRCLE) {
            shape = getCircle(key, config);
        } else { // type == Type.RECTANGLE
            shape = getRectangle(key, config);
        }

        // optional fields
        Map<String, String> optionalArgs = new HashMap<>();
        config.getConfigurationSection(key).getKeys(false).stream()
                .filter(k -> !required.contains(k))
                .forEach(k -> optionalArgs.put(k, config.getString(key + "." + k)));

        return new Region(key, name, dimension, type, shape, new ArrayList<>(), optionalArgs);
    }


    private static Polygon getPolygon(String key, FileConfiguration config) {
        List<Map<?, ?>> p = config.getMapList(key + ".corners");
        if (p.size() < 3) {
            Bukkit.getLogger().severe("Invalid number of corners for polygon '" + key + "', was " +p.size()+ ", should be at least 3");
            return null;
        }
        PolygonBuilder pb = new PolygonBuilder();
        for (Map<?, ?> point : p) {
            Integer x = (Integer) point.get("x");
            Integer z = (Integer) point.get("z");
            if (x == null || z == null) {
                Bukkit.getLogger().severe("A corner point for '" + key + "' is invalid");
                return null;
            }
            pb.addCorner(x, z);
        }
        return pb.build();
    }


    private static Circle getCircle(String key, FileConfiguration config) {
        double centerX = config.getDouble(key + ".center.x", 1e-9);
        double centerY = config.getDouble(key + ".center.y", 1e-9);
        if (centerX == 1e-9 || centerY == 1e-9) {
            Bukkit.getLogger().severe("Center point for '" + key + "' is invalid");
            return null;
        }
        double radius = config.getDouble(key + ".radius", 1e-9);
        if (radius == 1e-9) {
            Bukkit.getLogger().severe("Radius for '" + key + "' is invalid");
            return null;
        }
        return new Circle(radius, new Point2D(centerX, centerY));
    }


    private static Rectangle getRectangle(String key, FileConfiguration config) {
        List<Map<?, ?>> p = config.getMapList(key + ".corners");
        if (p.isEmpty()) {
            Bukkit.getLogger().severe("Corner points for '" + key + "' not found");
            return null;
        }
        if (p.size() != 2) {
            Bukkit.getLogger().severe("Invalid number of corners for rectangle '" + key + "', was " +p.size()+ ", should be 2");
            return null;
        }

        Integer x1 = (Integer) p.get(0).get("x");
        Integer z1 = (Integer) p.get(0).get("z");
        Integer x2 = (Integer) p.get(1).get("x");
        Integer z2 = (Integer) p.get(1).get("z");
        if (x1 == null || z1 == null || x2 == null || z2 == null) {
            Bukkit.getLogger().severe("A corner point for '" + key + "' is invalid");
            return null;
        }
        return new Rectangle(x1, z1, x2, z2);
    }


    private static FileConfiguration loadConfigFile(Plugin plugin) {
        File customConfigFile = new File(plugin.getDataFolder(), "regions.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource("regions.yml", false);
            Bukkit.getLogger().warning("[paxterya] Region config file not found, saving default");
        }

        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }
}

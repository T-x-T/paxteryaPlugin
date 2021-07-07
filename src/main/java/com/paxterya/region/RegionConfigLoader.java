package com.paxterya.region;

import com.paxterya.util.Circle;
import com.paxterya.util.Point2D;
import com.paxterya.util.PolygonBuilder;
import com.paxterya.util.Shape;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionConfigLoader {

    public static String[] optionals = new String[] {"color", "fillcolor", "weight", "opacity", "fillopacity"};

    public static List<Region> loadRegions(Plugin plugin) {
        Bukkit.getLogger().info("[paxterya] Loading regions from config");
        FileConfiguration config = loadConfigFile(plugin);
        List<Region> regions = new ArrayList<>();

        config.getKeys(false).forEach(key -> {
            Region region = loadRegion(config, key);
            if (region != null)
                regions.add(region);
        });
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
        } else {
            Bukkit.getLogger().severe("Unknown type '"+ typeString +"' for '" + key + "'");
            return null;
        }

        Shape shape;
        if (type == RegionType.POLYGON) {
            List<Map<?, ?>> p = config.getMapList(key + ".corners");
            if (p.isEmpty()) {
                Bukkit.getLogger().severe("Corner points for '" + key + "' not found");
                return null;
            }
            PolygonBuilder pb = new PolygonBuilder();
            for (Map<?, ?> point : p) {
                Integer x = (Integer) point.get("x");
                Integer z = (Integer) point.get("z");
                if (x == null || z == null) {
                    Bukkit.getLogger().severe("A corner point for '" + key + "' is invalid");
                    continue;
                }
                pb.addCorner(x, z);
            }
            shape = pb.build();

        } else { // type == Type.Circle
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
            shape = new Circle(radius, new Point2D(centerX, centerY));
        }

        // optional fields
        Map<String, String> meta = new HashMap<>();
        for (String fieldKey : optionals) {
            String value = config.getString(key + "." + fieldKey);
            if (value != null)
                meta.put(fieldKey, value);
        }

        return new Region(key, name, dimension, type, shape, meta);
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

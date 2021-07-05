package com.paxterya.region;

import com.paxterya.util.PolygonBuilder;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RegionConfigLoader {

    private Plugin plugin;

    public List<Region> loadRegions() {

        FileConfiguration config = loadConfigFile();

        List<Region> regions = new ArrayList<>();

        config.getKeys(false).forEach(key -> {
            Bukkit.getLogger().info("loading region '" + key + "'");
            String name = config.getString(key + ".name");
            if (name == null) Bukkit.getLogger().severe("Name for '" + key + "' not found");

            String hexColor = config.getString(key + ".color");
            if (hexColor == null) {
                Bukkit.getLogger().severe("Color for '" + key + "' not found");
                return;
            }
            TextColor color = TextColor.fromCSSHexString(hexColor);

            List<Map<?, ?>> p = config.getMapList(key + ".corners");
            if (p.isEmpty()) {
                Bukkit.getLogger().severe("Corner points for '" + key + "' not found");
                return;
            }

            PolygonBuilder pb = new PolygonBuilder();
            for (Map<?, ?> point : p) {
                Integer x = (Integer) point.get("x");
                Integer z =-(Integer) point.get("z");
                if (x == null || z == null) {
                    Bukkit.getLogger().severe("A corner point for '" + key + "' is invalid");
                    continue;
                }
                pb.addCorner(x, z);
            }

            regions.add(new Region(name, color, pb.build()));
        });

        return regions;
    }

    private FileConfiguration loadConfigFile() {
        File customConfigFile = new File(plugin.getDataFolder(), "regions.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource("regions.yml", false);
            Bukkit.getLogger().warning("Region config file not found, saving default");
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

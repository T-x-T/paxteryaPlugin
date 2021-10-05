package com.paxterya.base;

import com.paxterya.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BaseDataLoader {

    public static List<Base> loadBases(Plugin plugin) {
        Bukkit.getLogger().info("[paxterya] Loading bases from config");
        FileConfiguration config = loadDataFile(plugin);
        List<Base> bases = new ArrayList<>();

        config.getKeys(false).forEach(key -> {
            Base base = loadBase(config, key);
            if (base != null)
                bases.add(base);
        });

        Bukkit.getLogger().info("[paxterya] Base loading complete");
        return bases;
    }

    private static Base loadBase(FileConfiguration config, String key) {

        long id;
        try {
            id = Long.parseLong(key);
        } catch (NumberFormatException e) {
            Bukkit.getLogger().severe("Malformed id for '" + key + "' (must be an integer)");
            return null;
        }

        Location location = config.getLocation(key + ".location");
        if (location == null) {
            Bukkit.getLogger().severe("No location defined for base '" + key + "'");
            return null;
        }

        Map<UUID, String> owners = new HashMap<>();
        ConfigurationSection ownersData = config.getConfigurationSection(key + ".owners");
        if (ownersData == null) {
            Bukkit.getLogger().severe("No owners defined for base '" + key + "'");
            return null;
        }

        ownersData.getValues(false)
                .forEach((uuid, name) -> owners.put(UUID.fromString(uuid), (String) name));

        return new Base(location, owners, null, id);
    }

    public static void saveBases(Plugin plugin, List<Base> bases) {
        FileConfiguration config = new YamlConfiguration();
        bases.forEach(b -> saveBase(config, b));
        saveDataFile(plugin, config);
    }

    private static void saveBase(FileConfiguration config, Base base) {
        ConfigurationSection baseSection = config.createSection(String.valueOf(base.getId()));
        Location blockLocation = base.getLocation().toBlockLocation();
        blockLocation.setYaw(0f);
        blockLocation.setPitch(0f);
        baseSection.set("location", blockLocation);
        ConfigurationSection ownersSection = baseSection.createSection("owners");
        base.getOwners().forEach(((uuid, name) -> ownersSection.set(uuid.toString(), name)));
    }

    private static FileConfiguration loadDataFile(Plugin plugin) {
        File customConfigFile = new File(plugin.getDataFolder(), "bases.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource("bases.yml", false);
            Bukkit.getLogger().warning("[paxterya] Base data file not found, saving default");
        }

        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

    private static void saveDataFile(Plugin plugin, FileConfiguration config) {
        File customConfigFile = new File(plugin.getDataFolder(), "bases.yml");

        plugin.saveResource("bases.yml", true);
        Bukkit.getLogger().warning("[paxterya] Writing data file...");

        try {
            config.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

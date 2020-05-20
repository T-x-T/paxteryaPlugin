package com.paxterya.role;

import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class RoleUpdater implements Listener {

  JavaPlugin plugin;

  public RoleUpdater(JavaPlugin plugin){
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event){
    //Get uuid of player that just joined
    String uuid = event.getPlayer().getUniqueId().toString();

    //Convert to short form
    while(uuid.contains("-")) uuid = uuid.replace("-", "");

    //Run async
    String finalUuid = uuid;
    new BukkitRunnable(){

      @Override
      public void run() {
        //Everything in here is async; Make web request
        try {
          URL url = new URL("https://paxterya.com/api/roles?uuid=" + finalUuid);
          HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
          InputStream is = connection.getInputStream();
          InputStreamReader isr = new InputStreamReader(is);
          BufferedReader br = new BufferedReader(isr);

          String inputLine, res = "";
          while((inputLine = br.readLine()) != null){
            res += inputLine;
          }

          br.close();

          int role = Integer.parseInt(new JsonParser().parse(res).getAsJsonObject().get("role").toString());

          if(role >= 0 && role <= 9){
            //role seems kinda valid, execute update
            RoleManager roleManager = new RoleManager(plugin);

            roleManager.setRole(event.getPlayer(), role);
          }

        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }


      }
    }.runTaskAsynchronously(this.plugin);
  }

}

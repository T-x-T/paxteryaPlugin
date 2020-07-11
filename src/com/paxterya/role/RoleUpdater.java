package com.paxterya.role;

import com.google.gson.JsonParser;
import com.paxterya.paxteryaplugin.PaxteryaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class RoleUpdater implements Listener {

  PaxteryaPlugin plugin;
  RoleManager roleManager;

  public RoleUpdater(PaxteryaPlugin plugin){
    this.plugin = plugin;
    this.roleManager = new RoleManager(this.plugin);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event){
    new BukkitRunnable(){
      @Override
      public void run() {
        updateRole(event.getPlayer());
      }
    }.runTaskAsynchronously(this.plugin);
  }

  private void updateRole(Player player){
    String uuid = getUuidInShortForm(player);
    int role = getRoleFromApi(uuid);
    if(RoleTools.isRoleIdValid(role)){
      roleManager.setRole(player, role);
    }
  }

  private String getUuidInShortForm(Player player){
    String uuid = player.getUniqueId().toString();
    while(uuid.contains("-")){
      uuid = uuid.replace("-", "");
    }
    return uuid;
  }

  private int getRoleFromApi(String uuid) {
    try{
      URL url = getRequestUrl(uuid);
      assert url != null;
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      InputStream is = connection.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);

      String inputLine;
      StringBuilder res = new StringBuilder();
      while((inputLine = br.readLine()) != null){
        res.append(inputLine);
      }
      br.close();
      return Integer.parseInt(new JsonParser().parse(res.toString()).getAsJsonObject().get("role").toString());
    }catch (IOException | NullPointerException ignored){}
    return -1;
  }

  private URL getRequestUrl(String uuid){
    try {
      return new URL("https://paxterya.com/api/roles?uuid=" + uuid);
    } catch (MalformedURLException ignore) {} //Can be safely ignored, will never happen
    return null; //We will never get here
  }

}

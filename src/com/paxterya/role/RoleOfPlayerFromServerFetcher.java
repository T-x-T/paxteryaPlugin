package com.paxterya.role;

import com.google.gson.JsonParser;
import com.paxterya.paxteryaplugin.PaxteryaPlugin;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

class RoleOfPlayerFromServerFetcher {

  PaxteryaPlugin plugin;

  protected RoleOfPlayerFromServerFetcher(PaxteryaPlugin plugin){
    this.plugin = plugin;
  }

  protected static int getRoleIdForPlayer(Player player){
    String uuid = getUuidInShortForm(player);
    int role = getRoleFromApi(uuid);
    return role;
  }

  private static String getUuidInShortForm(Player player){
    String uuid = player.getUniqueId().toString();
    while(uuid.contains("-")){
      uuid = uuid.replace("-", "");
    }
    return uuid;
  }

  private static int getRoleFromApi(String uuid) {
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

  private static URL getRequestUrl(String uuid){
    try {
      return new URL("https://paxterya.com/api/roles?uuid=" + uuid);
    } catch (MalformedURLException ignore) {} //Can be safely ignored, will never happen
    return null; //We will never get here
  }

}

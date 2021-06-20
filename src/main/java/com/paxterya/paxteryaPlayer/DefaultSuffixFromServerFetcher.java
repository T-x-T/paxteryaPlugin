package com.paxterya.paxteryaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.entity.Player;

public class DefaultSuffixFromServerFetcher {
  protected static String getFromApi(Player player) {
    String uuid = getUuidInShortForm(player);
    
    try {
      URL url = new URL("https://paxterya.com/api/suffix?uuid=" + uuid);
      assert url != null;
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      InputStream is = connection.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);

      String inputLine;
      StringBuilder res = new StringBuilder();
      while((inputLine = br.readLine()) != null) {
        res.append(inputLine);
      }
      br.close();
      return res.toString().replace("{\"suffix\":\"", "").replace("\"}", "");
    }catch (IOException | NullPointerException ignored){
      return "";
    }
  }

  private static String getUuidInShortForm(Player player) {
    String uuid = player.getUniqueId().toString();
    while (uuid.contains("-")) {
      uuid = uuid.replace("-", "");
    }
    return uuid;
  }
}

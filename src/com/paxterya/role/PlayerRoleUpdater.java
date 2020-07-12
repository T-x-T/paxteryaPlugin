package com.paxterya.role;

import com.paxterya.paxteryaPlayer.PaxteryaPlayer;
import com.paxterya.paxteryaplugin.PaxteryaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRoleUpdater implements Listener {

  private final PaxteryaPlugin plugin;
  private final Map<Player, Role> playerRoleMappings;
  private final Roles allRoles;

  public PlayerRoleUpdater(PaxteryaPlugin plugin) {
    this.plugin = plugin;
    this.playerRoleMappings = new HashMap<>();
    this.allRoles = this.plugin.getRoles();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event){
    new BukkitRunnable(){
      @Override
      public void run() {
        int newRoleId = RoleOfPlayerFromServerFetcher.getRoleIdForPlayer(event.getPlayer());
        setRole(event.getPlayer(), newRoleId);
      }
    }.runTaskAsynchronously(this.plugin);
  }

  protected void setRole(Player player, int newRoleID){
    playerRoleMappings.put(player, allRoles.getRoleById(newRoleID));
    setPrefix(player);
    setPermissions(player);
  }

  private void setPrefix(Player player){
    String prefixToSet = playerRoleMappings.get(player).getPrefix();
    if(prefixToSet != null){
      new PaxteryaPlayer(plugin, player).setPrefix(prefixToSet);
    }
  }

  private void setPermissions(Player player){
    List<String> permissions = playerRoleMappings.get(player).getPermissions();
    PermissionAttachment attachment = player.addAttachment(plugin);
    permissions.forEach((permission) -> attachment.setPermission(permission, true));
    fixTabAutocompleteBug(player);
  }

  private void fixTabAutocompleteBug(Player player){
    player.recalculatePermissions();
    boolean opStatus = player.isOp();
    player.setOp(true);
    player.setOp(false);
    player.setOp(opStatus);
  }
}

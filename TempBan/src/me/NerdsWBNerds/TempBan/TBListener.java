package me.NerdsWBNerds.TempBan;

import static org.bukkit.ChatColor.*;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class TBListener implements Listener{
	public TempBan plugin;
	public TBListener(TempBan p){
		plugin = p;
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		String[] args = e.getMessage().split(" ");

		if(args[0].equalsIgnoreCase("/tempban") || args[0].equalsIgnoreCase("/tban") || args[0].equalsIgnoreCase("/tb")){
			e.setCancelled(true);
			
			if(!player.isOp()){
				tell(player, RED + "[TempBan] You don't have permission to do this.");
				return;
			}
			
			Player target = plugin.server.getPlayer(args[1]);
			if(target==null || !target.isOnline()){
				tell(player, RED + "[TempBan] Player could not be found!");
				return;
			}
			
			long endOfBan = System.currentTimeMillis() + parseTimeSpec(args[2], args[3]);
			getBanned().put(target.getName().toLowerCase(), endOfBan);
			
			target.kickPlayer("[TempBan] You are temp-banned for " + args[2] + " " + args[3] + "(s).");
			plugin.server.broadcastMessage(GOLD + "[TempBan] " + GREEN + "The player " + AQUA + target.getName() + GREEN + " is now banned for " + AQUA + args[2] + " " + args[3] + "(s).");
		}
		if(args[0].equalsIgnoreCase("/tempbanexact") || args[0].equalsIgnoreCase("/tbanexact") || args[0].equalsIgnoreCase("/tbe") || args[0].equalsIgnoreCase("/tbane") || args[0].equalsIgnoreCase("/tbexact")){
			e.setCancelled(true);
			
			if(!player.isOp()){
				tell(player, RED + "[TempBan] You don't have permission to do this.");
				return;
			}
			
			long endOfBan = System.currentTimeMillis() + parseTimeSpec(args[2], args[3]);
			getBanned().put(args[1].toLowerCase(), endOfBan);
			
			tell(player, GOLD + "[TempBan] " + GREEN + "The player " + AQUA + args[1].toLowerCase() + GREEN + " is now banned for " + AQUA + args[2] + " " + args[3] + "(s).");
		}
		if(args[0].equalsIgnoreCase("/tunban") || args[0].equalsIgnoreCase("/tempunban") || args[0].equalsIgnoreCase("/tu")){
			e.setCancelled(true);
			
			if(!player.isOp()){
				tell(player, RED + "[TempBan] You don't have permission to do this.");
				return;
			}
			
			if(!getBanned().containsKey(args[1].toLowerCase())){
				tell(player, RED + "[TempBan] Player could not be found!");
				return;
			}

			getBanned().remove(args[1].toLowerCase());
			tell(player, GOLD + "[TempBan] " + GREEN + "The player " + AQUA + args[1].toLowerCase() + GREEN + " is now unbanned.");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerLoginEvent e){
		Player player = e.getPlayer();
		
		if(getBanned().containsKey(player.getName().toLowerCase())){
			if(getBanned().get(player.getName().toLowerCase()) != null){
				long endOfBan = getBanned().get(player.getName().toLowerCase());
				long now = System.currentTimeMillis();
				long diff = endOfBan - now;
				
				if(diff<=0){
					getBanned().remove(player.getName().toLowerCase());
				}else{
					e.disallow(Result.KICK_OTHER, "[TempBan] You are temp-banned for " + diff/1000 + " more seconds.");
				}
			}
		}
	}

	static long parseTimeSpec(String time, String unit) {
		long sec;
		try{
			sec = Integer.parseInt(time)*60;
		}catch(NumberFormatException ex){
			return 0;
		}
		
		if (unit.startsWith("hour")){
			sec *= 60;
		}else if (unit.startsWith("day")){
			sec *= (60*24);
		}else if (unit.startsWith("week")){
			sec *= (7*60*24);
		}else if (unit.startsWith("month")){
			sec *= (30*60*24);
		}else if (unit.startsWith("min")){
			sec *= 1;
		}else if (unit.startsWith("sec")){
			sec /= 60;
		}

		return sec*1000;
	}
	
	public void tell(Player player, String m){
		player.sendMessage(m);
	}
	
	public HashMap<String, Long> getBanned(){
		return TempBan.Banned;
	}
}

package me.NerdsWBNerds.TempBan;

import java.util.Date;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.apache.commons.lang.StringUtils;

public class CommandExec implements CommandExecutor {
	public TempBan plugin;
	
	public CommandExec(TempBan p){
		plugin = p;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String desc, String[] args) {
		if(!sender.hasPermission("tempban.ban")){
			sender.sendMessage(ChatColor.RED + "[TempBan] You need OP or the 'tempban.ban' permission to do this.");
			return true;
		}

		if(args.length < 3) {
			sender.sendMessage(ChatColor.RED + "[TempBan] Usage: /tempban <player> <time> <unit> [reason]");
			return true;
		}

		String reason = args.length > 3 ? StringUtils.join(args, ' ', 3, args.length) : null;

		long now = System.currentTimeMillis();
		long endOfBan = now + BanUnit.getTicks(args[2], Integer.parseInt(args[1]));
		long diff = endOfBan - now;

		String remainingTimeString = getMSG(endOfBan);

		if(diff <= 0){
			sender.sendMessage(ChatColor.RED + "[TempBan] Error: Invalid amount or unit (valid units: sec, min, hour, day, month, year)");
			return true;
		}

		Date endOfBanDate = new Date(endOfBan);
		BanEntry entry = Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, endOfBanDate, sender.getName());

		if(entry == null){
			sender.sendMessage(ChatColor.RED + "[TempBan] Error: Player '" + args[0] + "' not found.");
			return true;
		}else{
			String bannedName = entry.getTarget();
			sender.sendMessage("[TempBan] The player '" + bannedName + "' is now banned for " + remainingTimeString);
		}

		Player player = Bukkit.getPlayer(args[0]);
		if(player != null){
			String secondLine = reason != null ? "\nReason: " + reason : "";
			player.kickPlayer("[TempBan] You are temp-banned for " + remainingTimeString + secondLine);
		}

		return true;
	}

	public static String getMSG(long endOfBan){
		String message = "";

		long now = System.currentTimeMillis();
		long diff = endOfBan - now;
		int seconds = (int) (diff / 1000);				
		
		if(seconds >= 60*60*24){
			int days = seconds / (60*60*24);
			seconds = seconds % (60*60*24);
			
			message += days + " Day(s) ";
		}
		if(seconds >= 60*60){
			int hours = seconds / (60*60);
			seconds = seconds % (60*60);
			
			message += hours + " Hour(s) ";
		}	
		if(seconds >= 60){
			int min = seconds / 60;
			seconds = seconds % 60;
			
			message += min + " Minute(s) ";
		}	
		if(seconds >= 0){
			message += seconds + " Second(s) ";
		}	
		
		return message;
	}
}

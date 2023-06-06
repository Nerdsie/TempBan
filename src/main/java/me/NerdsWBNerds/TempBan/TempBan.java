package me.NerdsWBNerds.TempBan;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class TempBan extends JavaPlugin {
	public static HashMap<String, Long> banned = new HashMap<String, Long>();

	public static String LegacyDataPath = "plugins/TempBan" + File.separator + "BanList.dat";
	public TBListener Listener = new TBListener(this);
	public Server server;
	public Logger log;

	public void onEnable(){
		server = this.getServer();
		log = this.getLogger();

		server.getPluginManager().registerEvents(Listener, this);
		
		File file = new File(LegacyDataPath);
		if(file.exists()){
			importLegacyBans(loadLegacyData());
			file.delete();
	    }
		
		try {
			Metrics metrics = new Metrics(this, 18675);
		} catch (Exception e) {
			System.out.println("[TempBan] Failed to start Metrics.");
		}

		this.getCommand("tempban").setExecutor(new CommandExec(this));
	}

	//If anyone updates to this version from an old version, import legacy data to use official Bukkit BanList.
	void importLegacyBans(Map<String, Long> banned){
		for (String name : banned.keySet()){
			Bukkit.getBanList(BanList.Type.NAME).addBan(name, null, new Date(banned.get(name)), null);
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Long> loadLegacyData(){
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LegacyDataPath));
			Object result = ois.readObject();
			ois.close();
			return (HashMap<String,Long>)result;
		}catch(Exception e){
			return null;
		}
	}
}

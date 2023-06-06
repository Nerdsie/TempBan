package me.NerdsWBNerds.TempBan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class TempBan extends JavaPlugin {
	public static HashMap<String, Long> banned = new HashMap<String, Long>();
	
	public static String Path = "plugins/TempBan" + File.separator + "BanList.dat";
	public TBListener Listener = new TBListener(this);
	public Server server;
	public Logger log;
	
	public void onEnable(){
		server = this.getServer();
		log = this.getLogger();

		server.getPluginManager().registerEvents(Listener, this);
		
		File file = new File(Path);
		new File("plugins/TempBan").mkdir();
	    
		if(file.exists()){
			banned = load();
	    }
		
		if(banned == null){
			banned = new HashMap<String, Long>();
		}

		try {
			Metrics metrics = new Metrics(this, 18675);
		} catch (Exception e) {
			System.out.println("[TempBan] Failed to start Metrics.");
		}

		this.getCommand("tempban").setExecutor(new CommandExec(this));
		this.getCommand("tempbanexact").setExecutor(new CommandExec(this));
		this.getCommand("unban").setExecutor(new CommandExec(this));
		this.getCommand("check").setExecutor(new CommandExec(this));
	}
	
	public void onDisable(){
		save();
	}
	
	public static void save(){
		File file = new File("plugins/TempBan" + File.separator + "BanList.dat");
		new File("plugins/TempBan").mkdir();
	    if(!file.exists()){
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path));
			oos.writeObject(banned);
			oos.flush();
			oos.close();
			//Handle I/O exceptions
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	@SuppressWarnings("unchecked")
	public static HashMap<String, Long> load(){
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path));
			Object result = ois.readObject();
			ois.close();
			return (HashMap<String,Long>)result;
		}catch(Exception e){
			return null;
		}
	}
}

package ca.qc.icerealm.bukkit.plugins.socketrcon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SocketRconPlugin extends JavaPlugin implements Runnable {
	
	private ServerSocket server;

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		
		new Thread(this).start();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			server = new ServerSocket(5555);
			
			Logger.getLogger("Minecraft").info("Accepting connection");
			
			Socket client = server.accept();
						
			Logger.getLogger("Minecraft").info("Received connection");
			
			getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

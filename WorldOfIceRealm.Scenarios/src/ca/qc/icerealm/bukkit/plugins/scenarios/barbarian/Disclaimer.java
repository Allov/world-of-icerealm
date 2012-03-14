package ca.qc.icerealm.bukkit.plugins.scenarios.barbarian;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class Disclaimer implements ZoneObserver {

	private WorldZone _zone;
	private Server _server;
	private List<String> _messageList;
	
	public Disclaimer(Server s, WorldZone z, String msg) {
		_zone = z;
		_server = s;
		_messageList = new ArrayList<String>();
		_messageList.add(msg);
	}
	
	public  Disclaimer(Server s, WorldZone z, List<String> list) {
		_zone = z;
		_server = s;
		_messageList = list;
	}

	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		for (String msg : _messageList) {
			p.sendMessage(msg);	
		}
	}

	@Override
	public void playerLeft(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
}

package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommander implements CommandExecutor {

	private EventService _service;
	private Map<Integer, Event> _events;
	
	public EventCommander(EventService s) {
		_service = s;
		_events = new HashMap<Integer, Event>();
		mapEventsToId();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,	String[] arg3) {
		
		try {
			if (!sender.isOp()) {
				sender.sendMessage("Only OP can do that. This action will be reported.");
				return false;
			}
			else {
				
				if (arg3.length == 1 && arg3[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "Event Commander Help");
					sender.sendMessage(ChatColor.YELLOW + "/ev list " + ChatColor.GOLD + "Display the list of events, use the ID to issue commands");
					sender.sendMessage(ChatColor.YELLOW + "/ev [int] release " + ChatColor.GOLD + "Release the event, the event will not trigger");
					sender.sendMessage(ChatColor.YELLOW + "/ev [int] activate " + ChatColor.GOLD + "Activate the event, the event will trigger");
					sender.sendMessage(ChatColor.YELLOW + "/ev [int] teleport " + ChatColor.GOLD + "Teleport to the event");
					sender.sendMessage(ChatColor.YELLOW + "/ev [int] config " + ChatColor.GOLD + "Display the configuration for an event");
				}
				
				if (arg3.length == 1 && arg3[0].equalsIgnoreCase("list")) {
					mapEventsToId();
					for (Integer id : _events.keySet()) {
						sender.sendMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + id + ChatColor.DARK_GREEN + "]: " + getFormattedEvent(_events.get(id)));
					}
				}		
				
				if (arg3.length == 2) {
					
					int eventId = Integer.parseInt(arg3[0]);
					
					if (arg3[1].equalsIgnoreCase("release")) {
						
						Event e = _events.get(eventId);
						e.releaseEvent();
						sender.sendMessage("Event release: " + getFormattedEvent(_events.get(eventId)));
					}
					else if (arg3[1].equalsIgnoreCase("activate")) {
						Event e = _events.get(eventId);
						e.activateEvent();
						sender.sendMessage("Event reset: " + getFormattedEvent(_events.get(eventId)));
					}
					else if (arg3[1].equalsIgnoreCase("teleport")) {
						
						if (sender instanceof Player) {
							Player p = (Player)sender;
							Location l = _events.get(eventId).getSourceLocation();
							p.teleport(l);
							p.sendMessage("Teleport to event: " + getFormattedEvent(_events.get(eventId)));
						}
					}
					else if (arg3[1].equalsIgnoreCase("config")) {
						Event e = _events.get(eventId);
						sender.sendMessage("Event config: " + e.getName() + " = " + e.getConfiguration());
					}
					else {
						sender.sendMessage(ChatColor.RED + "No command found! " + ChatColor.GREEN + " type /ev help");
					}
					
				}
				
				if (arg3.length == 2) {
					
					
					
				}
				
				return true;
			}
		}
		catch (Exception ex) {
			sender.sendMessage(ChatColor.RED + "Exception occured: " + ex.getMessage());
			return false;
		}
		
	}
	
	private String getFormattedEvent(Event e) {
		Location l = e.getSourceLocation();
		return ChatColor.GOLD + e.getName() + ChatColor.YELLOW + " at " + l.getX() + ", " + l.getY() + ", " + l.getZ();
	}
	
	private void mapEventsToId() {
		
		for (int i = 0; i < _service.getActiveEvents().size(); i++) {
			_events.put(i, _service.getActiveEvents().get(i));			
		}
	}
}

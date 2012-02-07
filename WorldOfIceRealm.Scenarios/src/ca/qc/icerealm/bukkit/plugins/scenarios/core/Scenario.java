package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;


public abstract class Scenario {
	
	private WorldZone _zone;
	private List<Player> _players;
	private String _name;
	private Server _server;
	private JavaPlugin _plugin;
	private World _world;
	
	/*
	 *	Indique à l'engine que le scénario est démarré. L'engin vérifie si un joueur se trouve dans la zone,
	 *  et s'il n'est pas dans la liste des joueurs, il y est ajouté. Par la suite, cette méthode est 
	 *  appellé afin de déterminer si on doit démarrer le scénario ou non.
	 */
	public abstract boolean isTriggered();
	
	/*
	 * Cette méthode est appellé lorsque l'engine détermine que le scénario
	 * doit être démarré. Constitue le coeur du scénario. Initiliser vos objets ici. 
	 */
	public abstract void triggerScenario();
	
	/*
	 * Cette méthode est appellé lorsque le scénario se termine. Un scénario peut se terminer
	 * pour différente raison: les joueurs quittent la zone, les joueurs se disconnect. 
	 * Il faut faire un cleanup des objets et mettre le scénario dans l'état dans lequel il 
	 * doit se trouver lorsqu'il sera redémarré plus tard.
	 * <br><br>
	 * Exemple: si un paquet de monstre sont spawné, il est idéal de les enlever dans cette
	 * méthode.
	 */
	public abstract void abortScenario();
	
	/*
	 * Détermine si le scénario s'arrête si le dernier joueur quitte la zone d'activation. 
	 * Il n'est pas recommendé de laisser un scénario qui demande beaucoup de ressource se
	 * déroulé s'il n'y a pas de joueur dans la zone. Cette méthode est utilisé pour une
	 * fin non prévue au scénario
	 */
	public abstract boolean abortWhenLeaving();
	
	/*
	 * Indique que le scénario peut être démarré. Utile pour vérifier une condition précise
	 * afin de démarrer le scénario correctement.
	 *<br><br>
	 * Par exemple, vérifiez si le nombre de joueurs requis est atteint!
	 */
	public abstract boolean canBeTriggered();
	
	/*
	 * Indique au working prober s'il doit arrêter le scénario selon la condition codée
	 * dans l'implémentation du scénario. Si cette condition est vrai, le probing va 
	 * appellé la méthode terminateScenario.
	 */
	public abstract boolean mustBeStop();
	
	/*
	 * Le prober appelle cette méthode si le scénario doit s'arrêté de manière normale.
	 * Cette méthode est utilisé pour que le scénario se termine normalement. Ne pas 
	 * confondre avec abortScenario qui lui est codé afin de prévoir une fin imprévue
	 * au scénario.
	 */
	public abstract void terminateScenario();
	
	public abstract boolean isComplete();
	
	public World getWorld() {
		return _world;
	}
	
	public void setWorld(World w) {
		_world = w;
	}
	
	public JavaPlugin getPlugin() {
		return _plugin;
	}
	
	public void setPlugin(JavaPlugin p) {
		_plugin = p;
	}
	
	public Server getServer() {
		return _server;
	}
	
	public void setServer(Server s) {
		_server = s;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public WorldZone getZone()
	{
		return _zone;
	}
	
	public void setZone(WorldZone zone) {
		_zone = zone;
	}
	
	public void addPlayer(Player p) {
		if (_players == null) {
			_players = new ArrayList<Player>();
		}
		_players.add(p);
	}
	
	public void removePlayer(Player p) {
		_players.remove(p);
	}
	
	public List<Player> getPlayers() {
		if (_players == null) {
			_players = new ArrayList<Player>();
		}
		return _players;
	}
	

	
}

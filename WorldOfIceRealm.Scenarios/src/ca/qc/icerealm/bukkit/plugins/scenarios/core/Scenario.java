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
	 *	Indique � l'engine que le sc�nario est d�marr�. L'engin v�rifie si un joueur se trouve dans la zone,
	 *  et s'il n'est pas dans la liste des joueurs, il y est ajout�. Par la suite, cette m�thode est 
	 *  appell� afin de d�terminer si on doit d�marrer le sc�nario ou non.
	 */
	public abstract boolean isTriggered();
	
	/*
	 * Cette m�thode est appell� lorsque l'engine d�termine que le sc�nario
	 * doit �tre d�marr�. Constitue le coeur du sc�nario. Initiliser vos objets ici. 
	 */
	public abstract void triggerScenario();
	
	/*
	 * Cette m�thode est appell� lorsque le sc�nario se termine. Un sc�nario peut se terminer
	 * pour diff�rente raison: les joueurs quittent la zone, les joueurs se disconnect. 
	 * Il faut faire un cleanup des objets et mettre le sc�nario dans l'�tat dans lequel il 
	 * doit se trouver lorsqu'il sera red�marr� plus tard.
	 * <br><br>
	 * Exemple: si un paquet de monstre sont spawn�, il est id�al de les enlever dans cette
	 * m�thode.
	 */
	public abstract void abortScenario();
	
	/*
	 * D�termine si le sc�nario s'arr�te si le dernier joueur quitte la zone d'activation. 
	 * Il n'est pas recommend� de laisser un sc�nario qui demande beaucoup de ressource se
	 * d�roul� s'il n'y a pas de joueur dans la zone. Cette m�thode est utilis� pour une
	 * fin non pr�vue au sc�nario
	 */
	public abstract boolean abortWhenLeaving();
	
	/*
	 * Indique que le sc�nario peut �tre d�marr�. Utile pour v�rifier une condition pr�cise
	 * afin de d�marrer le sc�nario correctement.
	 *<br><br>
	 * Par exemple, v�rifiez si le nombre de joueurs requis est atteint!
	 */
	public abstract boolean canBeTriggered();
	
	/*
	 * Indique au working prober s'il doit arr�ter le sc�nario selon la condition cod�e
	 * dans l'impl�mentation du sc�nario. Si cette condition est vrai, le probing va 
	 * appell� la m�thode terminateScenario.
	 */
	public abstract boolean mustBeStop();
	
	/*
	 * Le prober appelle cette m�thode si le sc�nario doit s'arr�t� de mani�re normale.
	 * Cette m�thode est utilis� pour que le sc�nario se termine normalement. Ne pas 
	 * confondre avec abortScenario qui lui est cod� afin de pr�voir une fin impr�vue
	 * au sc�nario.
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

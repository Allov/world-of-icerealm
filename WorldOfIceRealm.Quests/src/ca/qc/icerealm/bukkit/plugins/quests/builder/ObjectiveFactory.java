package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.quests.CollectObjective;
import ca.qc.icerealm.bukkit.plugins.quests.FindObjective;
import ca.qc.icerealm.bukkit.plugins.quests.KillObjective;
import ca.qc.icerealm.bukkit.plugins.quests.Objective;
import ca.qc.icerealm.bukkit.plugins.quests.Quest;
import ca.qc.icerealm.bukkit.plugins.quests.QuestPlugin;
import ca.qc.icerealm.bukkit.plugins.quests.ZoneObjective;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class ObjectiveFactory {
	private static final String ObjectiveTypeCollect = "collect";
	private static final String ObjectiveTypeKill = "kill";
	private static final String ObjectiveTypeZone = "zone";
	private static final String ObjectiveTypeFind = "find";
	private static final String ObjectiveMonsterFury = "monsterfury";
	private static ObjectiveFactory instance;

	public static ObjectiveFactory getInstance() {
		if (instance == null) {
			instance = new ObjectiveFactory();
		}

		return instance;
	}

	public Objective createFromMap(QuestPlugin quests, Player player,
			MapWrapper map, Quest quest) {
		Objective objective = null;
		WorldZone zone = getWorldZone(quests, map);

		if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeKill)) {

			objective = createKillObjective(quests, player, map, zone);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeZone)) {

			objective = createZoneObjective(quests, player, map, zone);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeCollect)) {

			objective = createCollectObjective(quests, player, map, zone, quest);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveTypeFind)) {

			objective = createFindObjective(quests, player, map, zone);

		} else if (map.getString("type", "").equalsIgnoreCase(ObjectiveMonsterFury)) {

		}

		return objective;
	}

	private FindObjective createFindObjective(QuestPlugin quests, Player player, MapWrapper map, WorldZone zone) {
		FindObjective objective = new FindObjective(player, zone, map.getString("name", ""), map.getInt("amount", 0), map.getInt("what", 0));

		quests.getPluginManager().registerEvents(objective, quests);
		return objective;
	}

	private CollectObjective createCollectObjective(QuestPlugin quests, Player player, MapWrapper map, WorldZone zone, Quest quest) {
		CollectObjective objective = new CollectObjective(player, zone,
				map.getString("name", "N/A"), map.getInt("amount", 0),
				map.getBoolean("keep", false), map.getInt("what", 0));

		if (!quest.isCompleted()) {
			quest.addListener(objective);
			
			ScheduledObjectiveThreadTracker.getInstance().getScheduledThreads().put(objective, 
					Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(objective, 1000, 100, TimeUnit.MILLISECONDS));
		}
		

		return objective;
	}

	private ZoneObjective createZoneObjective(QuestPlugin quests, Player player, MapWrapper map, WorldZone zone) {
		ZoneObjective objective = new ZoneObjective(player, zone,
				map.getString("name", ""), quests.getServer());

		ZoneServer.getInstance().addListener((ZoneObjective) objective);
		return objective;
	}

	private KillObjective createKillObjective(QuestPlugin quests, Player player, MapWrapper map, WorldZone zone) {
		List<Integer> entityIds = getEntities(map);

		KillObjective objective = new KillObjective(player, map.getString("name", ""), zone, map.getInt("amount", 0), entityIds);

		quests.getServer().getPluginManager().registerEvents(objective, quests);
		return objective;
	}

	private List<Integer> getEntities(MapWrapper map) {
		List<Integer> entityIds = new ArrayList<Integer>();

		String ids = map.getString("what", "");

		if (ids != null && ids.length() > 0) {
			String[] entities = ids.split(",");

			for (int i = 0; i < entities.length; i++) {
				entityIds.add(Integer.parseInt(entities[i]));
			}
		}
		return entityIds;
	}

	private WorldZone getWorldZone(QuestPlugin quests, MapWrapper map) {
		WorldZone zone = null;
		String coords = map.getString("zone", "");
		String world = map.getString("world", "world");

		if (coords.split(",").length == 6) {
			zone = new WorldZone(quests.getServer().getWorld(world), coords);
		}
		return zone;
	}
}

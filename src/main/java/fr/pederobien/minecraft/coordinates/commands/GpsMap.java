package fr.pederobien.minecraft.coordinates.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.pederobien.minecraft.coordinates.commands.exceptions.GpsEntryAlreadyRegisteredException;
import fr.pederobien.minecraft.coordinates.commands.exceptions.GpsEntryNotRegisteredException;

public class GpsMap {
	private Map<Player, Map<String, GpsEntry>> entries;

	private GpsMap() {
		entries = new HashMap<Player, Map<String, GpsEntry>>();
	}

	public static GpsMap getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final GpsMap INSTANCE = new GpsMap();
	}

	/**
	 * Puts the given entry to this map.
	 * 
	 * @param player The player that run a gps.
	 * @param entry  The player's gps.
	 * 
	 * @return The map of all registered gps for the given player.
	 * 
	 * @throws GpsEntryAlreadyRegisteredException if a gps entry is already registered for the destination name.
	 */
	public Map<String, GpsEntry> put(Player player, GpsEntry entry) {
		Map<String, GpsEntry> gps = entries.get(player);
		if (gps == null) {
			gps = new HashMap<String, GpsEntry>();
			entries.put(player, gps);
		} else {
			GpsEntry gpsEntry = gps.get(entry.getDestination());
			if (gpsEntry != null)
				throw new GpsEntryAlreadyRegisteredException(entry.getDestination());
		}
		gps.put(entry.getDestination(), entry);
		return gps;
	}

	/**
	 * Removes the gps associated to the given destination name from this map.
	 * 
	 * @param player          The player whose gps should be removed.
	 * @param destinationName The gps destination name to remove.
	 * 
	 * @return The gps associated to the given destination name.
	 * 
	 * @throws GpsEntryNotRegisteredException if there is any gps registered for the given destination name.
	 */
	public GpsEntry remove(Player player, String destinationName) {
		Map<String, GpsEntry> gps = entries.get(player);
		GpsEntry entry;
		if (gps == null || gps.isEmpty() || (entry = gps.remove(destinationName)) == null)
			throw new GpsEntryNotRegisteredException(destinationName);
		return entry;
	}

	/**
	 * Get the map of gps registered for the given player.
	 * 
	 * @param player The player whose registered gps are returned.
	 * 
	 * @return A map that contains each registered gps for the given player.
	 */
	public Map<String, GpsEntry> get(Player player) {
		return entries.get(player);
	}

	/**
	 * Removes all of the mappings from this map (optional operation). The map will be empty after this call returns.
	 *
	 * @throws UnsupportedOperationException if the <tt>clear</tt> operation is not supported by this map
	 */
	public void clear() {
		Iterator<Map<String, GpsEntry>> iterator0 = entries.values().iterator();
		while (iterator0.hasNext()) {
			Map<String, GpsEntry> gpsEntries = iterator0.next();
			Iterator<GpsEntry> iterator1 = gpsEntries.values().iterator();
			while (iterator1.hasNext())
				iterator1.next().stop();
		}
		entries.clear();
	}
}

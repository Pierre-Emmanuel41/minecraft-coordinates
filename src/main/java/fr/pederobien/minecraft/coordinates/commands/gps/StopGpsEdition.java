package fr.pederobien.minecraft.coordinates.commands.gps;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraft.coordinates.commands.ECoordinatesMessageCode;
import fr.pederobien.minecraft.coordinates.commands.exceptions.GpsEntryNotRegisteredException;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;

public class StopGpsEdition extends AbstractSimpleMapEdition {

	public StopGpsEdition() {
		super("stop", ECoordinatesMessageCode.STOP_GPS__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
		List<GpsEntry> entriesToStop = emptyList();
		for (String destinationName : args)
			try {
				entriesToStop.add(GpsMap.getInstance().remove(player, destinationName));
			} catch (GpsEntryNotRegisteredException e) {
				sendNotSynchro(sender, ECoordinatesMessageCode.STOP_GPS__GPS_NOT_FOUND, destinationName);
				return false;
			}

		if (entriesToStop.isEmpty()) {
			sendSynchro(sender, ECoordinatesMessageCode.STOP_GPS__NO_GPS_TO_STOP);
			return true;
		}

		StringJoiner entryNames = new StringJoiner(", ");
		for (GpsEntry entry : entriesToStop) {
			entry.stop();
			entryNames.add(entry.getDestinationName());
		}

		switch (entriesToStop.size()) {
		case 1:
			sendSynchro(sender, ECoordinatesMessageCode.STOP_GPS__ONE_GPS_TO_STOP, entryNames);
			break;
		default:
			sendSynchro(sender, ECoordinatesMessageCode.STOP_GPS__SEVERAL_GPS_TO_STOP, entryNames);
			break;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player))
			return emptyList();

		Map<String, GpsEntry> entries = GpsMap.getInstance().get((Player) sender);
		return entries == null ? emptyList() : filter(getFreeGpsEntries(entries, asList(args)), args);
	}

	private Stream<String> getFreeGpsEntries(Map<String, GpsEntry> total, List<String> alreadyMentionnedEntries) {
		return total.values().stream().map(entry -> entry.getDestinationName()).filter(entry -> !alreadyMentionnedEntries.contains(entry));
	}
}

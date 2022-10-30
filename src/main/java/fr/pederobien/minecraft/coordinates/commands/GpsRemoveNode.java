package fr.pederobien.minecraft.coordinates.commands;

import java.util.List;
import java.util.function.Predicate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeNode;
import fr.pederobien.minecraft.coordinates.commands.exceptions.GpsEntryNotRegisteredException;
import fr.pederobien.minecraft.game.GamePlugin;

public class GpsRemoveNode extends MinecraftCodeNode {

	/**
	 * Creates a node in order to remove a GPS from the score board of a player.
	 */
	protected GpsRemoveNode() {
		super("remove", ECoordinatesMessageCode.GPS__REMOVE__EXPLANATION, () -> GamePlugin.getGameTree().getGame() != null);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player))
			return emptyList();

		Player player = (Player) sender;
		Predicate<String> filter = destination -> !asList(args).contains(destination);
		return filter(GpsMap.getInstance().get(player).values().stream().map(entry -> entry.getDestination()).filter(filter), args);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__CANNOT_DISPLAY_PLAYER_COORDINATES).build());
			return false;
		}

		Player player = (Player) sender;
		List<GpsEntry> destinations = emptyList();
		for (String destination : args) {
			try {
				destinations.add(GpsMap.getInstance().remove(player, destination));
			} catch (GpsEntryNotRegisteredException e) {
				send(eventBuilder(sender, ECoordinatesMessageCode.GPS__REMOVE__GPS_NOT_FOUND, e.getDestination()));
				return false;
			}
		}

		String destinationNames = concat(args, ", ");
		for (GpsEntry entry : destinations)
			entry.stop();

		switch (destinations.size()) {
		case 0:
			sendSuccessful(sender, ECoordinatesMessageCode.GPS__REMOVE__NO_GPS_TO_REMOVE);
			return true;
		case 1:
			sendSuccessful(sender, ECoordinatesMessageCode.GPS__REMOVE__ONE_GPS_TO_REMOVE, destinationNames);
			return true;
		default:
			sendSuccessful(sender, ECoordinatesMessageCode.GPS__REMOVE__SEVERAL_GPS_TO_REMOVE, destinationNames);
			return true;
		}
	}
}

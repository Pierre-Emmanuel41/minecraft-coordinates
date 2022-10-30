package fr.pederobien.minecraft.coordinates.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Predicate;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeNode;
import fr.pederobien.minecraft.coordinates.CoordinatesPlugin;
import fr.pederobien.minecraft.coordinates.commands.exceptions.GpsEntryAlreadyRegisteredException;
import fr.pederobien.minecraft.managers.ScoreboardManager;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.platform.GamePlatformPlugin;
import fr.pederobien.minecraft.platform.Platform;
import fr.pederobien.minecraft.scoreboards.impl.Objective;
import fr.pederobien.minecraft.scoreboards.interfaces.IObjective;

public class GpsAddNode extends MinecraftCodeNode {

	/**
	 * Creates a node in order to add a GPS to the score boards of a player.
	 */
	protected GpsAddNode() {
		super("add", ECoordinatesMessageCode.GPS__ADD__EXPLANATION, () -> GamePlatformPlugin.getGameTree().getGame() != null);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player))
			return emptyList();

		switch (args.length) {
		case 1:
			return asList(getMessage(sender, ECoordinatesMessageCode.GPS__DESTINATION_NAME__COMPLETION));
		case 2:
			Map<String, GpsEntry> playerGps = GpsMap.getInstance().get((Player) sender);
			Predicate<String> isDestinationValid = destination -> playerGps == null || playerGps.get(destination) == null;
			return check(args[0], isDestinationValid, asList("<X> <Z>"));
		case 3:
			return check(args[1], e -> isNotStrictInt(e), asList("<Z>"));
		default:
			return emptyList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__CANNOT_DISPLAY_PLAYER_COORDINATES).build());
			return false;
		}

		Player player = (Player) sender;

		String destination;
		try {
			destination = args[0];
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__ADD__DESTINATION_IS_MISSING).build());
			return false;
		}

		int x;
		try {
			x = getInt(args[1]);
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__ADD__X_COORDINATE_IS_MISSING).build());
			return false;
		} catch (NumberFormatException e) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__ADD__X_COORDINATE_BAD_FORMAT).build());
			return false;
		}

		int z;
		try {
			z = getInt(args[1]);
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__ADD__Z_COORDINATE_IS_MISSING).build());
			return false;
		} catch (NumberFormatException e) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__ADD__Z_COORDINATE_BAD_FORMAT).build());
			return false;
		}

		try {
			WorldBorder border = player.getWorld().getWorldBorder();
			// offset based on the world border center in which is the player.
			Block target = WorldManager.getHighestBlockYAt(player.getWorld(), x + border.getCenter().getBlockX(), z + border.getCenter().getBlockZ());

			GpsEntry gps = new GpsEntry(0, target, destination);
			GpsMap.getInstance().put(player, gps);

			Platform platform = Platform.get(player);
			if (platform == null) {
				send(eventBuilder(sender, ECoordinatesMessageCode.GPS__ADD__NO_RUNNING_GAME).build());
				return false;
			}

			Optional<IObjective> optObjective = platform.getObjectiveUpdater().getObjective(player);
			if (optObjective.isPresent())
				optObjective.get().addEntry(2, gps);
			else {
				IObjective objective = new Objective(CoordinatesPlugin.instance(), player, "GPS", "GPS");
				objective.setScoreboard(ScoreboardManager.createScoreboard());
				objective.addEntry(gps);
				platform.getObjectiveUpdater().register(objective);
			}
		} catch (GpsEntryAlreadyRegisteredException e) {
			send(eventBuilder(sender, ECoordinatesMessageCode.GPS__ADD__GPS_ALREADY_REGISTERED, e.getDestination()));
			return false;
		}

		sendSuccessful(sender, ECoordinatesMessageCode.GPS__ADD__GPS_CREATED, destination, x, z);
		return true;
	}
}

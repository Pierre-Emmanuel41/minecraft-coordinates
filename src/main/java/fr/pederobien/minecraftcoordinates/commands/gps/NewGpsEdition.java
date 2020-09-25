package fr.pederobien.minecraftcoordinates.commands.gps;

import java.util.List;
import java.util.Optional;

import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraftcoordinates.CoordinatesPlugin;
import fr.pederobien.minecraftcoordinates.commands.ECoordinatesMessageCode;
import fr.pederobien.minecraftcoordinates.commands.exceptions.GpsEntryAlreadyRegisteredException;
import fr.pederobien.minecraftdictionary.impl.Permission;
import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftmanagers.EColor;
import fr.pederobien.minecraftmanagers.ScoreboardManager;
import fr.pederobien.minecraftmanagers.WorldManager;
import fr.pederobien.minecraftscoreboards.impl.Objective;
import fr.pederobien.minecraftscoreboards.interfaces.IObjective;

public class NewGpsEdition extends AbstractSimpleMapEdition {

	public NewGpsEdition() {
		super("new", ECoordinatesMessageCode.NEW_GPS__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		int xCoord, zCoord;
		try {
			xCoord = Integer.parseInt(args[0]);
			zCoord = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException e) {
			ECommonMessageCode.COMMON_MISSING_COORDINATES.setPermission(Permission.SENDER);
			sendNotSynchro(sender, ECommonMessageCode.COMMON_MISSING_COORDINATES);
			ECommonMessageCode.COMMON_MISSING_COORDINATES.setPermission(Permission.OPERATORS);
			return false;
		} catch (NumberFormatException e) {
			ECommonMessageCode.COMMON_BAD_INTEGER_FORMAT.setPermission(Permission.SENDER);
			sendNotSynchro(sender, ECommonMessageCode.COMMON_BAD_INTEGER_FORMAT);
			ECommonMessageCode.COMMON_BAD_INTEGER_FORMAT.setPermission(Permission.OPERATORS);
			return false;
		}

		WorldBorder border = player.getWorld().getWorldBorder();
		// offset based on the world border center in which is the player.
		Block target = WorldManager.getHighestBlockYAt(player.getWorld(), xCoord + border.getCenter().getBlockX(), zCoord + border.getCenter().getBlockZ());
		String destinationName = args.length == 3 ? args[2] : xCoord + ";" + zCoord;

		GpsEntry entry = null;
		try {
			entry = new GpsEntry(0, target, destinationName);
			GpsMap.getInstance().put(player, entry);
		} catch (GpsEntryAlreadyRegisteredException e) {
			sendNotSynchro(sender, ECoordinatesMessageCode.NEW_GPS__GPS_ALREADY_REGISTERED, entry.getDestinationName());
			return false;
		}

		Optional<IObjective> optObj = Plateform.getObjectiveUpdater().getObjective(player);

		if (optObj.isPresent())
			optObj.get().addEntry(2, entry);
		else {
			IObjective objective = new Objective(CoordinatesPlugin.get(), player, "GPS", "GPS");
			objective.setScoreboard(ScoreboardManager.createScoreboard());
			objective.addEntry(entry);
		}

		sendNotSynchro(sender, ECoordinatesMessageCode.NEW_GPS__GPS_STARTED, EColor.GRAY, xCoord, zCoord);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return check(args[0], e -> isNotStrictInt(e), asList("<X> <Z>"));
		case 2:
			return check(args[1], e -> isNotStrictInt(e), check(args[0], e -> isStrictInt(e), asList("<Z>")));
		case 3:
			return check(args[2], e -> isNotStrictInt(e), asList(getMessage(sender, ECommonMessageCode.COMMON_NEW_TAB_COMPLETE)));
		default:
			return emptyList();
		}
	}
}

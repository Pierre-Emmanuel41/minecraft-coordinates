package fr.pederobien.minecraftcoordinates.commands.gps;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftcoordinates.commands.ECoordinatesMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftmanagers.EColor;

public class GpsEdition extends AbstractSimpleMapEdition {

	public GpsEdition() {
		super("gps", ECoordinatesMessageCode.GPS__EXPLANATION);
		addEdition(GpsEditionFactory.newGpsEdition());
		addEdition(GpsEditionFactory.stopGpsEdition());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!Plateform.getGameConfigurationContext().isRunning()) {
			sendNotSynchro(sender, ECoordinatesMessageCode.GPS__NO_GAME_IS_RUNNING, EColor.DARK_RED);
			return false;
		}
		return super.onCommand(sender, command, label, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return !Plateform.getGameConfigurationContext().isRunning() ? emptyList() : super.onTabComplete(sender, command, alias, args);
	}
}

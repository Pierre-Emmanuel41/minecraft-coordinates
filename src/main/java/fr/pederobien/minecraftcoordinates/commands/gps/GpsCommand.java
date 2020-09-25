package fr.pederobien.minecraftcoordinates.commands.gps;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.commands.AbstractSimpleCommand;
import fr.pederobien.minecraftgameplateform.interfaces.element.IGame;

public class GpsCommand extends AbstractSimpleCommand {

	public GpsCommand(JavaPlugin plugin) {
		super(plugin, new GpsEdition());
	}

	@Override
	public <U extends IGame> void onGameIsStopped(U IGame) {
		GpsMap.getInstance().clear();
	}
}

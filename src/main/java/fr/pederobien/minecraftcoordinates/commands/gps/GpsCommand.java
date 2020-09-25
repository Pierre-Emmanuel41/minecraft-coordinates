package fr.pederobien.minecraftcoordinates.commands.gps;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.commands.AbstractSimpleCommand;

public class GpsCommand extends AbstractSimpleCommand {

	public GpsCommand(JavaPlugin plugin) {
		super(plugin, new GpsEdition());
	}
}

package fr.pederobien.minecraftcoordinates.commands.coord;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.commands.AbstractSimpleCommand;

public class CoordinatesCommand extends AbstractSimpleCommand {

	public CoordinatesCommand(JavaPlugin plugin) {
		super(plugin, new CoordinateEdition());
	}
}

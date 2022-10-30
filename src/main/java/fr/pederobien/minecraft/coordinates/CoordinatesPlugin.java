package fr.pederobien.minecraft.coordinates;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.dictionary.impl.JarXmlDictionaryParser;
import fr.pederobien.minecraft.coordinates.commands.CoordinatesCommandTree;
import fr.pederobien.minecraft.dictionary.impl.MinecraftDictionaryContext;
import fr.pederobien.utils.AsyncConsole;

public class CoordinatesPlugin extends JavaPlugin {
	private static final String DICTIONARY_FOLDER = "resources/dictionaries/coordinates/";

	private static Plugin instance;
	private static CoordinatesCommandTree coordinateCommandTree;

	/**
	 * @return The instance of this plugin.
	 */
	public static Plugin instance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		coordinateCommandTree = new CoordinatesCommandTree();

		registerDictionaries();
		registerTabExecutors();
	}

	private void registerDictionaries() {
		JarXmlDictionaryParser dictionaryParser = new JarXmlDictionaryParser(getFile().toPath());

		MinecraftDictionaryContext context = MinecraftDictionaryContext.instance();
		String[] dictionaries = new String[] { "English.xml", "French.xml" };
		for (String dictionary : dictionaries)
			try {
				context.register(dictionaryParser.parse(DICTIONARY_FOLDER.concat(dictionary)));
			} catch (Exception e) {
				AsyncConsole.print(e);
				for (StackTraceElement element : e.getStackTrace())
					AsyncConsole.print(element);
			}
	}

	private void registerTabExecutors() {
		PluginCommand coord = getCommand(coordinateCommandTree.getCoordinateNode().getLabel());
		coord.setTabCompleter(coordinateCommandTree.getCoordinateNode());
		coord.setExecutor(coordinateCommandTree.getCoordinateNode());

		PluginCommand gps = getCommand(coordinateCommandTree.getGpsNode().getRoot().getLabel());
		gps.setTabCompleter(coordinateCommandTree.getGpsNode());
		gps.setExecutor(coordinateCommandTree.getGpsNode());
	}
}

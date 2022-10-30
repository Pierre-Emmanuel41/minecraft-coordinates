package fr.pederobien.minecraft.coordinates;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.dictionary.interfaces.IDictionaryParser;
import fr.pederobien.minecraft.coordinates.commands.coord.CoordinatesCommand;
import fr.pederobien.minecraft.coordinates.commands.gps.GpsCommand;
import fr.pederobien.minecraftgameplateform.utils.Plateform;

public class CoordinatesPlugin extends JavaPlugin {
	private static Plugin plugin;

	/**
	 * @return The plugin associated to this coordinates plugin.
	 */
	public static Plugin get() {
		return plugin;
	}

	@Override
	public void onEnable() {
		Plateform.getPluginHelper().register(this);
		plugin = this;

		new CoordinatesCommand(this);
		new GpsCommand(this);

		registerDictionaries();
	}

	private void registerDictionaries() {
		String[] dictionaries = new String[] { "Coordinates.xml" };
		// Registering French dictionaries
		registerDictionary("French", dictionaries);

		// Registering English dictionaries
		registerDictionary("English", dictionaries);
	}

	private void registerDictionary(String parent, String... dictionaryNames) {
		Path jarPath = Plateform.ROOT.getParent().resolve(getName().concat(".jar"));
		String dictionariesFolder = "resources/dictionaries/".concat(parent).concat("/");
		for (String name : dictionaryNames)
			registerDictionary(Plateform.getDefaultDictionaryParser(dictionariesFolder.concat(name)), jarPath);
	}

	private void registerDictionary(IDictionaryParser parser, Path jarPath) {
		try {
			Plateform.getNotificationCenter().getDictionaryContext().register(parser, jarPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

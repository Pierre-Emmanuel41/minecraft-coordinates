package fr.pederobien.minecraftcoordinates.commands.gps;

import fr.pederobien.minecraftdevelopmenttoolkit.interfaces.messagecode.IMessageCodeSimpleMapEdition;

public class GpsEditionFactory {

	/**
	 * @return An edition to display the direction to follow.
	 */
	public static IMessageCodeSimpleMapEdition newGpsEdition() {
		return new NewGpsEdition();
	}

	/**
	 * @return An edition to display the direction to follow.
	 */
	public static IMessageCodeSimpleMapEdition stopGpsEdition() {
		return new StopGpsEdition();
	}
}

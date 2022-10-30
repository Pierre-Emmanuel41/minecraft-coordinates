package fr.pederobien.minecraft.coordinates.commands.exceptions;

import fr.pederobien.minecraftgameplateform.exceptions.SimpleMessageException;

public class GpsEntryNotRegisteredException extends SimpleMessageException {
	private static final long serialVersionUID = 1L;
	private String destinationName;

	public GpsEntryNotRegisteredException(String destinationName) {
		super("The gps " + destinationName + " is not registered");
		this.destinationName = destinationName;
	}

	/**
	 * @return The not registered gps entry name.
	 */
	public String getDestinationName() {
		return destinationName;
	}
}

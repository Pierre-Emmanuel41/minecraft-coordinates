package fr.pederobien.minecraft.coordinates.commands.exceptions;

import fr.pederobien.minecraft.coordinates.commands.gps.GpsEntry;
import fr.pederobien.minecraftgameplateform.exceptions.SimpleMessageException;

public class GpsEntryAlreadyRegisteredException extends SimpleMessageException {
	private static final long serialVersionUID = 1L;
	private GpsEntry gps;

	public GpsEntryAlreadyRegisteredException(GpsEntry gps) {
		super("The gps " + gps.getDestinationName() + " is already registered");
		this.gps = gps;
	}

	/**
	 * @return The already registered gps entry.
	 */
	public GpsEntry getGps() {
		return gps;
	}
}

package fr.pederobien.minecraft.coordinates.commands.exceptions;

public class GpsEntryNotRegisteredException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String destination;

	/**
	 * Creates an exception thrown when a GPS is already registered for a destination.
	 * 
	 * @param destination The already registered destination.
	 */
	public GpsEntryNotRegisteredException(String destination) {
		super(String.format("There is no gps leading to %s", destination));
		this.destination = destination;
	}

	/**
	 * @return The destination associated to this exception.
	 */
	public String getDestination() {
		return destination;
	}
}

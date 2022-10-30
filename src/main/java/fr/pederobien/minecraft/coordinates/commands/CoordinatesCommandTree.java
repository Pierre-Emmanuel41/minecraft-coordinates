package fr.pederobien.minecraft.coordinates.commands;

import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeNode;

public class CoordinatesCommandTree {
	private IMinecraftCodeNode coordinateNode;
	private IMinecraftCodeNode gpsNode;

	/**
	 * Creates a command tree that contains no root but several children.
	 */
	public CoordinatesCommandTree() {
		coordinateNode = new CoordinatesNode();
		gpsNode = new GpsNode();
	}

	/**
	 * @return The node that display the coordinates of a player.
	 */
	public IMinecraftCodeNode getCoordinateNode() {
		return coordinateNode;
	}

	/**
	 * @return The node that appends or removes a GPS line from the score board a player.
	 */
	public IMinecraftCodeNode getGpsNode() {
		return gpsNode;
	}
}

package fr.pederobien.minecraft.coordinates.commands;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeRootNode;

public class GpsNode extends MinecraftCodeRootNode {
	private GpsAddNode addNode;
	private GpsRemoveNode removeNode;

	/**
	 * Creates a node in order to add or remove GPS from player's scoreboards.
	 */
	public GpsNode() {
		super("gps", ECoordinatesCode.GPS__EXPLANATION, () -> true);

		add(addNode = new GpsAddNode());
		add(removeNode = new GpsRemoveNode());
	}

	/**
	 * @return The node that adds a GPS line in the score board of a player.
	 */
	public GpsAddNode getAddNode() {
		return addNode;
	}

	/**
	 * @return The node the removes a GPS line from the score board of a player.
	 */
	public GpsRemoveNode getRemoveNode() {
		return removeNode;
	}
}

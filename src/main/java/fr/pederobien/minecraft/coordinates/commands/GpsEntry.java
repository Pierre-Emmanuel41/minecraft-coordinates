package fr.pederobien.minecraft.coordinates.commands;

import org.bukkit.block.Block;

import fr.pederobien.minecraft.commandtree.interfaces.ICodeSender;
import fr.pederobien.minecraft.managers.EColor;
import fr.pederobien.minecraft.managers.WorldManager;
import fr.pederobien.minecraft.platform.entries.simple.OrientationEntry;
import fr.pederobien.minecraft.scoreboards.impl.PeriodicEntryUpdater;
import fr.pederobien.minecraft.scoreboards.interfaces.IEntryUpdater;

public class GpsEntry extends OrientationEntry {
	private String destination;
	private IEntryUpdater updater;

	/**
	 * Create an entry that display the direction to follow in order to reach the given block..
	 * 
	 * @param score       The line number of this entry.
	 * @param block       The block defined as the center of the world.
	 * @param destination The name of the destination.
	 */
	public GpsEntry(int score, Block block, String destination) {
		super(score, block);
		addUpdater(updater = new GpsEntryUpdater());
		this.destination = destination;
	}

	@Override
	public String getBefore() {
		return String.format("%s: ", destination);
	}

	/**
	 * @return The name of the destination.
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Stop this entry. This entry is deactivated, and is removed from its objective.
	 */
	public void stop() {
		setActivated(false);
		removeUpdater(updater);
		getObjective().removeEntry(getScore());
	}

	private class GpsEntryUpdater extends PeriodicEntryUpdater implements ICodeSender {

		public GpsEntryUpdater() {
			super(10);
		}

		@Override
		protected void onUpdate() {
			if (!(WorldManager.getSquaredDistance2D(getPlayer().getLocation(), getBlock().getLocation()) < 10))
				return;

			sendSuccessful(getObjective().getPlayer(), ECoordinatesMessageCode.GPS__PLAYER_ARRIVED, EColor.DARK_GREEN.getInColor(destination));
			GpsMap.getInstance().remove(getPlayer(), destination);
			stop();
		}
	}
}

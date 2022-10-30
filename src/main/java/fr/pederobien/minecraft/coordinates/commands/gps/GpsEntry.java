package fr.pederobien.minecraft.coordinates.commands.gps;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.pederobien.minecraft.coordinates.commands.ECoordinatesMessageCode;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.entries.simple.OrientationEntry;
import fr.pederobien.minecraftgameplateform.interfaces.editions.IPlateformCodeSender;
import fr.pederobien.minecraftmanagers.EColor;
import fr.pederobien.minecraftmanagers.WorldManager;
import fr.pederobien.minecraftscoreboards.impl.PeriodicEntryUpdater;
import fr.pederobien.minecraftscoreboards.interfaces.IEntryUpdater;

public class GpsEntry extends OrientationEntry {
	private String destinationName;
	private IEntryUpdater updater;

	/**
	 * Create an entry that display the direction to follow in order to reach the given block..
	 * 
	 * @param score           The line number of this entry.
	 * @param block           The block defined as the center of the world.
	 * @param destinationName The destination's name.
	 */
	public GpsEntry(int score, Block block, String destinationName) {
		super(score, block);
		addUpdater(updater = new GpsEntryUpdater());
		this.destinationName = destinationName;
	}

	@Override
	protected IMinecraftMessageCode getBeforeAsCode(Player player) {
		return ECoordinatesMessageCode.NEW_GPS__SCOREBOARD_DISPLAY;
	}

	@Override
	public String getAfter() {
		return " (" + destinationName + ")";
	}

	/**
	 * @return The destination's name
	 */
	public String getDestinationName() {
		return destinationName;
	}

	/**
	 * Stop this entry. This entry is deactivated, and is removed from its objective.
	 */
	public void stop() {
		setActivated(false);
		removeUpdater(updater);
		getObjective().removeEntry(getScore());
	}

	private class GpsEntryUpdater extends PeriodicEntryUpdater implements IPlateformCodeSender {

		public GpsEntryUpdater() {
			super(10);
		}

		@Override
		protected void onUpdate() {
			if (WorldManager.getSquaredDistance2D(getPlayer().getLocation(), getBlock().getLocation()) < 50) {
				sendNotSynchro(getObjective().getPlayer(), ECoordinatesMessageCode.NEW_GPS__PLAYER_ARRIVED, EColor.GRAY, getAfter());
				GpsMap.getInstance().remove(getPlayer(), destinationName);
				stop();
				return;
			}
		}
	}
}

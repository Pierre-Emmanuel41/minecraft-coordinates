package fr.pederobien.minecraft.coordinates.commands;

import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraft.commandtree.impl.MinecraftCodeRootNode;
import fr.pederobien.minecraft.game.GamePlugin;
import fr.pederobien.minecraft.game.impl.DisplayHelper;
import fr.pederobien.minecraft.game.interfaces.IGame;
import fr.pederobien.minecraft.game.interfaces.ITeam;
import fr.pederobien.minecraft.game.interfaces.ITeamConfigurable;

public class CoordinatesNode extends MinecraftCodeRootNode {

	/**
	 * Creates a node in order to display the coordinates the player running this command.
	 */
	public CoordinatesNode() {
		super("coord", ECoordinatesMessageCode.COORD__EXPLANATION, () -> GamePlugin.getGameTree().getGame() != null);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return emptyList();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			send(eventBuilder(sender, ECoordinatesMessageCode.COORD__CANNOT_DISPLAY_PLAYER_COORDINATES).build());
			return false;
		}

		IGame game = GamePlugin.getGameTree().getGame();
		Player player = (Player) sender;
		Location loc = player.getLocation();
		Location center = player.getWorld().getWorldBorder().getCenter();
		Location display = new Location(player.getWorld(), loc.getBlockX() - center.getBlockX(), loc.getBlockY(), loc.getBlockZ() - center.getBlockZ());
		String playerCoords = DisplayHelper.toString(display, false, true);

		if (!(game instanceof ITeamConfigurable)) {
			sendSuccessful(sender, ECoordinatesMessageCode.COORD__PLAYER_TO_ITSELF_COORD, playerCoords);
			return true;
		}

		Optional<ITeam> optTeam = ((ITeamConfigurable) game).getTeams().getTeam(player);
		if (!optTeam.isPresent()) {
			sendSuccessful(sender, ECoordinatesMessageCode.COORD__PLAYER_TO_ITSELF_COORD, playerCoords);
			return true;
		}

		optTeam.get().sendMessage(player, ECoordinatesMessageCode.COORD__PLAYER_TO_TEAMMATES_COORD, playerCoords);
		return true;
	}
}

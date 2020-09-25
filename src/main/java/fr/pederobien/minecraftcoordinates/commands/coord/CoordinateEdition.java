package fr.pederobien.minecraftcoordinates.commands.coord;

import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraftchat.ChatPlugin;
import fr.pederobien.minecraftchat.interfaces.IChat;
import fr.pederobien.minecraftchat.interfaces.IChatConfiguration;
import fr.pederobien.minecraftcoordinates.commands.ECoordinatesMessageCode;
import fr.pederobien.minecraftdevelopmenttoolkit.utils.DisplayHelper;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;
import fr.pederobien.minecraftgameplateform.interfaces.element.IGameConfiguration;
import fr.pederobien.minecraftgameplateform.interfaces.element.ITeam;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftmanagers.MessageManager;
import fr.pederobien.minecraftmanagers.PlayerManager;

public class CoordinateEdition extends AbstractSimpleMapEdition {
	private String pattern;

	public CoordinateEdition() {
		super("coord", ECoordinatesMessageCode.COORDINATES__EXPLANATION);
		pattern = "<%s>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;

		// Getting current chat configuration
		IChatConfiguration chatConfiguration = ChatPlugin.getCurrentChatConfiguration();
		if (chatConfiguration != null) {
			List<IChat> chats = chatConfiguration.getChats(player);
			if (!chats.isEmpty()) {
				for (IChat chat : chatConfiguration.getChats(player))
					chat.sendMessage(player, ECoordinatesMessageCode.COORDINATES__PLAYER_COORDS, getLoc(player));
				return true;
			}
		}

		// Getting current game configuration
		IGameConfiguration gameConfiguration = Plateform.getGameConfigurationContext().getGameConfiguration();
		if (gameConfiguration != null) {
			// Getting player's team
			Optional<ITeam> optTeam = Plateform.getOrCreateConfigurationHelper(gameConfiguration).getTeam(player);
			if (optTeam.isPresent()) {
				optTeam.get().sendMessage(player, ECoordinatesMessageCode.COORDINATES__PLAYER_COORDS, getLoc(player));
				return true;
			}
		}

		// Send the message to everyone
		PlayerManager.getPlayers().forEach(p -> {
			MessageManager.sendMessage(p, getPrefix(player) + getMessage(player, ECoordinatesMessageCode.COORDINATES__PLAYER_COORDS, getLoc(player)));
		});
		return true;
	}

	private String getLoc(Player player) {
		Location loc = player.getLocation();
		Location center = player.getWorld().getWorldBorder().getCenter();
		Location display = new Location(player.getWorld(), loc.getBlockX() - center.getBlockX(), loc.getBlockY(), loc.getBlockZ() - center.getBlockZ());
		return DisplayHelper.toString(display, false, true);
	}

	private String getPrefix(Player player) {
		return String.format(pattern, player.getName());
	}
}

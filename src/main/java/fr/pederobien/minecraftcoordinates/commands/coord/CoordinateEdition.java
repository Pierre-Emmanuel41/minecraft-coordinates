package fr.pederobien.minecraftcoordinates.commands.coord;

import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraftchat.ChatPlugin;
import fr.pederobien.minecraftchat.interfaces.IChat;
import fr.pederobien.minecraftchat.interfaces.IChatConfiguration;
import fr.pederobien.minecraftdevelopmenttoolkit.utils.DisplayHelper;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractSimpleMapEdition;
import fr.pederobien.minecraftgameplateform.interfaces.element.IGameConfiguration;
import fr.pederobien.minecraftgameplateform.interfaces.element.ITeam;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecraftmanagers.MessageManager;
import fr.pederobien.minecraftmanagers.PlayerManager;

public class CoordinateEdition extends AbstractSimpleMapEdition {

	public CoordinateEdition() {
		super("coord", ECoordinatesMessageCode.COORDINATES__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;

		// Getting current chat configuration
		IChatConfiguration chatConfiguration = ChatPlugin.getCurrentConfiguration();
		if (chatConfiguration != null) {
			List<IChat> chats = chatConfiguration.getChats(player);
			if (!chats.isEmpty()) {
				for (IChat chat : chatConfiguration.getChats(player))
					chat.sendMessage(player, getPlayerCoordinates(player));
				return true;
			}
		}

		// Getting current game configuration
		IGameConfiguration gameConfiguration = Plateform.getGameConfigurationContext().getGameConfiguration();
		if (gameConfiguration != null) {
			// Getting player's team
			Optional<ITeam> optTeam = Plateform.getOrCreateConfigurationHelper(gameConfiguration).getTeam(player);
			if (optTeam.isPresent()) {
				optTeam.get().sendMessage(player, getPlayerCoordinates(player));
				return true;
			}
		}

		// Send the message to everyone
		MessageManager.sendMessage(PlayerManager.getPlayers(), "<" + player.getName() + ">" + getPlayerCoordinates(player));
		return true;
	}

	private String getPlayerCoordinates(Player player) {
		return getMessage(player, ECoordinatesMessageCode.COORDINATES__PLAYER_COORDS, DisplayHelper.toString(player.getLocation(), false, true));
	}
}

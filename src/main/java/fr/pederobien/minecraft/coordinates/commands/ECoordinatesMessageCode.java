package fr.pederobien.minecraft.coordinates.commands;

import fr.pederobien.minecraft.dictionary.impl.PlayerGroup;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.dictionary.interfaces.IPlayerGroup;

public enum ECoordinatesMessageCode implements IMinecraftCode {
	// Code for the "coord" command ---------------------------------------------------------------
	COORD__EXPLANATION,

	// Code when the command is running from the server console
	COORD__CANNOT_DISPLAY_PLAYER_COORDINATES,

	// Code when displaying the coordinate of a player to itself
	COORD__PLAYER_TO_ITSELF_COORD,

	// Code when displaying the coordinate of a player to its team mates.
	COORD__PLAYER_TO_TEAMMATES_COORD,

	// Code for the "gps" command -----------------------------------------------------------------
	GPS__EXPLANATION,

	// Code when the command is running from the server console
	GPS__CANNOT_DISPLAY_PLAYER_COORDINATES,

	// Code for the "gps add" command -------------------------------------------------------------
	GPS__ADD__EXPLANATION,

	// Code for the destination name completion
	GPS__DESTINATION_NAME__COMPLETION,

	// Code when the destination name is missing
	GPS__ADD__DESTINATION_IS_MISSING,

	// Code when the X coordinate is missing
	GPS__ADD__X_COORDINATE_IS_MISSING,

	// Code when the X coordinate has a bad format
	GPS__ADD__X_COORDINATE_BAD_FORMAT,

	// Code when the Z coordinate is missing
	GPS__ADD__Z_COORDINATE_IS_MISSING,

	// Code when the Z coordinate has a bad format
	GPS__ADD__Z_COORDINATE_BAD_FORMAT,

	// Code when there is no running game
	GPS__ADD__NO_RUNNING_GAME,

	// Code when a GPS is already registered for a destination
	GPS__ADD__GPS_ALREADY_REGISTERED,

	// Code when a new GPS has been created
	GPS__ADD__GPS_CREATED,

	// Code when the player arrived at the GPS destination.
	GPS__PLAYER_ARRIVED,

	// Code for the "gps remove" command ----------------------------------------------------------
	GPS__REMOVE__EXPLANATION,

	// Code when the GPS does not exist
	GPS__REMOVE__GPS_NOT_FOUND,

	// Code when there is no GPS to remove
	GPS__REMOVE__NO_GPS_TO_REMOVE,

	// Code when there is one GPS to remove
	GPS__REMOVE__ONE_GPS_TO_REMOVE,

	// Code when there is several GPS to remove
	GPS__REMOVE__SEVERAL_GPS_TO_REMOVE;

	private IPlayerGroup group;

	private ECoordinatesMessageCode() {
		this(PlayerGroup.OPERATORS);
	}

	private ECoordinatesMessageCode(IPlayerGroup group) {
		this.group = group;
	}

	@Override
	public String value() {
		return name();
	}

	@Override
	public IPlayerGroup getGroup() {
		return group;
	}

	@Override
	public void setGroup(IPlayerGroup group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return String.format("value=%s,group=%s", value(), getGroup());
	}
}

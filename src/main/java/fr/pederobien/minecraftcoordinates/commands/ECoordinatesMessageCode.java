package fr.pederobien.minecraftcoordinates.commands;

import fr.pederobien.minecraftdictionary.impl.Permission;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;

public enum ECoordinatesMessageCode implements IMinecraftMessageCode {
	// Code for command coord
	COORDINATES__EXPLANATION, COORDINATES__PLAYER_COORDS,

	// Code for command gps
	GPS__EXPLANATION, GPS__NO_GAME_IS_RUNNING,

	// Code for command new
	NEW_GPS__EXPLANATION, NEW_GPS__GPS_ALREADY_REGISTERED, NEW_GPS__SCOREBOARD_DISPLAY, NEW_GPS__GPS_STARTED(Permission.SENDER),
	NEW_GPS__PLAYER_ARRIVED(Permission.SENDER),

	// Code for command stop
	STOP_GPS__EXPLANATION, STOP_GPS__GPS_NOT_FOUND, STOP_GPS__NO_GPS_TO_STOP, STOP_GPS__ONE_GPS_TO_STOP, STOP_GPS__SEVERAL_GPS_TO_STOP;

	private Permission permission;

	private ECoordinatesMessageCode() {
		this(Permission.OPERATORS);
	}

	private ECoordinatesMessageCode(Permission permission) {
		this.permission = permission;
	}

	@Override
	public String value() {
		return toString();
	}

	@Override
	public Permission getPermission() {
		return permission;
	}

	@Override
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}

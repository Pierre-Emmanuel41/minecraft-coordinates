package fr.pederobien.minecraftcoordinates.commands;

import fr.pederobien.minecraftdictionary.impl.Permission;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;

public enum ECoordinatesMessageCode implements IMinecraftMessageCode {
	// Code for command coord
	COORDINATES__EXPLANATION, COORDINATES__PLAYER_COORDS;

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

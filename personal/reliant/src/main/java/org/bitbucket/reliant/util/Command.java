package org.bitbucket.reliant.util;

public final class Command {
	public enum Type {
		BROWSE("browse"), EDIT("edit"), MAIL("mail"), OPEN("open"), PRINT("print"), CHEAT("cheat"), NATIVE("native"), GAME("game");
		
		public static Type typeByKey(final String key) {
			for (final Type value : values()) {
				if (key.equalsIgnoreCase(value.key)) {
					return value;
				}
			}
			return null;
		}
		
		private final String key;
		
		private Type(final String key) {
			this.key = key;
		}
	}
	public final Type type;
	public final String data;
	
	public Command(final Type type, final String data) {
		this.type = type;
		this.data = data;
	}
}

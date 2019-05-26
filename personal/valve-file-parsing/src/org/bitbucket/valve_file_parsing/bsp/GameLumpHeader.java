package org.bitbucket.valve_file_parsing.bsp;

public class GameLumpHeader {
	public final int lumpCount;
	public final GameLump[] gameLump;
	
	public GameLumpHeader(final int lumpCount, final GameLump[] gameLump) {
		this.lumpCount = lumpCount;
		this.gameLump = gameLump;
	}
}

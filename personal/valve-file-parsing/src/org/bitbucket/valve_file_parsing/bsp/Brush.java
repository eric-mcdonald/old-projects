package org.bitbucket.valve_file_parsing.bsp;

public class Brush {
	public static final int CONTENTS_EMPTY = 0; // No contents
	public static final int CONTENTS_SOLID = 0x1; // an eye is never valid in a solid
	public static final int CONTENTS_WINDOW = 0x2; // translucent, but not watery (glass)
	public static final int CONTENTS_AUX = 0x4; // 
	public static final int CONTENTS_GRATE = 0x8; // alpha-tested "grate" textures. Bullets/sight pass through, but solids don't
	public static final int CONTENTS_SLIME = 0x10; // 
	public static final int CONTENTS_WATER = 0x20; // 
	public static final int CONTENTS_MIST = 0x40; // 
	public static final int CONTENTS_OPAQUE = 0x80; // block AI line of sight
	public static final int CONTENTS_TESTFOGVOLUME = 0x100; // things that cannot be seen through (may be non-solid though)
	public static final int CONTENTS_UNUSED = 0x200; // unused
	public static final int CONTENTS_UNUSED6 = 0x400; // unused
	public static final int CONTENTS_TEAM1 = 0x800; // per team contents used to differentiate collisions between players and objects on different teams
	public static final int CONTENTS_TEAM2 = 0x1000;
	public static final int CONTENTS_IGNORE_NODRAW_OPAQUE = 0x2000; // ignore public static final int CONTENTS_OPAQUE on surfaces that have SURF_NODRAW
	public static final int CONTENTS_MOVEABLE = 0x4000; // hits entities which are MOVETYPE_PUSH (doors, plats, etc.)
	public static final int CONTENTS_AREAPORTAL = 0x8000; // remaining contents are non-visible, and don't eat brushes
	public static final int CONTENTS_PLAYERCLIP = 0x10000; // 
	public static final int CONTENTS_MONSTERCLIP = 0x20000; // 
	public static final int CONTENTS_CURRENT_0 = 0x40000; // currents can be added to any other contents, and may be mixed
	public static final int CONTENTS_CURRENT_90 = 0x80000;
	public static final int CONTENTS_CURRENT_180 = 0x100000;
	public static final int CONTENTS_CURRENT_270 = 0x200000;
	public static final int CONTENTS_CURRENT_UP = 0x400000;
	public static final int CONTENTS_CURRENT_DOWN = 0x800000;
	public static final int CONTENTS_ORIGIN = 0x1000000; // removed before bsping an entity
	public static final int CONTENTS_MONSTER = 0x2000000; // should never be on a brush, only in game
	public static final int CONTENTS_DEBRIS = 0x4000000; // 
	public static final int CONTENTS_DETAIL = 0x8000000; // brushes to be added after vis leafs
	public static final int CONTENTS_TRANSLUCENT = 0x10000000; // auto set if any surface has trans
	public static final int CONTENTS_LADDER = 0x20000000; // 
	public static final int CONTENTS_HITBOX = 0x40000000;
	public final int firstSide, numSides;
	public final int contents;

	public Brush(final int firstSide, final int numSides, final int contents) {
		this.firstSide = firstSide;
		this.numSides = numSides;
		this.contents = contents;
	}
}

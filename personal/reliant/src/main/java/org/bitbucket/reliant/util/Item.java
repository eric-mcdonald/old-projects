package org.bitbucket.reliant.util;

import java.util.HashMap;
import java.util.Map;

public enum Item {
	GOLDEN_KNIFE("Golden Knife", 0, true), DESERT_EAGLE("Desert Eagle", 1, false), DUAL_BERETTAS("Dual Berettas", 2, false), FIVESEVEN("Five-SeveN", 3, false), GLOCK("Glock-18", 4, false), AK47("AK-47", 7, true), AUG("AUG", 8, true), AWP("AWP", 9, false), FAMAS("FAMAS", 10, true), G3SG1("G3SG1", 11, true), GALILAR("Galil AR", 13, true), M249("M249", 14, true), M4A4("M4A4", 16, true), MAC10("MAC-10", 17, true), P90("P90", 19, true), UMP45("UMP-45", 24, true), XM1014("XM1014", 25, true), PPBIZON("PP-Bizon", 26, true), MAG7("MAG-7", 27, false), NEGEV("Negev", 28, true), SAWEDOFF("Sawed-Off", 29, false), TEC9("Tec-9", 30, false), ZEUS_X27("Zeus x27", 31, false), P2000("P2000", 32, false), MP7("MP7", 33, true), MP9("MP9", 34, true), NOVA("Nova", 35, false), P250("P250", 36, false), SCAR20("SCAR-20", 38, true), SG_553("SG 553", 39, true), SSG_08("SSG 08", 40, false), KNIFE("Knife", 41, true), KNIFE2("Knife", 42, true), FLASHBANG("Flashbang", 43, false), HE_GRENADE("High Explosive Grenade", 44, false), SMOKE_GRENADE("Smoke Grenade", 45, false), MOLOTOV("Molotov", 46, false), DECOY_GRENADE("Decoy Grenade", 47, false), INCENDIARY_GRENADE("Incendiary Grenade", 48, false), C4("C4", 49, false), TERRORIST_KNIFE("Knife", 59, true), M4A1S("M4A1-S", 60, true), USPS("USP-S", 61, false), CZ75AUTO("CZ75-Auto", 63, true), R8_REVOLVER("R8 Revolver", 64, true), BAYONET("Bayonet", 500, true), FLIP_KNIFE("Flip Knife", 505, true), GUT_KNIFE("Gut Knife", 506, true), KARAMBIT("Karambit", 507, true), M9_BAYONET("M9 Bayonet", 508, true), HUNTSMAN_KNIFE("Huntsman Knife", 509, true), FALCHION_KNIFE("Falchion Knife", 512, true), BOWIE_KNIFE("Bowie Knife", 514, true), BUTTERFLY_KNIFE("Butterfly Knife", 515, true), SHADOW_DAGGERS("Shadow Daggers", 516, true);

	private static final Map<Integer, Item> itemMap = new HashMap<Integer, Item>();
	static {
		for (final Item item : values()) {
			itemMap.put(item.index, item);
		}
	}
	public static boolean automatic(final int itemIdx) {
		return itemMap.containsKey(itemIdx) && itemMap.get(itemIdx).automatic;
	}

	public static boolean boltAction(final int itemIdx) {
		return itemIdx == AWP.index || itemIdx == SSG_08.index;
	}
	
	public static boolean grenade(final int itemIdx) {
		return itemIdx == FLASHBANG.index || itemIdx == HE_GRENADE.index || itemIdx == SMOKE_GRENADE.index || itemIdx == MOLOTOV.index || itemIdx == DECOY_GRENADE.index || itemIdx == INCENDIARY_GRENADE.index;
	}

	public static boolean melee(final int itemIdx) {
		return itemIdx == GOLDEN_KNIFE.index || itemIdx == ZEUS_X27.index || itemIdx == KNIFE.index || itemIdx == KNIFE2.index || itemIdx == TERRORIST_KNIFE.index || itemIdx == BAYONET.index || itemIdx == FLIP_KNIFE.index || itemIdx == GUT_KNIFE.index || itemIdx == KARAMBIT.index || itemIdx == M9_BAYONET.index || itemIdx == HUNTSMAN_KNIFE.index || itemIdx == FALCHION_KNIFE.index || itemIdx == BOWIE_KNIFE.index || itemIdx == BUTTERFLY_KNIFE.index || itemIdx == SHADOW_DAGGERS.index;
	}
	
	public static long minDelay(final int itemIdx) {
		return itemMap.containsKey(itemIdx) && itemMap.get(itemIdx).automatic ? 0L : SdkUtils.TICK_TIME * 1;
	}
	
	public static String name(final int itemIdx) {
		return itemMap.containsKey(itemIdx) ? itemMap.get(itemIdx).name : null;
	}
	
	public static boolean scoped(final int itemIdx) {
		return itemIdx == AUG.index || itemIdx == AWP.index || itemIdx == G3SG1.index || itemIdx == SSG_08.index || itemIdx == SCAR20.index || itemIdx == SG_553.index;
	}
	
	public static boolean shotgun(final int itemIdx) {
		return itemIdx == XM1014.index || itemIdx == MAG7.index || itemIdx == SAWEDOFF.index || itemIdx == NOVA.index;
	}
	
	public static boolean sniper(final int itemIdx) {
		return scoped(itemIdx) && itemIdx != AUG.index && itemIdx != SG_553.index;
	}
	
	public final String name;
	public final int index;
	private final boolean automatic;
	
	private Item(final String name, final int index, final boolean automatic) {
		this.name = name;
		this.index = index;
		this.automatic = automatic;
	}
}

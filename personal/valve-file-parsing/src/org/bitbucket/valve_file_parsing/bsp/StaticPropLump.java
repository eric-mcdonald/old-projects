package org.bitbucket.valve_file_parsing.bsp;

import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.valve_file_parsing.math.Color32;

public class StaticPropLump {
	// v4
	public final Vector		origin;		 // origin
	public final float[]		angles;		 // orientation (pitch roll yaw)
	public final char	propType;	 // index into model name dictionary
	public final char	firstLeaf;	 // index into leaf array
	public final char	leafCount;
	public final byte	solid;		 // solidity type
	public final byte	flags;
	public final int		skin;		 // model skin numbers
	public final float		fadeMinDist;
	public final float		fadeMaxDist;
	public final Vector		lightingOrigin;  // for lighting
	// since v5
	public float		forcedFadeScale; // fade distance scale
	// v6 and v7 only
	public char  minDXLevel;      // minimum DirectX version to be visible
	public char  maxDXLevel;      // maximum DirectX version to be visible
	// since v8
	public byte   minCPULevel;
	public byte   maxCPULevel;
	public byte   minGPULevel;
	public byte   maxGPULevel;
	// since v7
	public Color32         diffuseModulation; // per instance color and alpha modulation
	// since v10
	public float           unknown; 
	// since v9
	public boolean            disableX360;     // if true, don't show on XBox 360
	
	public StaticPropLump(final Vector origin, final float[] angles, final char propType, final char firstLeaf, final char leafCount, final byte solid, final byte flags, final int skin, final float fadeMinDist, final float fadeMaxDist, final Vector lightingOrigin) {
		this.origin = origin;
		this.angles = angles;
		this.propType = propType;
		this.firstLeaf = firstLeaf;
		this.leafCount = leafCount;
		this.solid = solid;
		this.flags = flags;
		this.skin = skin;
		this.fadeMinDist = fadeMinDist;
		this.fadeMaxDist = fadeMaxDist;
		this.lightingOrigin = lightingOrigin;
	}
}

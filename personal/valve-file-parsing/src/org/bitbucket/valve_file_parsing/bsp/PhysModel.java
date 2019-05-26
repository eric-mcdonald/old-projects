package org.bitbucket.valve_file_parsing.bsp;

public class PhysModel {
	public final int modelIndex;  // Perhaps the index of the model to which this physics model applies?
    public final int dataSize;    // Total size of the collision data sections
    public final int keydataSize; // Size of the text section
    public final int solidCount;  // Number of collision data sections
    
    public PhysModel(final int modelIndex, final int dataSize, final int keydataSize, final int solidCount) {
    	this.modelIndex = modelIndex;
    	this.dataSize = dataSize;
    	this.keydataSize = keydataSize;
    	this.solidCount = solidCount;
    }
}

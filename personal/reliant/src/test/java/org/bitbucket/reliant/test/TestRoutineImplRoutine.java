package org.bitbucket.reliant.test;

import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.GameCache.ParseMapThread;
import org.bitbucket.reliant.GameCache.ParseMapThread.MdlBox;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.routine.BaseRoutine;

public final class TestRoutineImplRoutine extends BaseRoutine {
	public TestRoutineImplRoutine() {
		super("test_routine_impl", "Tests that the routine system is working.", true, 4002, new BoolOption("Test Boolean", "Tests that the boolean option class is working.", false), new FloatOption("Test Float", "Tests that the float option class is working.", new ClampedNumber<Float>(5.0F, 0.0F, 10.0F), 0.1F), new IntOption("Test Int", "Tests that the int option class is working.", new ClampedNumber<Integer>(5, 0, 10), 1));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post || GameCache.getMapDir() == null) {
			return;
		}
		final ParseMapThread mapParser = GameCache.getMapParser(GameCache.getMapDir());
		if (mapParser == null || mapParser.isAlive()) {
			return;
		}
		outer_loop:
			for (final MdlBox mdlBox : mapParser.mdlBoxes) {
				final Vector minsVec = mdlBox.mins, maxsVec = mdlBox.maxs;
				final float[][] screenPos = new float[][] {Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, minsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, maxsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, minsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, maxsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, maxsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, maxsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, minsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, minsVec.z, maxsVec.y})};
				for (final float[] pos : screenPos) {
					if (pos == null || pos[3] < org.bitbucket.eric_generic.math.MathHelper.EQUALS_PRECISION) {
						continue outer_loop;
					}
				}
				float left = screenPos[0][0], top = screenPos[0][1], right = screenPos[0][0], bottom = screenPos[0][1];
				for (int posIdx = 1; posIdx < screenPos.length; posIdx++) {
					if (left > screenPos[posIdx][0]) {
						left = screenPos[posIdx][0];
					}
					if (top > screenPos[posIdx][1]) {
						top = screenPos[posIdx][1];
					}
					if (right < screenPos[posIdx][0]) {
						right = screenPos[posIdx][0];
					}
					if (bottom < screenPos[posIdx][1]) {
						bottom = screenPos[posIdx][1];
					}
				}
				Reliant.instance.getRenderer().drawOutline(left, top, right - left, bottom - top, 0xFFFF00FF, 1.0F);
			}
	}
}

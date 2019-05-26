package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.memory.WriteMemoryException;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class BunnyhopRoutine extends DisplayRoutine {
	private boolean executed;
	private final Timer jumpTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			/*long retVal = getInt("Delay");
			if (Reliant.instance.antiOverwatch()) {
				retVal += Reliant.instance.getOverwatchRand().nextInt(Reliant.OVERWATCH_MAX_TIME);
			}*/
			return /*retVal*/getInt("Delay");
		}
		
	};
	private final Timer missTimer = new BasicTimer(SdkUtils.TICK_TIME * 1); // Eric: Delay to wait before compensating for a possibly missed hop
	
	public BunnyhopRoutine() {
		super("Bunny-hop", "Allows the player to jump consecutively.", true, true, true, 0x20, 5, new IntOption("Delay", "Specifies how long to wait before jumping again.", new ClampedNumber<Integer>(0, 0, 1000), 100), new FloatOption("Minimum Velocity", "Specifies the minimum horizontal velocity required to bunny-hop.", new ClampedNumber<Float>(0.0F, 0.0F, 1000.0F), 100.0F));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFFFF00;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		if (executed) {
			final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
			if (clientAddr != MemoryStream.NULL) {
				Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceJump"), MemoryStream.FALSE);
			}
			executed = false;
		}
		jumpTimer.reset();
		missTimer.reset();
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			reset(false);
			return;
		}
		float[] vecVelocity = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecVelocity.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecVelocity[0]"), MemoryStream.FLOAT_SZ * vecVelocity.length)).rewind()).asFloatBuffer().get(vecVelocity);
		final boolean onGround = (Reliant.instance.getProcessStream().readByte(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_fFlags")) & SdkUtils.ON_GROUND_FLAG) != 0;
		if (Math.sqrt(vecVelocity[0] * vecVelocity[0] + vecVelocity[1] * vecVelocity[1]) >= getFloat("Minimum Velocity") && onGround && !executed && jumpTimer.delayPassed()) {
			if (!Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceJump"), MemoryStream.TRUE)) {
				throw new WriteMemoryException("force_jump", MemoryStream.TRUE, clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceJump"));
			}
			jumpTimer.setStartTime();
			missTimer.setStartTime();
			executed = true;
		} else if ((!onGround || missTimer.delayPassed()) && executed) {
			if (!Reliant.instance.getProcessStream().write(clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceJump"), MemoryStream.FALSE)) {
				throw new WriteMemoryException("force_jump", MemoryStream.FALSE, clientAddr + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwForceJump"));
			}
			executed = false;
		}
	}
}

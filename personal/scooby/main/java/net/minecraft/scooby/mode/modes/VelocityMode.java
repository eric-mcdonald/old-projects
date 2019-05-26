package net.minecraft.scooby.mode.modes;

import java.util.Random;

import net.minecraft.scooby.Scooby;
import net.minecraft.scooby.command.commands.VelocityCommand;
import net.minecraft.scooby.mode.Mode;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import org.lwjgl.input.Keyboard;

/**
 * Velocity makes you take less velocity, you dingus. Key bind to enable it is the 'V' key.
 * @author pootPoot
 * @since brudin started ignoring my pull request... :'C
 */
public class VelocityMode extends Mode {

	private float prevHealth = -999.0F; // Because Minecraft Forge is dumb and doesn't have an Event for velocity being added to an Entity...
	private Random rand = new Random();
	public VelocityMode(Scooby scooby) {
		super(scooby, "Velocity", Keyboard.KEY_V);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		if (event instanceof LivingUpdateEvent && ((LivingUpdateEvent) event).entity.equals(scooby.mc.thePlayer)) {
			float currentHealth = scooby.mc.thePlayer.getHealth();
			if (prevHealth == -999.0F || currentHealth > prevHealth) {
				prevHealth = currentHealth;
			}
			else if (currentHealth < prevHealth && (scooby.mc.thePlayer.motionX != 0.0D || scooby.mc.thePlayer.motionY != 0.0D || scooby.mc.thePlayer.motionZ != 0.0D)) {
				VelocityCommand velocityCommand = (VelocityCommand) scooby.commandUtils.getCommandByName("-velocity");
				double maxFactor = velocityCommand.getMaxFactor(), randMultiplier = rand.nextDouble() * maxFactor, minFactor = velocityCommand.getMinFactor();
				while (randMultiplier < minFactor) {
					randMultiplier = rand.nextDouble() * maxFactor;
				}
				scooby.mc.thePlayer.motionX *= randMultiplier;
				scooby.mc.thePlayer.motionY *= randMultiplier;
				scooby.mc.thePlayer.motionZ *= randMultiplier;
				prevHealth = currentHealth;
			}
		}
		else if (event instanceof WorldEvent.Unload || (event instanceof PlayerEvent.Clone && ((PlayerEvent.Clone) event).wasDeath)) {
			prevHealth = -999.0F;
		}
	}

}

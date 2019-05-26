package org.bitbucket.lanius.core;

import org.bitbucket.lanius.Lanius;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public final class LaniusContainer extends DummyModContainer {
	public LaniusContainer() {
		super(new ModMetadata());
		final ModMetadata metadata = getMetadata();
		metadata.modId = Lanius.MODID + "_core";
		metadata.name = Lanius.NAME + " Core";
		metadata.version = "1.7";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}

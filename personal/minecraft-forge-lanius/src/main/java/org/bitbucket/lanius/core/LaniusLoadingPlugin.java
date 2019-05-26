package org.bitbucket.lanius.core;

import java.util.Map;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.util.registry.Registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = Lanius.NAME)
@IFMLLoadingPlugin.MCVersion(value = MinecraftForge.MC_VERSION)
@IFMLLoadingPlugin.TransformerExclusions(value = "org.bitbucket.lanius.core")
@IFMLLoadingPlugin.SortingIndex(value = 1001)
// Eric: 1001 means working with
// SRGs
public final class LaniusLoadingPlugin implements IFMLLoadingPlugin {

	static final Registry<String> nameRegistry = new Registry<String>() {
		private static final boolean USE_SRGS = true; // Eric: set this to
														// false
														// while developing
														// Lanius!

		@Override
		public final String get(final String name) {
			return USE_SRGS ? objMap.get(name) : name;
		}
	};

	public LaniusLoadingPlugin() {
		nameRegistry.register("renderWorldPass", "func_175068_a");
		nameRegistry.register("onLivingUpdate", "func_70636_d");
		nameRegistry.register("sprintToggleTimer", "field_71156_d");
		nameRegistry.register("moveStrafe", "field_78902_a");
		nameRegistry.register("moveForward", "field_192832_b");
		nameRegistry.register("movementInput", "field_71158_b");
		nameRegistry.register("player", "field_71439_g");
		nameRegistry.register("updatePlayerMoveState", "func_78898_a");
		nameRegistry.register("onEntityCollidedWithBlock", "func_180634_a");
		nameRegistry.register("SOUL_SAND_AABB", "field_185703_a");
		nameRegistry.register("SAND", "field_151595_p");
		nameRegistry.register("BROWN", "field_151650_B");
		nameRegistry.register("BUILDING_BLOCKS", "field_78030_b");
		nameRegistry.register("setCreativeTab", "func_149647_a");
		nameRegistry.register("getCollisionBoundingBox", "func_180646_a");
		nameRegistry.register("motionX", "field_70159_w");
		nameRegistry.register("motionZ", "field_70179_y");
		nameRegistry.register("renderItem", "func_180454_a");
		nameRegistry.register("renderModel", "func_191961_a");
		nameRegistry.register("doRender", "func_76986_a");
		nameRegistry.register("enableOutlineMode", "func_187431_e");
		nameRegistry.register("getTeamColor", "func_188298_c");
		nameRegistry.register("handleLoginSuccess", "func_147390_a");
		nameRegistry.register("launchIntegratedServer", "func_71371_a");
		nameRegistry.register("isItemValid", "func_75214_a");
		nameRegistry.register("getItem", "func_77973_b");
		nameRegistry.register("getSlotForItemStack", "func_184640_d");
		nameRegistry.register("val$entityequipmentslot", "val$entityequipmentslot");
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		// TODO Auto-generated method stub
		return new String[] { EntRenderTransformer.class.getName(), EntPlayerTransformer.class.getName(),
				SoulSandTransformer.class.getName(), RenderItemTransformer.class.getName(),
				RenderLivingTransformer.class.getName(), NetLoginTransformer.class.getName(),
				GuiConnectingTransformer.class.getName(), RealmsConnectTransformer.class.getName(),
				RenderEntityItemTransformer.class.getName(), ContainerPlayerTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return LaniusContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub

	}

}

package org.bitbucket.lanius.routine.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class CreativeDropRoutine extends TabbedRoutine implements Configurable {

	private static final File dropFile = new File(Lanius.dataDir, "creative_drop.cfg");

	private static final int MAX_INDEX;

	static {
		final Iterator<Item> itemIt = Item.REGISTRY.iterator();
		int maxIdx = 0;
		while (itemIt.hasNext()) {
			itemIt.next();
			++maxIdx;
		}
		MAX_INDEX = maxIdx - 1;
	}

	private int currentIdx;

	private final Map<String, Item> exemptMap = new HashMap<String, Item>();

	private ItemStack savedStack;

	private boolean stackSaved;

	private long startTime;

	public CreativeDropRoutine() {
		super(Keyboard.KEY_O, false, Tab.WORLD);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Drops stacks of every item in the item registry.";
	}

	@Override
	public void init() {
		setCurrentIdx();
		if (stackSaved && Lanius.mc.player != null && Lanius.mc.player.capabilities.isCreativeMode) {
			InventoryUtils.putStackInHotbar(savedStack);
		}
		savedStack = null;
		stackSaved = false;
		startTime = 0L;
	}

	@Override
	public void load() {
		exemptMap.clear();
		if (!dropFile.exists()) {
			return;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(dropFile));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					putExempt(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Creative Drop";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null
				|| !Lanius.mc.player.capabilities.isCreativeMode
				|| System.currentTimeMillis() - startTime <= getInt("Delay").intValue() || Lanius.mc.isGamePaused()) {
			return;
		}
		if (!stackSaved) {
			savedStack = Lanius.mc.player.inventoryContainer.getSlot(InventoryUtils.HOTBAR_BEGIN).getStack();
			stackSaved = true;
		}
		Iterator<Item> itemIt = Item.REGISTRY.iterator();
		int index, base;
		boolean hasBase = false;
		for (index = 0, base = 0; itemIt.hasNext();) {
			if (!exemptMap.containsValue(itemIt.next())) {
				if (index < MAX_INDEX) {
					index++;
				}
				if (!hasBase) {
					hasBase = true;
				}
			} else if (!hasBase) {
				base++;
			}
		}
		if (currentIdx >= base + index) {
			setCurrentIdx();
		}
		itemIt = Item.REGISTRY.iterator();
		for (index = 0; itemIt.hasNext(); index++) {
			final ItemStack stack = new ItemStack(itemIt.next());
			stack.setCount(getBoolean("Legitimate Stacks") ? stack.getMaxStackSize() : 64);
			final Item item = stack.getItem();
			if (index <= currentIdx || exemptMap.containsValue(item)) {
				continue;
			}
			if (getBoolean("NaN")) {
				InventoryUtils.addAllNanAttributes(stack);
			}
			InventoryUtils.putStackInHotbar(stack);
			InventoryUtils.clickWindow(InventoryUtils.HOTBAR_BEGIN, 1, ClickType.THROW);
			currentIdx = index;
			break;
		}
		startTime = System.currentTimeMillis();
	}

	public Item putExempt(final String itemName) {
		final Item item = Item.getByNameOrId(itemName);
		if (exemptMap.containsValue(item)) {
			final Item exemptItem = exemptMap.get(itemName);
			return exemptItem == null ? item : exemptItem;
		}
		return exemptMap.put(itemName, item);
	}

	@Override
	public void registerValues() {
		registerValue("Delay", 0, 0, 1000, "Specifies the time to wait before dropping the next stack of items.");
		registerValue("NaN", false, "Determines whether or not to drop items with NaN movement speed.");
		registerValue("Legitimate Stacks", false,
				"Determines whether or not to drop items with legitimate stack sizes.");
	}

	public Item removeExempt(final String itemName) {
		return exemptMap.remove(itemName);
	}

	@Override
	public void save() {
		dropFile.delete();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(dropFile))));
			for (final String itemName : exemptMap.keySet()) {
				out.println(itemName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	private void setCurrentIdx() {
		currentIdx = -1;
	}

}

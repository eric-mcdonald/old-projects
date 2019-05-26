import java.lang.reflect.Modifier;



// import java.lang.reflect.Modifier;

// import com.gunrun66.waffen.util.ReflectionHelper;
// import com.runescape.client.src.Ground;
// import com.runescape.client.src.Object4;
// import com.runescape.client.src.Object5;
// import com.runescape.client.src.ObjectDef;
// import com.runescape.client.src.WorldController;
// import com.runescape.client.src.Stream;

public class BankCommand extends CommandBase {

	public BankCommand() {
		super("Bank", "Stores X amount of item in your bank", "bank [slot] [amount]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String command) {
		// TODO Auto-generated method stub
		String[] splitCommand = command.split(" ");
		Stream stream = null;
		try {
			stream = (Stream) ReflectionHelper.findField(client.class, Modifier.PRIVATE, Stream.class, "stream").get(client.uberClient);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* if (splitCommand.length < 4) {
			int id = 0, x = 0, y = 0;
			Ground[][][] groundArray = null;
			try {
				groundArray = (Ground[][][]) ReflectionHelper.findField(WorldController.class, Modifier.PRIVATE | Modifier.FINAL, Ground[][][].class, "groundArray").get(ReflectionHelper.findField(client.class, Modifier.PRIVATE, WorldController.class, "worldController").get(client.uberClient));
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			label:
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 104; j++) {
						for (int k = 0; k < 104; k++) {
							Ground g = groundArray[i][j][k];
							if (g != null) {
								int l = -1;
								for (Object5 o51 : g.obj5Array) {
									if (o51 != null) {
										l = o51.uid >> 14 & 32767;
										if (ObjectDef.forID(l).name != null && ObjectDef.forID(l).name.equals("Bank booth")) {
											id = l;
											x = l & 127;
											y = l >> 7 & 127;
											break label;
										}
									}
								}
								try {
									int m = g.obj1 != null ? g.obj1.uid : g.obj2 != null ? g.obj2.uid : g.obj3 != null ? g.obj3.uid : -1;
									if (m == -1 && g.obj4 != null) {
										m = ReflectionHelper.findField(Object4.class, 0, Integer.TYPE, "uid").getInt(g.obj4);
									}
									l = m >> 14 & 32767;
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (l != 32767 && ObjectDef.forID(l).name != null && ObjectDef.forID(l).name.equals("Bank booth")) {
									id = l;
									x = l & 127;
									y = l >> 7 & 127;
									break label;
								}
							}
						}
					}
				}
			if (id == 0 && x == 0 && y == 0) {
				CommandUtils.pushCommandMessage(this.getName() + ": could not find a bank booth", CommandMessageTypeEnum.ERROR);
				return;
			}
			else {
				int baseX = 0, baseY = 0;
				try {
					baseX = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseX").getInt(client.uberClient);
					baseY = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseY").getInt(client.uberClient);
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				x += baseX;
				y += baseY;
				client.uberClient.stream.createFrame(98);
	            client.uberClient.stream.writeWordBigEndian(5);
	            client.uberClient.stream.method433(x);
	            client.uberClient.stream.method431(y);
				client.uberClient.stream.method424(0);
				client.uberClient.stream.createFrame(132);
				client.uberClient.stream.method433(x);
				client.uberClient.stream.writeWord(id);
				client.uberClient.stream.method432(y);
			} */
		if (splitCommand.length == 1) {
			for (int i = 0; i < 28; i++) {
				int j = RSInterface.interfaceCache[3214].inv /* inventory */[i] - 1;
				if (j == -1) {
					continue;
				}
				/* client.uberClient. */stream.createFrame(129);
				/* client.uberClient. */stream.method432(i);
				/* client.uberClient. */stream.writeWord(5064);
				/* client.uberClient. */stream.method432(j);
			}
			CommandUtils.pushCommandMessage(this.getName() + ": banked all of the player's items", CommandMessageTypeEnum.INFO);
		}
		else if (splitCommand.length == 2 || splitCommand.length == 3) {
			int slot;
			try {
				slot = Integer.parseInt(splitCommand[1]);
				if (slot < 0 || slot > 27) {
					CommandUtils.pushCommandMessage(this.getName() + ": changing slot from " + slot + " to " + (slot = slot < 0 ? 0 : 27), CommandMessageTypeEnum.WARNING);
				}
			}
			catch (NumberFormatException e) {
				CommandUtils.pushCommandMessage(this.getName() + ": usage: " + this.getUsage(), CommandMessageTypeEnum.ERROR);
				return;
			}
			int itemID = RSInterface.interfaceCache[3214].inv /* inventory */[slot] - 1;
			if (splitCommand.length == 2) {
				if (itemID != -1) {
					/* client.uberClient. */stream.createFrame(129);
					/* client.uberClient. */stream.method432(slot);
					/* client.uberClient. */stream.writeWord(5064);
					/* client.uberClient. */stream.method432(itemID);
				}
				CommandUtils.pushCommandMessage(this.getName() + ": banked all of the item in slot " + slot, CommandMessageTypeEnum.INFO);
			}
			else {
				int amount;
				try {
					amount = Integer.parseInt(splitCommand[2]);
					if (amount < 1) {
						CommandUtils.pushCommandMessage(this.getName() + ": changing amount from " + amount + " to " + (amount = 1), CommandMessageTypeEnum.WARNING);
					}
				}
				catch (NumberFormatException e) {
					CommandUtils.pushCommandMessage(this.getName() + ": usage: " + this.getUsage(), CommandMessageTypeEnum.ERROR);
					return;
				}
				if (itemID != -1) {
					/* client.uberClient. */stream.createFrame(135);
					/* client.uberClient. */stream.method431(slot);
					/* client.uberClient. */stream.method432(5064);
					/* client.uberClient. */stream.method431(itemID);
					/* client.uberClient. */stream.createFrame(208);
					/* client.uberClient. */stream.writeDWord(amount);
				}
				CommandUtils.pushCommandMessage(this.getName() + ": banked " + amount + " of the item in slot " + slot, CommandMessageTypeEnum.INFO);
			}
		}
		// }
	}

}

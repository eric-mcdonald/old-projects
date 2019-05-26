import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public class KeepItemsModule extends ModuleBase {

	/* private List<Integer> inventoryItems = new ArrayList<Integer>(), equippedItems = new ArrayList<Integer>();
	private boolean hasAddedItems; */
	public KeepItemsModule() {
		super("Keep Items", "Stores the player's items on death", ModuleTypeEnum.COMBAT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		/* this.inventoryItems.clear();
		this.equippedItems.clear();
		this.hasAddedItems = false; */
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (client.myPlayer.loopCycleStatus > client.loopCycle && (double) client.myPlayer.currentHealth / (double) client.myPlayer.maxHealth * 100.0D == 0.0D) {
			Stream stream = null;
			try {
				stream = (Stream) ReflectionHelper.findField(client.class, Modifier.PRIVATE, Stream.class, "stream").get(client.uberClient);
			} catch (IllegalArgumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IllegalAccessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			/* if (RSInterface.interfaceCache[199].message.contains("Level: ")) {
				if (this.hasAddedItems) {
					int x = 0, y = 0;
					try {
						x = (ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseX").getInt(client.uberClient) + (client.myPlayer.x - 6 >> 7)) - ((ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "anInt1069").getInt(client.uberClient) - 6) * 8);
						y = (ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseY").getInt(client.uberClient) + (client.myPlayer.y - 6 >> 7)) - ((ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "anInt1070").getInt(client.uberClient) - 6) * 8);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (y > 0 && y < 103 && x > 0 && x < 103) {
						boolean isInventoryEmpty = true;
						for (int i = 0; i < 28; i++) { */
							// if (RSInterface.interfaceCache[3214].inv /* inventory */[i] - 1 != -1) {
								/* isInventoryEmpty = false;
								break;
							}
						}
						if (isInventoryEmpty) {
							NodeList[][][] groundArray = null;
							int plane = 0, baseX = 0, baseY = 0;
							try {
								groundArray = (NodeList[][][]) ReflectionHelper.findField(client.class, Modifier.PRIVATE, NodeList[][][].class, "groundArray").get(client.uberClient);
								plane = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "plane").getInt(client.uberClient);
								baseX = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseX").getInt(client.uberClient);
								baseY = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseY").getInt(client.uberClient);
							} catch (IllegalArgumentException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							boolean resetFields = false;
							for (int i = 0; i < 104; i++) {
								for (int j = 0; j < 104; j++) {
									NodeList nl = groundArray[plane][i][j];
									if (nl == null) {
										continue;
									}
									int k = 0;
									for (Item it = (Item) nl.reverseGetFirst(); it != null; it = (Item) nl.reverseGetNext()) {
										if (!this.equippedItems.contains(it.ID) || it.x != x || it.y != y) {
											continue;
										}
										stream.createFrame(236);
										stream.method431(it.y + baseY);
										stream.writeWord(it.ID);
										stream.method431(it.x + baseX);
										stream.createFrame(41);
										stream.writeWord(it.ID);
										stream.method432(k);
										stream.method432(3214);
										++k;
										resetFields = true;
									}
									for (Item it = (Item) nl.reverseGetFirst(); it != null; it = (Item) nl.reverseGetNext()) {
										if (!this.inventoryItems.contains(it.ID) || it.x != x || it.y != y) {
											continue;
										}
										stream.createFrame(236);
										stream.method431(it.y + baseY);
										stream.writeWord(it.ID);
										stream.method431(it.x + baseX);
										resetFields = true;
									}
								}
							}
							if (resetFields) {
								this.inventoryItems.clear();
								this.equippedItems.clear();
								this.hasAddedItems = false;
							}
						}
					}
				}
				else {
					for (int i = 0; i < 28; i++) { */
						// int j = RSInterface.interfaceCache[3214].inv /* inventory */[i] - 1;
						/* if (j == -1) {
							continue;
						}
						this.inventoryItems.add(j);
					}
					for (int i = 0; i < client.myPlayer.equipment.length - 1; i++) {
						int j = client.myPlayer.equipment[i];
						if (i == 6 || i == 8 || j < 512) {
							continue;
						}
						j -= 512;
						this.equippedItems.add(j);
					}
					for (int i = 12; i < 14; i++) { */
						// int j = RSInterface.interfaceCache[1688].inv /* inventory */[i] - 1;
						/* if (j == -1) {
							continue;
						}
						this.equippedItems.add(j);
					}
					this.hasAddedItems = true;
				}
			}
			else { */
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
				List<Integer> itemIDs = new ArrayList<Integer>();
				for (int i = 0; i < client.myPlayer.equipment.length - 1; i++) {
					int j = client.myPlayer.equipment[i];
					if (i == 6 || i == 8 || j < 512) {
						continue;
					}
					j -= 512;
					/* client.uberClient. */stream.createFrame(145);
					/* client.uberClient. */stream.method432(1688);
					/* client.uberClient. */stream.method432(i);
					/* client.uberClient. */stream.method432(j);
					itemIDs.add(j);
				}
				for (int i = 12; i < 14; i++) {
					int j = RSInterface.interfaceCache[1688].inv /* inventory */[i] - 1;
					if (j == -1) {
						continue;
					}
					/* client.uberClient. */stream.createFrame(145);
					/* client.uberClient. */stream.method432(1688);
					/* client.uberClient. */stream.method432(i);
					/* client.uberClient. */stream.method432(j);
					itemIDs.add(j);
				}
				for (int i = 0; i < itemIDs.size(); i++) {
					/* client.uberClient. */stream.createFrame(129);
					/* client.uberClient. */stream.method432(i);
					/* client.uberClient. */stream.writeWord(5064);
					/* client.uberClient. */stream.method432(itemIDs.get(i));
				}
			// }
		}
	}

}

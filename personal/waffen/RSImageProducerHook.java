

import java.awt.Graphics;

public class RSImageProducerHook extends RSImageProducer {

	public RSImageProducerHook(int var1, int var2) {
		super(var1, var2, client.uberClient.getGameComponent());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawGraphics(int var1, Graphics var2, int var3) {
		EventManager.callEvent(new DrawGraphicsEvent(this));
		super.drawGraphics(var1, var2, var3);
	}
}

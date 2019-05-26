

import java.awt.Component;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
// import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClientHook extends client {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	void login(String arg0, String arg1, boolean arg2) {
		EventManager.callEvent(new LoginEvent(arg0, arg1, arg2));
		super.login(arg0, arg1, arg2);
	}
	@Override
	protected void method73() {
		Field readIndexField = ReflectionHelper.findField(RSApplet.class, Modifier.PRIVATE, Integer.TYPE, "readIndex");
		int readIndex = 0;
		try {
			readIndex = readIndexField.getInt(this);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (((CancellableEvent) EventManager.callEvent(new HandleInputEvent(this))).isCancelled()) {
			try {
				ReflectionHelper.findField(client.class, Modifier.PRIVATE, String.class, "inputString").set(this, "");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			inputTaken = true;
			return;
		}
		try {
			readIndexField.setInt(this, readIndex);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.method73();
	}
	@Override
	public void processGameLoop() {
		super.processGameLoop();
		EventManager.callEvent(new ProcessGameLoopEvent(this));
		this.setMain3DArea();
	}
	/* @Override
	public void recreateClientFrame(boolean var1, int var2, int var3, boolean var4, int var5, boolean var6) {
		super.recreateClientFrame(var1, var2, var3, var4, var5, var6);
		this.setMain3DArea();
	} */
	@Override
	protected void resetImageProducers2() {
		super.resetImageProducers2();
		this.setMain3DArea();
	}
	private void setMain3DArea() {
		try {
			Field main3DAreaField = ReflectionHelper.findField(client.class, Modifier.PRIVATE, RSImageProducer.class, "aRSImageProducer_1165" /* "main3DArea" */);
			RSImageProducer main3DArea = (RSImageProducer) main3DAreaField.get(this);
			if (main3DArea != null) {
				int savedWidth = DrawingArea.width, savedHeight = DrawingArea.height;
				int[] savedPixels = DrawingArea.pixels;
				RSImageProducer newMain3DArea = new RSImageProducerHook(ReflectionHelper.findField(RSImageProducer.class, Modifier.PRIVATE | Modifier.FINAL, Integer.TYPE, "anInt316").getInt(main3DArea), ReflectionHelper.findField(RSImageProducer.class, Modifier.PRIVATE | Modifier.FINAL, Integer.TYPE, "anInt317").getInt(main3DArea));
				ReflectionHelper.findField(RSImageProducer.class, Modifier.PUBLIC | Modifier.FINAL, int[].class, "anIntArray315").set(newMain3DArea, main3DArea.anIntArray315);
				Field colorModelField = ReflectionHelper.findField(RSImageProducer.class, Modifier.PRIVATE | Modifier.FINAL, ColorModel.class, "aColorModel318"), imageConsumerField = ReflectionHelper.findField(RSImageProducer.class, Modifier.PRIVATE, ImageConsumer.class, "anImageConsumer319"), bufferedImageField = ReflectionHelper.findField(RSImageProducer.class, Modifier.PRIVATE | Modifier.FINAL, Image.class /* BufferedImage.class */, "anImage320" /* "bufferedImage" */);
				colorModelField.set(newMain3DArea, colorModelField.get(main3DArea));
				imageConsumerField.set(newMain3DArea, imageConsumerField.get(main3DArea));
				bufferedImageField.set(newMain3DArea, bufferedImageField.get(main3DArea));
				Image image = (Image) bufferedImageField.get(newMain3DArea);
				Method method239Method = ReflectionHelper.findMethod(RSImageProducer.class, Modifier.PRIVATE | Modifier.SYNCHRONIZED, Void.TYPE, "method239", new Class[0]);
				method239Method.invoke(newMain3DArea);
				Component gameComponent = client.uberClient.getGameComponent();
				gameComponent.prepareImage(image, newMain3DArea);
				method239Method.invoke(newMain3DArea);
				gameComponent.prepareImage(image, newMain3DArea);
				method239Method.invoke(newMain3DArea);
				gameComponent.prepareImage(image, newMain3DArea);
				DrawingArea.initDrawingArea(savedHeight, savedWidth, savedPixels);
				main3DAreaField.set(this, newMain3DArea);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

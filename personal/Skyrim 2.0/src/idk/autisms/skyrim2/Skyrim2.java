package idk.autisms.skyrim2;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Skyrim2 {

	private static JFrame skyrimFrame;
	
	public static void main(String[] args) {
		skyrimFrame = new JFrame("LWJGL IS SHIT LOL JAVA GRAPHICS 2 COOL");
		Painter painter = new Painter();
		skyrimFrame.getContentPane().add(painter);
		skyrimFrame.setMinimumSize(new Dimension(1024, 768));
		skyrimFrame.pack();
		skyrimFrame.addKeyListener(painter.penisGuy);
		skyrimFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		skyrimFrame.setVisible(true);
		while (true) {
			painter.repaint();
		}
	}
	
	private static class Painter extends Component {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private BufferedImage skyImage;
		
		private List<Cloud> clouds = new ArrayList<Cloud>();
		
		private PenisGuy penisGuy = new PenisGuy();
		
		private List<WorkoutShit> workoutShits = new ArrayList<WorkoutShit>();
		
		private Painter() {
			setSize(1024, 768);
			try {
				skyImage = ImageIO.read(new File("assets/idk/autisms/skyrim2/textures/background.bmp"));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Random rand = new Random();
			int prevPosX = 0;
			for (int cloudCount = 0; cloudCount < 5; cloudCount++) {
				int posX = rand.nextInt(256);
				while (Math.abs(posX - prevPosX) < 256) {
					posX = rand.nextInt(768);
				}
				clouds.add(new Cloud(posX, rand.nextInt(256)));
				prevPosX = posX;
			}
			prevPosX = 0;
			for (int workoutCount = 0; workoutCount < 5; workoutCount++) {
				int posX = rand.nextInt(256);
				while (Math.abs(posX - prevPosX) < 128) {
					posX = rand.nextInt(256);
				}
				workoutShits.add(new WorkoutShit(posX, rand.nextInt(256)));
				prevPosX = posX;
			}
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(skyImage, 0, 0, 1024, 768, this);
			for (Cloud curCloud : clouds) {
				curCloud.paint(g);
			}
			for (WorkoutShit workout : workoutShits) {
				workout.paint(g);
			}
			penisGuy.paint(g);
		}
		
	}
	
	private static class Cloud extends Component {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int posX;
		private int posY;
		
		private BufferedImage cloudImage;
		
		private Cloud(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
			try {
				cloudImage = ImageIO.read(new File("assets/idk/autisms/skyrim2/textures/cloud.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(cloudImage, posX, posY, 256, 128, this);
			if (++posX > 1024) {
				posX = 0;
			}
		}
		
	}
	
private static class WorkoutShit extends Component {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int posX;
		private int posY;
		
		private BufferedImage workoutImage;
		
		private WorkoutShit(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
			try {
				workoutImage = ImageIO.read(new File("assets/idk/autisms/skyrim2/textures/workout_shit.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(workoutImage, posX, posY, 128, 64, this);
			if (++posX > 1024) {
				posX = 0;
			}
		}
		
	}
	
	private static class PenisGuy extends Component implements KeyListener {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private BufferedImage penisImage;
		
		private int posX = 256, posY = 416;
		
		private PenisGuy() {
			try {
				penisImage = ImageIO.read(new File("assets/idk/autisms/skyrim2/textures/penis_guy.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(penisImage, posX, posY, 128, 256, this);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				posX += 16;
				if (posX > 1024) {
					posX = 0;
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				posX -= 16;
				if (posX < 0) {
					posX = 1024;
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				posY += 16;
				if (posY > 768) {
					posY = 0;
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				posY -= 16;
				if (posY < 0) {
					posY = 768;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}

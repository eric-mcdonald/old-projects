package org.bitbucket.reliant.ui;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.bitbucket.reliant.Reliant;

public final class UiFrame extends JFrame {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	public static final Image logoImage = new ImageIcon(UiFrame.class.getResource("/logo.png")).getImage();
	
	public UiFrame(String name, final Component content) {
		super(Reliant.NAME + " " + Reliant.instance.buildText() + ": " + name);
		setIconImage(logoImage);
		getContentPane().add(content);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

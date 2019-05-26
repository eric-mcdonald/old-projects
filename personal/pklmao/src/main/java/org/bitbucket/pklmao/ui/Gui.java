package org.bitbucket.pklmao.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.bitbucket.pklmao.PkLmao;

public class Gui {
	private PkLmao pkLmao;
	private JFrame mainFrame = new JFrame(PkLmao.NAME);
	private ConsolePanel console;
	private TogglesPanel toggles;
	
	public Gui(PkLmao pkLmao) {
		this.pkLmao = pkLmao;
		console = new ConsolePanel(25, 50, pkLmao.getI18n());
		console.addOutput(pkLmao.getI18n().translate("ui.tabs.console.init", "TODO"));
		toggles = new TogglesPanel();
	}
	
	private JMenuItem createMenuItem(String nameKey, String actCmd, ActionListener clickListener) {
		JMenuItem item = new JMenuItem(pkLmao.getI18n().translate(nameKey));
		item.setActionCommand(actCmd);
		item.addActionListener(clickListener);
		return item;
	}
	public ConsolePanel getConsole() {
		return console;
	}
	public TogglesPanel getToggles() {
		return toggles;
	}
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JMenuBar menuBar = new JMenuBar();
				JMenu cfgMenu = new JMenu(pkLmao.getI18n().translate("ui.menubar.menu.cfg.name")), helpMenu = new JMenu(pkLmao.getI18n().translate("ui.menubar.menu.help.name"));
				ActionListener cfgListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				};
				cfgMenu.add(createMenuItem("ui.menubar.menu.cfg.save", "save", cfgListener));
				cfgMenu.add(createMenuItem("ui.menubar.menu.cfg.save_as", "save_as", cfgListener));
				cfgMenu.addSeparator();
				cfgMenu.add(createMenuItem("ui.menubar.menu.cfg.load", "load", cfgListener));
				cfgMenu.add(createMenuItem("ui.menubar.menu.cfg.reload", "reload", cfgListener));
				cfgMenu.addSeparator();
				cfgMenu.add(createMenuItem("ui.menubar.menu.cfg.restart", "restart", cfgListener));
				cfgMenu.add(createMenuItem("ui.menubar.menu.cfg.exit", "exit", cfgListener));
				menuBar.add(cfgMenu);
				ActionListener helpListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				};
				helpMenu.add(createMenuItem("ui.menubar.menu.help.about", "about", helpListener));
				menuBar.add(helpMenu);
				JTabbedPane guiTabs = new JTabbedPane();
				guiTabs.addTab(pkLmao.getI18n().translate("ui.tabs.console.name"), getConsole());
				guiTabs.addTab(pkLmao.getI18n().translate("ui.tabs.toggles.name"), getToggles());
				mainFrame.setJMenuBar(menuBar);
				mainFrame.add(guiTabs);
				mainFrame.pack();
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainFrame.setVisible(true);
			}
		});
	}
	public PkLmao getPkLmao() {
		return pkLmao;
	}
	public JFrame getMainFrame() {
		return mainFrame;
	}
}

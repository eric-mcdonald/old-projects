package org.bitbucket.reliant.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cmd.Command;
import org.bitbucket.reliant.cmd.CommandException;

public final class ConsolePanel extends JPanel implements ActionListener {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	public static final String CONSOLE_IN_CMD = "console_input";
	private final JTextArea outputArea = new JTextArea();
	private final JTextField inputField = new JTextField();
	
	public ConsolePanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		outputArea.setEditable(false);
		((DefaultCaret) outputArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		outputArea.setColumns(32);
		outputArea.setRows(16);
		final JScrollPane outputScrollPane = new JScrollPane(outputArea);
		add(outputScrollPane, BorderLayout.CENTER);
		inputField.setActionCommand("console_input");
		inputField.addActionListener(this);
		add(inputField, BorderLayout.CENTER);
		appendOutput("Type \"" + Reliant.instance.getCmdRegistry().get("help").names()[0] + "\" for a list of commands.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals(CONSOLE_IN_CMD)) {
			final JTextField inputField = (JTextField) e.getSource();
			final String inputTxt = inputField.getText();
			appendOutput("> " + inputTxt);
			final String[] splitCmd = inputTxt.split(" ");
			final Command matchedCmd = Reliant.instance.getCmdRegistry().get(splitCmd[0]);
			final List<String> output = new ArrayList<String>();
			if (matchedCmd == null) {
				output.add("Could not find a command with name " + splitCmd[0]);
			} else {
				String[] cmdArgs = new String[splitCmd.length - 1];
				for (int argIdx = 1; argIdx < splitCmd.length; argIdx++) {
					cmdArgs[argIdx - 1] = splitCmd[argIdx];
				}
				try {
					matchedCmd.execute(cmdArgs, output);
				} catch (final CommandException cmdEx) {
					output.add("An error occcured while executing command " + matchedCmd.names()[0] + ": " + cmdEx);
				} catch (final Exception ex) {
					output.add("An unknown error occured while executing command " + matchedCmd.names()[0] + ": " + ex);
					Reliant.instance.getLogger().logError(ex);
				}
			}
			for (final String message : output) {
				appendOutput(message);
			}
			inputField.setText("");
		}
	}
	public void appendOutput(final String message) {
		outputArea.append(message + "\n");
	}
	public JTextField getInputField() {
		return inputField;
	}
}

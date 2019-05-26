package org.bitbucket.pklmao.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bitbucket.pklmao.cmd.Command;
import org.bitbucket.pklmao.cmd.CommandException;
import org.bitbucket.pklmao.util.I18n;

public class ConsolePanel extends RegistryPanel<String, Command> {
	private static final long serialVersionUID = 1L;
	private JTextArea outputArea;
	private JTextField inputField;
	
	public ConsolePanel(int rows, int columns, final I18n translator) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		outputArea = new JTextArea(rows, columns);
		inputField = new JTextField(columns);
		inputField.setActionCommand("input");
		ActionListener consoleListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("input")) {
					String[] cmdFields = inputField.getText().split(" ");
					Command cmd = getObj(cmdFields[0]);
					if (cmd != null) {
						final List<String> cmdArgs = new ArrayList<String>();
						for (int i = 1; i < cmdFields.length; i++) {
							cmdArgs.add(cmdFields[i]);
						}
						try {
							final List<String> outputMsgs = new ArrayList<String>();
							cmd.execute(cmdArgs.toArray(new String[0]), outputMsgs);
							if (outputMsgs != null && !outputMsgs.isEmpty()) {
								for (String outputMsg : outputMsgs) {
									addOutput(outputMsg);
								}
							}
						} catch (CommandException cmdEx) {
							addOutput(cmdEx.getLocalizedMessage());
						} catch (Throwable error) {
							addOutput(translator.translate("ui.tabs.console.unknown_err", cmd.name(), error));
						}
					} else {
						addOutput(translator.translate("ui.tabs.console.cmd_not_found", cmdFields[0]));
					}
					inputField.setText("");
				}
			}
		};
		inputField.addActionListener(consoleListener);
		JScrollPane outDisplayComp = new JScrollPane(outputArea);
		add(outDisplayComp);
		add(inputField);
	}
	
	public void addOutput(String message) {
		outputArea.append(message + "\n");
	}
	public void addOutput(Throwable error) {
		PrintStream errStream = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				outputArea.append(Character.toString((char) b));
			}
		});
		error.printStackTrace(errStream);
		errStream.close();
	}
}

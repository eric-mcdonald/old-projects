package org.bitbucket.reliant.cfg;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.bitbucket.eric_generic.lang.StringUtils;

public abstract class TextOption<V> extends BaseOption<V> implements ActionListener {
	public TextOption(final String name, final String description, final V defaultVal, final int maxFieldLen) {
		super(name, description, defaultVal, new JTextField());
		final JTextField guiComponent = (JTextField) guiComponent();
		guiComponent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64)); // Eric: Limit the text field's height to prevent large spaces being taken by it.
		guiComponent.setText(defaultVal.toString());
		guiComponent.setActionCommand("input");
		guiComponent.addActionListener(this);
		guiComponent.setDocument(new PlainDocument() {
			/**
			 * The default serial version UID.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if ((guiComponent.getText() + str).length() > maxFieldLen) { // Eric: (guiComponent.getText() + str).length() > MAX_PATH
					return;
				}
				super.insertString(offs, str, a);
			}
		});
		guiComponent.setText(defaultVal.toString());
		// TODO Auto-generated constructor stub
	}

	@Override
	public final void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("input")) {
			String value = ((JTextField) e.getSource()).getText();
			if (StringUtils.empty(value)) {
				value = getCfgDefault().toString();
			}
			setValue(parseValue(value));
		}
	}
	@Override
	protected void refreshComponent() {
		// TODO Auto-generated method stub
		((JTextField) guiComponent()).setText(getValue().toString());
	}
}

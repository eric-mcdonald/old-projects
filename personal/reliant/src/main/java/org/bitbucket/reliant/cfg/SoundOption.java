package org.bitbucket.reliant.cfg;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.bitbucket.eric_generic.media.Sound;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.ui.TabUi;

public final class SoundOption extends TextOption<Sound> {
	private static Sound createSound(final String value) {
		try {
			return new Sound(value.startsWith("/") ? TabUi.class.getResource(value) : new File(value).toURI().toURL());
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
		return null;
	}

	public SoundOption(final String name, final String description, final String defaultVal) {
		super(name, description, createSound(defaultVal), DirOption.MAX_PATH);
		cfgValue = cfgDefault = defaultVal;
		((JTextComponent) guiComponent()).setText(defaultVal);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Sound parseValue(String value) {
		// TODO Auto-generated method stub
		cfgValue = value;
		return createSound(value);
	}
	public void play(final float volume) {
		final Sound value = getValue();
		try {
			value.setVolume(volume / 100.0F);
			value.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Reliant.instance.getLogger().logError(e);
		}
	}
	@Override
	protected void refreshComponent() {
		// TODO Auto-generated method stub
		((JTextField) guiComponent()).setText(getCfgValue().toString());
	}
	@Override
	public void setValue(final Object value) {
		final Object cfgValue = this.cfgValue;
		super.setValue(value);
		this.cfgValue = cfgValue;
		refreshComponent();
	}
}

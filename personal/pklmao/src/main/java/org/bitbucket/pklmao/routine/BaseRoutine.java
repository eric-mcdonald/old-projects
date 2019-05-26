package org.bitbucket.pklmao.routine;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

import org.bitbucket.pklmao.PkLmao;
import org.bitbucket.pklmao.cfg.BasicConfiguration;
import org.bitbucket.pklmao.cfg.Configuration;
import org.bitbucket.pklmao.event.EventManager;

public abstract class BaseRoutine implements Routine {
	private String id;
	private String nameKey, descKey;
	private boolean enabled;
	private Configuration config;
	private PkLmao pkLmao;
	private JCheckBox toggleBtn;
	
	public BaseRoutine(String id, String nameKey, String descKey, Configuration config, PkLmao pkLmao) {
		this.id = id;
		this.nameKey = nameKey;
		this.descKey = descKey;
		this.config = config;
		this.pkLmao = pkLmao;
		toggleBtn = new JCheckBox(name());
		toggleBtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setEnabled(!isEnabled());
			}
		});
	}
	public BaseRoutine(String id, String nameKey, String descKey, Configuration config) {
		this(id, nameKey, descKey, config, PkLmao.getInstance());
	}
	public BaseRoutine(String id, String nameKey, String descKey) {
		this(id, nameKey, descKey, new BasicConfiguration(0, "routine_" + id + ".cfg"), PkLmao.getInstance());
	}

	@Override
	public String toString() {
		return id();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pkLmao == null) ? 0 : pkLmao.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BaseRoutine)) {
			return false;
		}
		BaseRoutine other = (BaseRoutine) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (pkLmao == null) {
			if (other.pkLmao != null) {
				return false;
			}
		} else if (!pkLmao.equals(other.pkLmao)) {
			return false;
		}
		return true;
	}
	@Override
	public String id() {
		return id;
	}
	@Override
	public String name() {
		return pkLmao.getI18n().translate(nameKey);
	}
	@Override
	public String desc() {
		return pkLmao.getI18n().translate(descKey);
	}
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (isEnabled()) {
			EventManager.getInstance().addListener(this);
		} else {
			EventManager.getInstance().removeListener(this);
		}
		toggleBtn.setSelected(isEnabled());
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	@Override
	public Configuration config() {
		return config;
	}

	@Override
	public AbstractButton toggleBtn() {
		return toggleBtn;
	}
}

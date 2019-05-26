package org.bitbucket.pklmao.cfg;

import javax.swing.JOptionPane;

import org.bitbucket.pklmao.PkLmao;

public abstract class BaseOption<T> implements Option<T> {
	private String id;
	private String nameKey, descKey;
	private T value;
	private boolean requiresRestart;
	private PkLmao pkLmao;
	
	public BaseOption(String id, String nameKey, String descKey, T value, boolean requiresRestart, PkLmao pkLmao) {
		this.id = id;
		this.nameKey = nameKey;
		this.descKey = descKey;
		this.value = value;
		this.requiresRestart = requiresRestart;
		this.pkLmao = pkLmao;
	}
	public BaseOption(String id, String nameKey, String descKey, T value, boolean requiresRestart) {
		this(id, nameKey, descKey, value, requiresRestart, PkLmao.getInstance());
	}
	public BaseOption(String id, String nameKey, String descKey, T value) {
		this(id, nameKey, descKey, value, false);
	}
	
	@Override
	public String toString() {
		return id() + "=" + getValue();
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
		if (!(obj instanceof BaseOption)) {
			return false;
		}
		BaseOption<?> other = (BaseOption<?>) obj;
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
	public T getValue() {
		return value;
	}
	@Override
	public void setValue(T value, boolean fromCfg) {
		final T prevVal = this.value;
		this.value = value;
		if (!this.value.equals(prevVal)) {
			onValChanged(fromCfg);
		}
	}
	@Override
	public void setValue(String valueStr, boolean fromCfg) {
		setValue(parseValue(valueStr), fromCfg);
	}
	protected void onValChanged(boolean fromCfg) {
		if (requiresRestart && !fromCfg) {
			JOptionPane.showMessageDialog(pkLmao.getGui().getMainFrame(), pkLmao.getI18n().translate("cfg.opt.change_on_restart"), name(), JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}

package org.bitbucket.pklmao.cmd;

import java.util.Arrays;

import org.bitbucket.pklmao.PkLmao;
import org.bitbucket.pklmao.util.StringUtils;

public abstract class BaseCommand implements Command {
	private String name;
	private String descKey;
	private String[] aliases;
	private PkLmao pkLmao;
	private String paramUsageKey;
	
	public BaseCommand(String name, String descKey, String paramUsageKey, PkLmao pkLmao, String... aliases) {
		this.name = name;
		this.descKey = descKey;
		this.paramUsageKey = paramUsageKey;
		this.pkLmao = pkLmao;
		this.aliases = aliases;
	}
	public BaseCommand(String name, String descKey, String paramUsageKey, String... aliases) {
		this(name, descKey, paramUsageKey, PkLmao.getInstance(), aliases);
	}
	
	@Override
	public String toString() {
		return name();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(aliases);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof BaseCommand)) {
			return false;
		}
		BaseCommand other = (BaseCommand) obj;
		if (!Arrays.equals(aliases, other.aliases)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
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
	public String name() {
		return name;
	}
	@Override
	public String[] aliases() {
		return aliases;
	}
	@Override
	public String desc() {
		return pkLmao.getI18n().translate(descKey);
	}
	@Override
	public String usage() {
		String usage = name();
		for (String alias : aliases()) {
			usage += "|" + alias;
		}
		if (!StringUtils.isEmpty(paramUsageKey)) {
			usage += " " + pkLmao.getI18n().translate(paramUsageKey);
		}
		return usage;
	}
	protected void assertUsage(boolean validCond) throws InvalidUsageException {
		if (!validCond) {
			throw new InvalidUsageException(this);
		}
	}
}

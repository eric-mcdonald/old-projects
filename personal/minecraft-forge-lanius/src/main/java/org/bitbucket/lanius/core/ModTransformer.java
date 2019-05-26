package org.bitbucket.lanius.core;

import net.minecraft.launchwrapper.IClassTransformer;

public abstract class ModTransformer implements IClassTransformer {

	private final String className;

	public ModTransformer(final String className) {
		this.className = className.replace('/', '.');
	}

	@Override
	public final byte[] transform(String name, String transformedName, byte[] basicClass) {
		// TODO Auto-generated method stub
		if (!transformedName.equals(className)) {
			return basicClass;
		}
		return transformedClass(basicClass);
	}

	protected abstract byte[] transformedClass(final byte[] basicClass);

}

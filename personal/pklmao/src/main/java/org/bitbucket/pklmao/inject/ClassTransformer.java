package org.bitbucket.pklmao.inject;

import java.io.IOException;

import org.bitbucket.pklmao.util.ReflectHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public abstract class ClassTransformer {
	private String className;
	
	public ClassTransformer(String className) {
		this.className = className;
	}
	
	public byte[] transform() throws IOException {
		ClassReader reader = new ClassReader(className);
		ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		reader.accept(createVisitor(writer), 0);
		return writer.toByteArray();
	}
	public void loadClass(ClassLoader loader) {
		byte[] classBin = null;
		try {
			classBin = transform();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (classBin != null) {
			ReflectHelper.invoke(ReflectCache.getDefClsMethod(), loader, className, classBin, 0, classBin.length);
		}
	}
	protected abstract ClassVisitor createVisitor(ClassWriter writer);
}

package org.bitbucket.lanius.core;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public abstract class BasicTransformer extends ModTransformer {

	public BasicTransformer(final String className) {
		super(className);
		// TODO Auto-generated constructor stub
	}

	protected abstract ClassVisitor classVisitor(final ClassWriter classWriter);

	@Override
	public final byte[] transformedClass(final byte[] basicClass) {
		// TODO Auto-generated method stub
		final ClassReader classReader = new ClassReader(basicClass);
		final ClassWriter classWriter = new ClassWriter(classReader, COMPUTE_FRAMES | COMPUTE_MAXS);
		classReader.accept(classVisitor(classWriter), 0);
		return classWriter.toByteArray();
	}

}

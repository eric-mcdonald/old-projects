package net.hackforums.pootpoot.monumental.util;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfuscationReflectionHelper {

	public static String[] remapMethodNames(String className, String methodDesc, String... methodNames)
    {
        String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(className.replace('.', '/'));
        String[] mappedNames = new String[methodNames.length];
        int nameCount = 0;
        for (String methodName : methodNames)
        {
            mappedNames[nameCount++] = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internalClassName, methodName, methodDesc);
        }
        return mappedNames;
    }
}

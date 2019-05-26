package org.bitbucket.eric_generic.lang;

public class ArrayUtils {
	public static <E> boolean arraysEqual(final E[] arrayOne, final E[] arrayTwo) {
		if (arrayOne == null && arrayTwo == null) {
			return true;
		}
		if (arrayOne == null && arrayOne != arrayTwo || arrayTwo == null && arrayTwo != arrayOne || arrayOne.length != arrayTwo.length) {
			return false;
		}
		for (int i = 0; i < arrayOne.length && i < arrayTwo.length; i++) {
			if (arrayOne[i] == null ? arrayTwo[i] == null ? arrayOne[i] != arrayTwo[i] : !arrayTwo[i].equals(arrayOne[i]) : !arrayOne[i].equals(arrayTwo[i])) {
				return false;
			}
		}
		return true;
	}
	public static <E> boolean contains(final Object obj, final E[] array) {
		for (final E element : array) {
			if (element.equals(obj)) {
				return true;
			}
		}
		return false;
	}
}

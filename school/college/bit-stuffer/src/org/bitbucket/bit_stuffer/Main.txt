package org.bitbucket.bit_stuffer;

public final class Main {
	public static void main(String[] args) {
		String data = "011111101111110", newData = "";
		for (int i = 0, oneCount = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			if (c == '1') {
				++oneCount;
			} else {
				oneCount = 0;
			}
			newData += Character.toString(c);
			if (oneCount > 0 && oneCount % 5 == 0) {
				newData += "0";
			}
		}
		System.out.println("Before stuffing: " + data + " -- " + data.length() + " characters");
		System.out.println("After stuffing:  " + newData + " -- " + newData.length() + " characters");
		final String flag = "01111110";
		String framedData = flag + newData + flag;
		System.out.println("After framing:   " + framedData);
	}
}

package org.bitbucket.lanius.gui;

import java.io.IOException;

public interface TypeableComponent extends Component {
	void keyTyped(final char typedChar, final int keyCode) throws IOException;
}

package io.github.albertus82.jface.maps;

import org.eclipse.swt.browser.Browser;

public interface MapBoundsDialog {

	MapBounds getBounds();

	MapOptions getOptions();

	void setBoundValues(Browser browser);

	void setOptionValues(Browser browser);

	void setReturnCode(int code);

	int open();

}

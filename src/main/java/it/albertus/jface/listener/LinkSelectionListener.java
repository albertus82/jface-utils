package it.albertus.jface.listener;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;

public class LinkSelectionListener implements SelectionListener {

	@Override
	public void widgetSelected(SelectionEvent event) {
		Program.launch(event.text);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent event) {
		widgetSelected(event);
	}

}

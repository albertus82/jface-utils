package it.albertus.jface.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class ListConsole extends ScrollableConsole<List> {
	
	private boolean newLine=true;

	protected ListConsole(final Composite parent, final Object layoutData, final boolean redirectSystemStream) {
		super(parent, layoutData, redirectSystemStream);
	}

	@Override
	public void clear() {
		scrollable.removeAll();
	}

	@Override
	public boolean isEmpty() {
		return scrollable.getItemCount() == 0;
	}

	@Override
	public boolean hasSelection() {
		return scrollable.getSelectionCount() > 0;
	}

	@Override
	protected List createScrollable(final Composite parent) {
		return new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	}

	@Override
	protected void doPrint(final String value, final int maxChars) {
		scrollable.setRedraw(false);
		
//		StringWriter sw = new StringWriter();
//		BufferedWriter bw = new BufferedWriter(sw);
//		try {
//		bw.write(value);bw.close();
//		}catch(IOException ioe) {}
		
//		try {
//		FileWriter fw = new FileWriter("d:\\text.txt", true);
//		fw.write(value);
//		fw.close();
//		}catch(IOException e){}
		 
//		StringReader sr = new StringReader(value);
//		BufferedReader br = new BufferedReader(sr);
//		String line;
//		try{
//		while ((line = br.readLine()) != null) {
		final String[] tokens = value.split("(?<=\r\n)", -1);
for(String line: tokens) {
			if (newLine) {
				scrollable.add(line);
			}
			else {
				scrollable.setItem(scrollable.getItemCount()-1, scrollable.getItem(scrollable.getItemCount()-1)+line);
			}
			if (line.endsWith("\r\n") || line.endsWith("\n" ) || line.endsWith("\r")) {
				newLine = true;
			}
			else {
				newLine=false;
			}
//			for (char c:line.toCharArray()) {
//				defaultSysOut.printf("%04x ", (int)c);
//				if (c == 13 || c == 10) defaultSysOut.print("\n");
//			}
//			defaultSysOut.println(value);
		}
//		}catch(IOException ioe) {}
//		final String[] tokens = value.split("[\r\n]", -1);
//		for (final String token : tokens) {
//			try {
//				for (char c:token.toCharArray()) {
//					defaultSysOut.printf("%04x ", (int)c);
//					if (c == 13 || c == 10) defaultSysOut.print("\n");
//				}
//			}catch(Exception e){}
//			scrollable.add(token);
//		}
		scrollable.setTopIndex(scrollable.getItemCount() - 1);
		scrollable.setRedraw(true);
	}

}

package it.albertus.jface.sysinfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import it.albertus.jface.JFaceMessages;
import it.albertus.util.IOUtils;

public class SystemInformationExporter implements IRunnableWithProgress {

	private static final String TASK_NAME = "Exporting system information";

	private static final String LBL_SYSTEM_INFO_EXPORT_TITLE = "lbl.system.info.export.title";

	private static final char KEY_VALUE_SEPARATOR = '=';

	private final String fileName;
	private final Map<String, Map<String, String>> maps;
	private final Map<String, Iterable<String>> iterables;

	public SystemInformationExporter(final String fileName, final Map<String, Map<String, String>> maps, final Map<String, Iterable<String>> iterables) {
		this.fileName = fileName;
		this.maps = maps;
		this.iterables = iterables;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws InvocationTargetException {
		monitor.beginTask(TASK_NAME, maps.size() + iterables.size());

		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);

			bw.write(JFaceMessages.get(LBL_SYSTEM_INFO_EXPORT_TITLE, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, new Locale(JFaceMessages.getLanguage())).format(new Date())));
			bw.newLine();

			for (final Entry<String, Map<String, String>> e1 : maps.entrySet()) {
				bw.newLine();
				bw.write(e1.getKey());
				bw.newLine();
				for (final Entry<String, String> e2 : e1.getValue().entrySet()) {
					bw.append(e2.getKey()).append(KEY_VALUE_SEPARATOR).append(e2.getValue());
					bw.newLine();
				}
				monitor.worked(1);
			}

			for (final Entry<String, Iterable<String>> entry : iterables.entrySet()) {
				bw.newLine();
				bw.write(entry.getKey());
				bw.newLine();
				for (final String str : entry.getValue()) {
					bw.write(str);
					bw.newLine();
				}
				monitor.worked(1);
			}
		}
		catch (final IOException e) {
			throw new InvocationTargetException(e);
		}
		finally {
			IOUtils.closeQuietly(bw, fw);
		}

		monitor.done();
	}

}

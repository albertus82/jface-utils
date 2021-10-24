/*
 * Copyright (c) 2000, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package io.github.albertus82.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * A variant of {@code java.util.logging}'s {@code SimpleFormatter} that uses a
 * custom format string.
 * 
 * @see SimpleFormatter
 */
public class CustomFormatter extends Formatter {

	private String format;
	private final Date timestamp = new Date();

	/**
	 * Creates a new formatter with the provided format string.
	 * 
	 * @param format the format string
	 */
	public CustomFormatter(final String format) {
		this.format = format;
	}

	/**
	 * Format the given LogRecord.
	 * 
	 * @param rec the log record to be formatted.
	 * @return a formatted log record
	 */
	@Override
	public synchronized String format(final LogRecord rec) {
		timestamp.setTime(rec.getMillis());
		String source;
		if (rec.getSourceClassName() != null) {
			source = rec.getSourceClassName();
			if (rec.getSourceMethodName() != null) {
				source += " " + rec.getSourceMethodName();
			}
		}
		else {
			source = rec.getLoggerName();
		}
		final String message = formatMessage(rec);
		String throwable = "";
		if (rec.getThrown() != null) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			pw.println();
			rec.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString();
		}
		return String.format(format, timestamp, source, rec.getLoggerName(), rec.getLevel().getName(), message, throwable);
	}

	/**
	 * Returns the current format string.
	 * 
	 * @return the format string
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set a new format string.
	 * 
	 * @param format the format string
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

}

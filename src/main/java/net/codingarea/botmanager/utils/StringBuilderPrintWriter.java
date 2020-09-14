package net.codingarea.botmanager.utils;

import java.io.PrintWriter;

/**
 * Uses a {@link StringBuilderWriter} as output
 * @see StringBuilderWriter
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public class StringBuilderPrintWriter extends PrintWriter {

	protected final StringBuilderWriter writer;

	public StringBuilderPrintWriter() {
		super(new StringBuilderWriter());
		writer = (StringBuilderWriter) out;
	}

	public StringBuilder getBuilder() {
		return writer.builder;
	}

}

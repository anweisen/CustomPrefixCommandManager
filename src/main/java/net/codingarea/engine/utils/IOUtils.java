package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class IOUtils {

	private IOUtils() { }

	public static String toString(URL url, Charset encoding) throws IOException {

		InputStream inputStream = url.openStream();

		String string;
		try {
			string = toString(inputStream, encoding);
		} finally {
			inputStream.close();
		}

		return string;
	}

	public static String toString(InputStream input, Charset encoding) throws IOException {
		StringBuilderWriter sw = new StringBuilderWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}

	public static void copy(InputStream input, Writer output, Charset charset) throws IOException {
		InputStreamReader in = new InputStreamReader(input, charset);
		copy(in, output);
	}

	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		return count > 2147483647L ? -1 : (int) count;
	}

	public static long copyLarge(Reader input, Writer output) throws IOException {
		return copyLarge(input, output, new char[4096]);
	}

	public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
		long count;
		int n;
		for (count = 0L; -1 != (n = input.read(buffer)); count += n) {
			output.write(buffer, 0, n);
		}
		return count;
	}

	@Nonnull
	@CheckReturnValue
	public static HttpURLConnection createConnection(final @Nonnull String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		return connection;
	}

	@Nonnull
	@CheckReturnValue
	public static HttpURLConnection createConnection(final @Nonnull String url, final @Nonnull Map<String, String> header) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		connection.setRequestProperty("Content-Type", "application/json");
		for (Entry<String, String> entry : header.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}
		return connection;
	}

}

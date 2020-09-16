package net.codingarea.botmanager.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class Fonts {

	private Fonts() { }

	@Nonnull
	@CheckReturnValue
	public static Font loadFile(@Nonnull String path, float size) throws IOException, FontFormatException {
		return loadFile(path, size, Font.PLAIN);
	}

	@Nonnull
	@CheckReturnValue
	public static Font loadFile(@Nonnull String path, float size, int style) throws IOException, FontFormatException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(style, size);
		register(font);
		return font;
	}

	@Nonnull
	@CheckReturnValue
	public static Font loadResource(@Nonnull String path, float size) throws IOException, FontFormatException {
		return loadResource(path, size, Font.PLAIN);
	}

	@Nonnull
	@CheckReturnValue
	public static Font loadResource(@Nonnull String path, float size, int style) throws IOException, FontFormatException {
		InputStream stream = Fonts.class.getClassLoader().getResourceAsStream(path);
		if (stream == null) throw new FileNotFoundException("Could not find resource " + path);
		return load(stream, size, style);
	}

	@Nonnull
	@CheckReturnValue
	public static Font load(@Nonnull InputStream stream, float size, int style) throws IOException, FontFormatException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(style, size);
		register(font);
		return font;
	}

	public static void register(@Nonnull Font font) {
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		environment.registerFont(font);
	}

}

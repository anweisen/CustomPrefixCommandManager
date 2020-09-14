package net.codingarea.botmanager.utils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class Fonts {

	private Fonts() { }

	public static Font loadFile(String path, float size) throws IOException, FontFormatException {
		return loadFile(path, size, Font.PLAIN);
	}

	public static Font loadFile(String path, float size, int style) throws IOException, FontFormatException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(style, size);
		register(font);
		return font;
	}

	public static Font loadResource(String path, float size) throws IOException, FontFormatException {
		return loadResource(path, size, Font.PLAIN);
	}

	public static Font loadResource(String path, float size, int style) throws IOException, FontFormatException {
		InputStream stream = Fonts.class.getClassLoader().getResourceAsStream(path);
		return load(stream, size, style);
	}

	public static Font load(InputStream stream, float size, int style) throws IOException, FontFormatException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(style, size);
		register(font);
		return font;
	}

	public static void register(@Nonnull Font font) {
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		environment.registerFont(font);
	}

}

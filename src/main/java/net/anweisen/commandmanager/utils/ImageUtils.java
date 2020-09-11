package net.anweisen.commandmanager.utils;

import net.anweisen.commandmanager.exceptions.MessageException;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class ImageUtils {

	private ImageUtils() { }

	public static void post(@Nonnull RenderedImage image, @Nonnull MessageChannel channel, @Nonnull String fileName, @Nonnull String fileType) throws IOException {

		fileName += fileType;

		File folder = new File("./temp");
		if (!folder.exists()) folder.mkdir();
		File file = new File(folder, "image-" + System.currentTimeMillis() + "-" + channel.getId());

		ImageIO.write(image, fileType, file);

		channel.sendFile(file, fileName).queue(message -> file.delete(), MessageException::create);

	}

	/**
	 * @param height The y-position of the text
	 * @return The width of the text added in pixels
	 */
	public static int addCenteredText(@Nonnull Graphics2D graphics, @Nonnull String text, int height) {

		TextLayout layout = getTextLayout(graphics, text);
		int lineWidth = (int) layout.getBounds().getWidth();

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.drawString(text, graphics.getClipBounds().width / 2 - lineWidth / 2, height);

		return lineWidth;

	}

	/**
	 * @param height The y-position of the text
	 */
	public static void addTextEndingAtMid(@Nonnull Graphics2D graphics, @Nonnull String text, int height) {

		TextLayout layout = getTextLayout(graphics, text);
		int lineWidth = (int) layout.getBounds().getWidth();

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.drawString(text, graphics.getClipBounds().width / 2 - lineWidth, height);

	}

	/**
	 * Reads a image from url using {@link ImageIO#read(InputStream)} and returns it
	 * @param request The URL the image is stored to
	 * @throws IOException When something goes wrong while connecting or reading the image
	 */
	public static BufferedImage getImage(@Nonnull String request) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(request).openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		return ImageIO.read(connection.getInputStream());
	}

	@Nonnull
	public static TextLayout getTextLayout(@Nonnull Graphics2D graphics, @Nonnull String text) {
		return new TextLayout(text, graphics.getFont(), graphics.getFontRenderContext());
	}

	public static void darkerImage(@Nonnull BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				image.setRGB(i, j, new Color(image.getRGB(i, j)).darker().getRGB());
			}
		}
	}

}

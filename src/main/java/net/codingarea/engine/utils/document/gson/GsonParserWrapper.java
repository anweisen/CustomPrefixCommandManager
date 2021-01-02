package net.codingarea.engine.utils.document.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.Reader;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 0.0.1
 */
public final class GsonParserWrapper {

	public static JsonElement parseReader(Reader json) {
		try {
			return JsonParser.parseReader(json);
		} catch (Error ex) {
			return new JsonParser().parse(json);
		}
	}

	public static JsonElement parseReader(JsonReader json) {
		try {
			return JsonParser.parseReader(json);
		} catch (Error ex) {
			return new JsonParser().parse(json);
		}
	}

	public static JsonElement parseString(String json) {
		try {
			return JsonParser.parseString(json);
		} catch (Error ex) {
			return new JsonParser().parse(json);
		}
	}

}

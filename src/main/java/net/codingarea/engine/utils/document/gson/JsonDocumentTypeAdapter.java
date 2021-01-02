package net.codingarea.engine.utils.document.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author Tareko | https://github.com/CloudNetService/CloudNet-v3/blob/master/cloudnet-common/src/main/java/de/dytanic/cloudnet/common/document/gson/JsonDocumentTypeAdapter.java
 * @since 0.0.1
 */
public class JsonDocumentTypeAdapter extends TypeAdapter<JsonDocument> {

	@Override
	public void write(JsonWriter jsonWriter, JsonDocument document) throws IOException {
		TypeAdapters.JSON_ELEMENT.write(jsonWriter, document == null ? new JsonObject() : document.jsonObject);
	}

	@Override
	public JsonDocument read(JsonReader jsonReader) throws IOException {
		JsonElement jsonElement = TypeAdapters.JSON_ELEMENT.read(jsonReader);
		if (jsonElement != null && jsonElement.isJsonObject()) {
			return new JsonDocument(jsonElement);
		} else {
			return null;
		}
	}

}

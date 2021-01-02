package net.codingarea.engine.utils.document.gson;

/**
 * @author CloudNet-v3 | https://github.com/CloudNetService/CloudNet-v3/blob/master/cloudnet-common/src/main/java/de/dytanic/cloudnet/common/document/gson/BasicJsonDocPropertyable.java
 * @since 0.0.1
 */
public class BasicJsonDocPropertyable implements IJsonDocPropertyable {

	protected JsonDocument properties = new JsonDocument();

	@Override
	public <E> IJsonDocPropertyable setProperty(JsonDocProperty<E> docProperty, E val) {
		this.properties.setProperty(docProperty, val);
		return this;
	}

	@Override
	public <E> E getProperty(JsonDocProperty<E> docProperty) {
		return this.properties.getProperty(docProperty);
	}

	@Override
	public <E> IJsonDocPropertyable removeProperty(JsonDocProperty<E> docProperty) {
		properties.removeProperty(docProperty);
		return this;
	}

	@Override
	public <E> boolean hasProperty(JsonDocProperty<E> docProperty) {
		return docProperty.tester.test(this.properties);
	}

	@Override
	public JsonDocument getProperties() {
		return this.properties;
	}

}

package net.codingarea.engine.utils.document.gson;

/**
 * @author Tareko | https://github.com/CloudNetService/CloudNet-v3/blob/master/cloudnet-common/src/main/java/de/dytanic/cloudnet/common/document/gson/IJsonDocPropertyable.java
 * @since 0.0.1
 */
public interface IJsonDocPropertyable {

	<E> IJsonDocPropertyable setProperty(JsonDocProperty<E> docProperty, E val);

	<E> E getProperty(JsonDocProperty<E> docProperty);

	<E> IJsonDocPropertyable removeProperty(JsonDocProperty<E> docProperty);

	<E> boolean hasProperty(JsonDocProperty<E> docProperty);

	JsonDocument getProperties();

}

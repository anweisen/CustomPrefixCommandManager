package net.codingarea.engine.utils.document;

import net.codingarea.engine.utils.config.Config;
import net.codingarea.engine.utils.document.gson.IJsonDocPropertyable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * A document is a wrapper to persistence data or read data in the heap or
 * easy into the following implementation format of this interface.
 *
 * @author CloudNet-v3 | https://github.com/CloudNetService/CloudNet-v3/blob/master/cloudnet-common/src/main/java/de/dytanic/cloudnet/common/document/IDocument.java
 * @since 0.0.1
 */
public interface IDocument<Document extends IDocument<?>> extends Config, IJsonDocPropertyable, Serializable, IPersistable, IReadable, Iterable<String> {


	@Nonnull
	@CheckReturnValue
	Collection<String> keys();

	@CheckReturnValue
	int size();

	@Nonnull
	@Override
	Document clear();

	@Nonnull
	@Override
	Document remove(@Nullable String key);

	@CheckReturnValue
	boolean contains(String key);

	<T> T toInstanceOf(Class<T> clazz);

	<T> T toInstanceOf(Type clazz);


	Document append(String key, @Nullable Object value);

	Document append(String key, @Nullable Number value);

	Document append(String key, @Nullable  Boolean value);

	Document append(String key, @Nullable String value);

	Document append(String key, @Nullable Character value);

	Document append(String key, Document value);

	Document append(Properties properties);

	Document append(Map<String, Object> map);

	Document append(String key, @Nonnull Properties properties);

	Document append(String key, byte[] bytes);

	Document append(Document t);

	Document getDocument(String key);


	@Override
	@CheckReturnValue
	int getInt(@Nonnull String key);

	@Override
	@CheckReturnValue
	double getDouble(@Nonnull String key);

	@Override
	@CheckReturnValue
	float getFloat(@Nonnull String key);

	@Override
	@CheckReturnValue
	byte getByte(@Nonnull String key);

	@Override
	@CheckReturnValue
	short getShort(@Nonnull String key);

	@Override
	@CheckReturnValue
	long getLong(@Nonnull String key);

	@Override
	@CheckReturnValue
	boolean getBoolean(@Nonnull String key);

	@Nullable
	@Override
	@CheckReturnValue
	String getString(@Nonnull String key);

	@Override
	@CheckReturnValue
	char getChar(@Nonnull String key);

	BigDecimal getBigDecimal(String key);

	BigInteger getBigInteger(String key);

	Properties getProperties(String key);

	byte[] getBinary(String key);

	<T> T get(String key, Class<T> clazz);

	<T> T get(String key, Type type);

}

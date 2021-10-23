package io.github.albertus82.jface.i18n;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Widget;

import io.github.albertus82.util.ISupplier;

public class LocalizedWidgets implements Map<Widget, ISupplier<String>> {

	private static final Map<Class<? extends Widget>, Method> setTextMethods = new HashMap<Class<? extends Widget>, Method>();

	private final Map<Widget, ISupplier<String>> wrappedMap;

	/** @see HashMap#HashMap() */
	public LocalizedWidgets() {
		wrappedMap = new HashMap<Widget, ISupplier<String>>();
	}

	/** @see HashMap#HashMap(int) */
	public LocalizedWidgets(final int initialCapacity) {
		wrappedMap = new HashMap<Widget, ISupplier<String>>(initialCapacity);
	}

	@Override
	public ISupplier<String> put(final Widget widget, final ISupplier<String> textSupplier) {
		final Class<? extends Widget> widgetClass = widget.getClass();
		if (!setTextMethods.containsKey(widgetClass)) {
			try {
				setTextMethods.put(widgetClass, widgetClass.getMethod("setText", String.class));
			}
			catch (final NoSuchMethodException e) {
				throw new IllegalArgumentException(String.valueOf(widget), e);
			}
		}
		setText(widget, textSupplier);
		return wrappedMap.put(widget, textSupplier);
	}

	@Override
	public void putAll(final Map<? extends Widget, ? extends ISupplier<String>> m) {
		for (final Entry<? extends Widget, ? extends ISupplier<String>> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public int size() {
		return wrappedMap.size();
	}

	@Override
	public boolean isEmpty() {
		return wrappedMap.isEmpty();
	}

	@Override
	public boolean containsKey(final Object widget) {
		return wrappedMap.containsKey(widget);
	}

	@Override
	public boolean containsValue(final Object textSupplier) {
		return wrappedMap.containsValue(textSupplier);
	}

	@Override
	public ISupplier<String> get(final Object widget) {
		return wrappedMap.get(widget);
	}

	@Override
	public ISupplier<String> remove(final Object widget) {
		return wrappedMap.remove(widget);
	}

	@Override
	public void clear() {
		wrappedMap.clear();
	}

	@Override
	public Set<Widget> keySet() {
		return wrappedMap.keySet();
	}

	@Override
	public Collection<ISupplier<String>> values() {
		return wrappedMap.values();
	}

	@Override
	public Set<Entry<Widget, ISupplier<String>>> entrySet() {
		return wrappedMap.entrySet();
	}

	public <T extends Widget> Entry<T, ISupplier<String>> putAndReturn(final T widget, final ISupplier<String> textSupplier) {
		put(widget, textSupplier);
		return new SimpleEntry<T, ISupplier<String>>(widget, textSupplier);
	}

	public void resetText(final Widget widget) {
		setText(widget, wrappedMap.get(widget));
	}

	public void resetAllTexts() {
		for (final Entry<Widget, ISupplier<String>> entry : wrappedMap.entrySet()) {
			setText(entry.getKey(), entry.getValue());
		}
	}

	private static void setText(final Widget widget, final ISupplier<String> textSupplier) {
		if (textSupplier != null && widget != null && !widget.isDisposed()) {
			try {
				setTextMethods.get(widget.getClass()).invoke(widget, String.valueOf(textSupplier.get()));
			}
			catch (final IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
			catch (final InvocationTargetException e) {
				throw new IllegalStateException(e);
			}
		}
	}

}

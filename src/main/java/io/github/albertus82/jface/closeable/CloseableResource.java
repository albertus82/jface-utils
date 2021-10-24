package io.github.albertus82.jface.closeable;

import java.io.Closeable;

import org.eclipse.swt.graphics.Resource;

public class CloseableResource<T extends Resource> implements Closeable {

	private final T resource;

	public CloseableResource(final T resource) {
		this.resource = resource;
	}

	public T getResource() {
		return resource;
	}

	@Override
	public void close() {
		if (resource != null) {
			resource.dispose();
		}
	}

}

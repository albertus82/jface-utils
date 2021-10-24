package it.albertus.jface.closeable;

import java.io.Closeable;

import org.eclipse.swt.graphics.Device;

public class CloseableDevice<T extends Device> implements Closeable {

	private final T device;

	public CloseableDevice(final T device) {
		this.device = device;
	}

	public T getDevice() {
		return device;
	}

	@Override
	public void close() {
		if (device != null) {
			device.dispose();
		}
	}

}

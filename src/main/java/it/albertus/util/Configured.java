package it.albertus.util;

public abstract class Configured<T> {

	public abstract T getValue();

	public T getDefault() {
		return null;
	}

}

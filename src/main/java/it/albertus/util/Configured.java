package it.albertus.util;

public abstract class Configured<T> {

	public abstract T getValue();

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}

}

package it.albertus.util;

public abstract class Localized {

	public abstract String getString();

	@Override
	public String toString() {
		return String.valueOf(getString());
	}

}

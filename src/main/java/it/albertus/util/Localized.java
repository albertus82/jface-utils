package it.albertus.util;

public abstract class Localized {

	public abstract String getMessage();

	@Override
	public String toString() {
		return String.valueOf(getMessage());
	}

}

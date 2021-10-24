package it.albertus.util;

public abstract class Localized extends Supplier<String> implements Comparable<Localized> {

	public abstract String getString();

	@Override
	public final String get() {
		return getString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int compareTo(final Localized o) {
		return this.getString().compareTo(o.getString());
	}

}

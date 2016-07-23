package it.albertus.util;

public abstract class Localized implements Comparable<Localized> {

	public abstract String getString();

	@Override
	public String toString() {
		return String.valueOf(getString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getString() == null) ? 0 : getString().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Localized)) {
			return false;
		}
		Localized other = (Localized) obj;
		if (getString() == null) {
			if (other.getString() != null) {
				return false;
			}
		}
		else if (!getString().equals(other.getString())) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Localized o) {
		return this.getString().compareTo(o.getString());
	}

}

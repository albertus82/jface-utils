package it.albertus.util;

public abstract class Configured<T> {

	public abstract T getValue();

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
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
		if (!(obj instanceof Configured)) {
			return false;
		}
		Configured<?> other = (Configured<?>) obj;
		if (getValue() == null) {
			if (other.getValue() != null) {
				return false;
			}
		}
		else if (!getValue().equals(other.getValue())) {
			return false;
		}
		return true;
	}

}

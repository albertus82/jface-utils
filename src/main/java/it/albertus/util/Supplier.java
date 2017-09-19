package it.albertus.util;

/**
 * Represents a supplier of results.
 * 
 * @param <T> the type of results supplied by this supplier
 */
public abstract class Supplier<T> implements ISupplier<T> {

	@Override
	public String toString() {
		return String.valueOf(get());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((get() == null) ? 0 : get().hashCode());
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
		if (!(obj instanceof Supplier)) {
			return false;
		}
		Supplier<?> other = (Supplier<?>) obj;
		if (get() == null) {
			if (other.get() != null) {
				return false;
			}
		}
		else if (!get().equals(other.get())) {
			return false;
		}
		return true;
	}

}

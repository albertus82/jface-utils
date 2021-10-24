package io.github.albertus82.util;

import javax.annotation.Nullable;

public class EmailUtils {

	private EmailUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static boolean validateAddress(@Nullable String address) {
		if (address == null) {
			return false;
		}
		if (address.startsWith(" ") || address.endsWith(" ")) {
			return false;
		}

		address = address.trim();
		if (address.indexOf('@') == -1 || address.length() < 3) {
			return false;
		}

		final String local = address.substring(0, address.lastIndexOf('@'));
		if (local.trim().isEmpty()) {
			return false;
		}
		if (local.endsWith(" ")) {
			return false;
		}
		if (local.indexOf('@') != -1 && local.indexOf('"') == local.lastIndexOf('"')) {
			return false;
		}

		final String domain = address.substring(address.lastIndexOf('@') + 1);
		if (domain.trim().isEmpty()) {
			return false;
		}
		if (domain.startsWith(".") || domain.endsWith(".")) {
			return false;
		}
		if (domain.startsWith(" ")) {
			return false;
		}
		if (domain.contains("..")) { // NOSONAR Replace this if-then-else statement by a single return statement. Return of boolean expressions should not be wrapped into an "if-then-else" statement (java:S1126)
			return false;
		}

		return true;
	}

}

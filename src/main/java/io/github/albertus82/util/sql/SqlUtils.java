package io.github.albertus82.util.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.albertus82.util.logging.LoggerFactory;

public class SqlUtils {

	private static final Logger log = LoggerFactory.getLogger(SqlUtils.class);

	private SqlUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static void closeQuietly(final Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (final SQLException e) {
			log.log(Level.FINE, "An error occurred while closing the connection:", e);
		}
	}

	public static void closeQuietly(final Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		}
		catch (final SQLException e) {
			log.log(Level.FINE, "An error occurred while closing the statement:", e);
		}
	}

	public static void closeQuietly(final ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		catch (final SQLException e) {
			log.log(Level.FINE, "An error occurred while closing the result set:", e);
		}
	}

	public static String sanitizeName(final String str) {
		return str.replaceAll("[^A-Za-z0-9_]+", "");
	}

	public static String sanitizeType(final String str) {
		return str.replaceAll("[^A-Za-z0-9_,() ]+", "");
	}

}

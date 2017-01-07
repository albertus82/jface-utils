package it.albertus.util.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtils {

	private SqlUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static void closeQuietly(final Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (final SQLException se) {/* Ignore */}
	}

	public static void closeQuietly(final Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		}
		catch (final SQLException se) {/* Ignore */}
	}

	public static void closeQuietly(final ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		catch (final SQLException se) {/* Ignore */}
	}

}

package it.albertus.util.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.util.logging.LoggerFactory;

public class SqlUtils {

	private static final Logger logger = LoggerFactory.getLogger(SqlUtils.class);

	private SqlUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static void closeQuietly(final Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (final SQLException se) {
			logger.log(Level.FINE, se.getLocalizedMessage() != null ? se.getLocalizedMessage() : se.getMessage(), se);
		}
	}

	public static void closeQuietly(final Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		}
		catch (final SQLException se) {
			logger.log(Level.FINE, se.getLocalizedMessage() != null ? se.getLocalizedMessage() : se.getMessage(), se);
		}
	}

	public static void closeQuietly(final ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		catch (final SQLException se) {
			logger.log(Level.FINE, se.getLocalizedMessage() != null ? se.getLocalizedMessage() : se.getMessage(), se);
		}
	}

}

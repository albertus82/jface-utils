package it.albertus.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

public class SqlUtilsTest {

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		// Class.forName("org.h2.Driver");
		final Connection conn = DriverManager.getConnection("jdbc:h2:mem:mytestdb", "sa", "");
		final PreparedStatement st = conn.prepareStatement("SELECT * FROM DUAL");
		final ResultSet rs = st.executeQuery();
		while (rs.next()) {
			Assert.assertEquals(1, rs.getInt(1));
		}
		SqlUtils.closeQuietly(rs);
		SqlUtils.closeQuietly(st);
		SqlUtils.closeQuietly(conn);
	}

}

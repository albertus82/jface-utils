package it.albertus.jface.sysinfo;

import java.lang.management.ManagementPermission;
import java.security.Permission;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import it.albertus.util.logging.LoggerFactory;

public class SystemInformationDialogTest {

	private static final Logger logger = LoggerFactory.getLogger(SystemInformationDialogTest.class);

	@Test
	public void testIsAvailable() {
		if (System.getSecurityManager() != null) {
			logger.log(Level.WARNING, "SecurityManager detected, ignoring test {0}.", getClass());
			return;
		}

		Assert.assertTrue(SystemInformationDialog.isAvailable());

		System.setSecurityManager(new SecurityManager() {
			@Override
			public void checkPropertiesAccess() {
				throw new SecurityException("1");
			}

			@Override
			public void checkPermission(final Permission perm) { /* NOOP */ }
		});
		Assert.assertTrue(SystemInformationDialog.isAvailable());

		System.setSecurityManager(new SecurityManager() {
			@Override
			public void checkPropertiesAccess() { /* NOOP */ }

			@Override
			public void checkPermission(final Permission perm) {
				if (new RuntimePermission("getenv.*").equals(perm)) {
					throw new SecurityException("2");
				}
			}
		});
		Assert.assertTrue(SystemInformationDialog.isAvailable());

		System.setSecurityManager(new SecurityManager() {
			@Override
			public void checkPropertiesAccess() { /* NOOP */ }

			@Override
			public void checkPermission(final Permission perm) {
				if (new ManagementPermission("monitor").equals(perm)) {
					throw new SecurityException("3");
				}
			}
		});
		Assert.assertTrue(SystemInformationDialog.isAvailable());

		System.setSecurityManager(new SecurityManager() {
			@Override
			public void checkPropertiesAccess() { /* NOOP */ }

			@Override
			public void checkPermission(final Permission perm) {
				if (new RuntimePermission("getenv.*").equals(perm) || new ManagementPermission("monitor").equals(perm)) {
					throw new SecurityException("4");
				}
			}
		});
		Assert.assertTrue(SystemInformationDialog.isAvailable());

		System.setSecurityManager(new SecurityManager() {
			@Override
			public void checkPropertiesAccess() {
				throw new SecurityException("5");
			}

			@Override
			public void checkPermission(final Permission perm) {
				if (new RuntimePermission("getenv.*").equals(perm) || new ManagementPermission("monitor").equals(perm)) {
					throw new SecurityException("6");
				}
			}
		});
		Assert.assertFalse(SystemInformationDialog.isAvailable());
	}

}

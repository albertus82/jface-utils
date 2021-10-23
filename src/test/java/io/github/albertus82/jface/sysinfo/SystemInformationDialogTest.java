package io.github.albertus82.jface.sysinfo;

import java.lang.management.ManagementPermission;
import java.security.Permission;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.albertus82.util.logging.LoggerFactory;

public class SystemInformationDialogTest {

	private static final Logger log = LoggerFactory.getLogger(SystemInformationDialogTest.class);

	private static boolean skip = false;

	@BeforeClass
	public static void beforeAll() {
		if (System.getSecurityManager() != null) {
			log.log(Level.WARNING, "SecurityManager detected, ignoring test {0}.", SystemInformationDialogTest.class);
			skip = true;
		}
	}

	@AfterClass
	public static void afterAll() {
		if (!skip) {
			System.setSecurityManager(null);
		}
	}

	@Test
	public void testIsAvailable() {
		if (skip) {
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

package it.albertus.jface.cocoa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

public class CocoaUIEnhancer {

	private static final int kAboutMenuItem = 0;
	private static final int kPreferencesMenuItem = 2;

	private static Callback proc3Args;

	private static long sel_aboutMenuItemSelected_;
	private static long sel_preferencesMenuItemSelected_;
	private static long sel_toolbarButtonClicked_;

	private final Display display;

	public CocoaUIEnhancer(final Display display) {
		this.display = display;
	}

	public void hookApplicationMenu(final Listener quitListener, final Listener aboutListener, final Listener preferencesListener) {
		hookApplicationMenu(quitListener, new ListenerCallbackObject(preferencesListener, aboutListener));
	}

	public void hookApplicationMenu(final Listener quitListener, final IAction aboutAction, final IAction preferencesAction) {
		hookApplicationMenu(quitListener, new ActionCallbackObject(preferencesAction, aboutAction));
	}

	private void hookApplicationMenu(final Listener quitListener, final CallbackObject callbackObject) {
		initialize(callbackObject);

		// Connect the quit/exit menu.
		if (!display.isDisposed() && quitListener != null) {
			display.addListener(SWT.Close, quitListener);
		}

		// Schedule disposal of callback object.
		display.disposeExec(new Runnable() {
			@Override
			public void run() {
				proc3Args.dispose();
			}
		});
	}

	private void initialize(final CallbackObject callbackObject) {
		final Class<?> osCls = classForName("org.eclipse.swt.internal.cocoa.OS");

		// Register names in Objective-C.
		if (sel_toolbarButtonClicked_ == 0) {
			sel_preferencesMenuItemSelected_ = registerName(osCls, "preferencesMenuItemSelected:");
			sel_aboutMenuItemSelected_ = registerName(osCls, "aboutMenuItemSelected:");
		}

		// Create an SWT Callback object that will invoke the actionProc method of our internal callbackObject.
		proc3Args = new Callback(callbackObject, "actionProc", 3);
		final long proc3 = proc3Args.getAddress();
		if (proc3 == 0) {
			SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		}

		final Object object = invoke(osCls, "objc_lookUpClass", new Object[] { "SWTApplicationDelegate" });
		final long cls = convertToLong(object);

		// Add the action callbacks for Preferences and About menu items.
		invoke(osCls, "class_addMethod", new Object[] { wrapPointer(cls), wrapPointer(sel_preferencesMenuItemSelected_), wrapPointer(proc3), "@:@" });
		invoke(osCls, "class_addMethod", new Object[] { wrapPointer(cls), wrapPointer(sel_aboutMenuItemSelected_), wrapPointer(proc3), "@:@" });

		final Class<?> nsapplicationCls = classForName("org.eclipse.swt.internal.cocoa.NSApplication");
		final Class<?> nsmenuCls = classForName("org.eclipse.swt.internal.cocoa.NSMenu");

		// Get the Mac OS X Application menu.
		final Object sharedApplication = invoke(nsapplicationCls, "sharedApplication");
		final Object mainMenu = invoke(sharedApplication, "mainMenu");
		final Object mainMenuItem = invoke(nsmenuCls, mainMenu, "itemAtIndex", new Number[] { wrapPointer(0) });
		final Object appMenu = invoke(mainMenuItem, "submenu");

		final Object aboutMenuItem = invoke(nsmenuCls, appMenu, "itemAtIndex", new Number[] { wrapPointer(kAboutMenuItem) });
		final Object prefMenuItem = invoke(nsmenuCls, appMenu, "itemAtIndex", new Number[] { wrapPointer(kPreferencesMenuItem) });

		final Class<?> nsmenuitemCls = classForName("org.eclipse.swt.internal.cocoa.NSMenuItem");

		invoke(nsmenuitemCls, prefMenuItem, "setAction", new Number[] { wrapPointer(sel_preferencesMenuItemSelected_) });
		invoke(nsmenuitemCls, aboutMenuItem, "setAction", new Number[] { wrapPointer(sel_aboutMenuItemSelected_) });
	}

	private Class<?> classForName(final String className) {
		try {
			final Class<?> cls = Class.forName(className);
			return cls;
		}
		catch (final ClassNotFoundException cnfe) {
			throw new RuntimeException(cnfe);
		}
	}

	private long convertToLong(final Object object) {
		if (object instanceof Integer) {
			final Integer i = (Integer) object;
			return i.longValue();
		}
		if (object instanceof Long) {
			final Long l = (Long) object;
			return l.longValue();
		}
		return 0;
	}

	private static Object invoke(final Class<?> clazz, final Object target, final String methodName, final Object[] args) {
		try {
			final Class<?>[] signature = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				final Class<?> thisClass = args[i].getClass();
				if (thisClass == Integer.class) {
					signature[i] = int.class;
				}
				else if (thisClass == Long.class) {
					signature[i] = long.class;
				}
				else if (thisClass == Byte.class) {
					signature[i] = byte.class;
				}
				else if (thisClass == Boolean.class) {
					signature[i] = boolean.class;
				}
				else {
					signature[i] = thisClass;
				}
			}
			final Method method = clazz.getMethod(methodName, signature);
			return method.invoke(target, args);
		}
		catch (final IllegalAccessException iae) {
			throw new RuntimeException(iae);
		}
		catch (final InvocationTargetException ite) {
			throw new RuntimeException(ite);
		}
		catch (final NoSuchMethodException nsme) {
			throw new RuntimeException(nsme);
		}
	}

	private static Object invoke(final Class<?> clazz, final String methodName, final Object[] args) {
		return invoke(clazz, null, methodName, args);
	}

	private static Number wrapPointer(final long value) {
		final Class<?> ptrClass = C.PTR_SIZEOF == 8 ? long.class : int.class;
		if (ptrClass == long.class) {
			return Long.valueOf(value);
		}
		else {
			return Integer.valueOf((int) value);
		}
	}

	private Object invoke(final Class<?> cls, final String methodName) {
		return invoke(cls, methodName, (Class<?>[]) null, (Object[]) null);
	}

	private Object invoke(final Class<?> cls, final String methodName, final Class<?>[] paramTypes, final Object... arguments) {
		try {
			final Method m = cls.getDeclaredMethod(methodName, paramTypes);
			return m.invoke(null, arguments);
		}
		catch (final IllegalAccessException iae) {
			throw new RuntimeException(iae);
		}
		catch (final InvocationTargetException ite) {
			throw new RuntimeException(ite);
		}
		catch (final NoSuchMethodException nsme) {
			throw new RuntimeException(nsme);
		}
	}

	private Object invoke(final Object obj, final String methodName) {
		return invoke(obj, methodName, (Class<?>[]) null, (Object[]) null);
	}

	private Object invoke(final Object obj, final String methodName, final Class<?>[] paramTypes, final Object... arguments) {
		try {
			final Method m = obj.getClass().getDeclaredMethod(methodName, paramTypes);
			return m.invoke(obj, arguments);
		}
		catch (final IllegalAccessException iae) {
			throw new RuntimeException(iae);
		}
		catch (final InvocationTargetException ite) {
			throw new RuntimeException(ite);
		}
		catch (final NoSuchMethodException nsme) {
			throw new RuntimeException(nsme);
		}
	}

	private long registerName(final Class<?> osCls, final String name) {
		final Object object = invoke(osCls, "sel_registerName", new Object[] { name });
		return convertToLong(object);
	}

	private abstract class CallbackObject {
		static final long RETURN_VALUE = 99;

		@SuppressWarnings("unused")
		int actionProc(final int id, final int sel, final int arg0) {
			// Casts the parameters to long so and use the method for 64 bit Cocoa.
			return (int) actionProc((long) id, (long) sel, (long) arg0);
		}

		abstract long actionProc(final long id, final long sel, final long arg0);
	}

	private class ListenerCallbackObject extends CallbackObject {
		private final Listener preferencesListener;
		private final Listener aboutListener;

		private ListenerCallbackObject(final Listener preferencesListener, final Listener aboutListener) {
			this.preferencesListener = preferencesListener;
			this.aboutListener = aboutListener;
		}

		@Override
		long actionProc(final long id, final long sel, final long arg0) {
			if (sel == sel_aboutMenuItemSelected_ && aboutListener != null) {
				aboutListener.handleEvent(null);
			}
			else if (sel == sel_preferencesMenuItemSelected_ && preferencesListener != null) {
				preferencesListener.handleEvent(null);
			}
			return RETURN_VALUE;
		}
	}

	private class ActionCallbackObject extends CallbackObject {
		private final IAction preferencesAction;
		private final IAction aboutAction;

		private ActionCallbackObject(final IAction preferencesAction, final IAction aboutAction) {
			this.preferencesAction = preferencesAction;
			this.aboutAction = aboutAction;
		}

		@Override
		long actionProc(final long id, final long sel, final long arg0) {
			if (sel == sel_aboutMenuItemSelected_ && aboutAction != null) {
				aboutAction.run();
			}
			else if (sel == sel_preferencesMenuItemSelected_ && preferencesAction != null) {
				preferencesAction.run();
			}
			return RETURN_VALUE;
		}
	}

}

package dev.rdh.quark.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class JavaUtils {
	private JavaUtils() {
	}

	public static final String JAVA_HOME;
	public static final String JAVA_VERSION = System.getProperty("java.version");
	public static final String JAVA_VENDOR = System.getProperty("java.vendor");

	public static final int JAVA_VERSION_MAJOR = Integer.parseInt(JAVA_VERSION.split("\\.")[0]);

	static {
		String javaHome = System.getProperty("java.home");
		if (javaHome.endsWith("jre") && JAVA_VERSION_MAJOR <= 8) {
			JAVA_HOME = Paths.get(javaHome).getParent().toString();
		} else {
			JAVA_HOME = javaHome;
		}
	}

	//IBM's JDK on AIX uses strange locations for the executables
	private static final boolean IS_IBM_JDK = Files.exists(Paths.get(JAVA_HOME, "jre", "sh"));

	/**
	 * Get the path to the Java binary with the given name, that is associated with the currently running JVM.
	 * @param binName The name of the binary to get the path for.
	 */
	public static Path getJavaBinary(String binName) {
		if (IS_IBM_JDK) {
			return Paths.get(JAVA_HOME, "jre", "sh", binName).toAbsolutePath();
		} else {
			return Paths.get(JAVA_HOME, "bin", binName).toAbsolutePath();
		}
	}
}

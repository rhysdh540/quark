package dev.rdh.quark.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class JavaUtils {
	private JavaUtils() {
	}

	public static final String JAVA_HOME = System.getProperty("java.home");

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

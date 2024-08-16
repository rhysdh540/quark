package dev.rdh.quark.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PathUtils {
	private PathUtils() {
	}

	public static Set<Path> getFiles(Path root) {
		return getChildren(root, s -> s.filter(Files::isRegularFile));
	}

	public static Set<Path> getChildren(Path root, UnaryOperator<Stream<Path>> transformer) {
		try(Stream<Path> stream = Files.walk(root)) {
			return transformer.apply(stream)
				.collect(Collectors.toSet());
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static void deleteRecursively(Path root) {
		try(Stream<Path> stream = Files.walk(root)) {
			stream
				.sorted((p1, p2) -> -p1.compareTo(p2)) // sort so that directories are deleted after their contents
				.forEach(p -> {
					try {
						Files.delete(p);
					} catch(IOException e) {
						throw new UncheckedIOException(e);
					}
				});
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static Set<Path> getDirectChildren(Path root) {
		return getChildren(root, s -> s.filter(p -> p.getParent().equals(root)));
	}
}

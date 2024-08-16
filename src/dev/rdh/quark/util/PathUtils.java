package dev.rdh.quark.util;

import dev.rdh.quark.util.exception.Exceptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.rdh.quark.util.exception.Exceptions.asUnchecked;

public final class PathUtils {
	private PathUtils() {
	}

	/**
	 * Get all files in the given directory, not including directories. The set will be ordered by the order in which
	 * {@link Files#walk(Path, java.nio.file.FileVisitOption...) Files.walk} returns the files; that is, depth-first.
	 * @param root the directory to search
	 * @return a set of all files in the directory
	 */
	public static Set<Path> getFiles(Path root) {
		return getChildren(root, s -> s.filter(Files::isRegularFile));
	}

	/**
	 * Get all of the children of the given directory, including files and directories. The set will be ordered by the order in
	 * which {@link Files#walk(Path, java.nio.file.FileVisitOption...) Files.walk} returns the files; that is, depth-first.
	 * @param root the directory to search
	 * @param transformer a function to transform the stream of paths before collecting them
	 * @return a set of all children of the directory
	 */
	public static Set<Path> getChildren(Path root, UnaryOperator<Stream<Path>> transformer) {
		try(Stream<Path> stream = Files.walk(root)) {
			return transformer.apply(stream)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		} catch(IOException e) {
			throw asUnchecked(e);
		}
	}

	/**
	 * Delete the given directory and all of its contents. This method will delete the directory itself, as well as all of its
	 * children, recursively. The directories will be deleted in reverse order of their depth, so that the contents of a
	 * directory are deleted before the directory itself.
	 * @param root the directory to delete
	 */
	public static void deleteRecursively(Path root) {
		try(Stream<Path> stream = Files.walk(root)) {
			stream.sorted((p1, p2) -> -p1.compareTo(p2)) // sort so that directories are deleted after their contents
					.forEach(Exceptions.uncheckC(Files::delete));
		} catch(IOException e) {
			throw asUnchecked(e);
		}
	}
}

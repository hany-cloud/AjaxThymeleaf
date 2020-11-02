package net.hka.common.util;

import java.util.stream.StreamSupport;

/**
 * A general utility class that is working on {@Link java.lang.Iterable<T>} interface.
 * @author Hany Kamal
 *
 */
public class IterableUtil {
	
	/**
	 * Return the size of any class that is implements {@Link java.lang.Iterable<T>} interface
	 * @param <T> 
	 * @param iterable {@Link java.lang.Iterable<T>} interface
	 * @return number of elements held by the {@Link java.lang.Iterable<T>} interface
	 */
	public static <T> long size(final Iterable<T> iterable) {
		return iterable == null ? 0 : StreamSupport.stream(iterable.spliterator(), false).count();
	}
}

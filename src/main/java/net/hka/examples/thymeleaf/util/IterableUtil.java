package net.hka.examples.thymeleaf.util;

import java.util.stream.StreamSupport;

public class IterableUtil {
	public static <T> long size(final Iterable<T> iterable) {
		return iterable==null?0:StreamSupport.stream(iterable.spliterator(), false).count();
	}
}

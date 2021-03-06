package com.wobserver.vcollections.keygenerators;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A common interface every generator must contain.
 * @param <T> The type of the key generated by the generator
 */
@FunctionalInterface
public interface IKeyGenerator<T>  extends Supplier<T> {
	/**
	 * Assign a tester to the generator, which will test the generated key
	 * @param tester
	 */
	default void setup(Predicate<T> tester) {

	}
}

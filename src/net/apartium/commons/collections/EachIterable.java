package net.apartium.commons.collections;

import java.util.function.Consumer;

public interface EachIterable<T> extends Iterable<T> {
	
	void forEachRemoved(Consumer<? super T> arg0);
	
	void forEachAdded(Consumer<? super T> arg0);
	
}

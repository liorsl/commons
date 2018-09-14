package net.apartium.commons.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

public class ConcurrentHashSet<E> implements Set<E> {
	
	final Set<E>
			set = Sets.newConcurrentHashSet();
	
	@Override
	public Iterator<E> iterator() {
		return set.iterator();
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean add(E arg0) {
		return set.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		return set.addAll(arg0);
	}

	@Override
	public void clear() {
		set.clear();
		
	}

	@Override
	public boolean contains(Object arg0) {
		return set.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return set.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean remove(Object arg0) {
		return set.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return set.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return set.retainAll(arg0);
	}

	@Override
	public Object[] toArray() {
		return set.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return set.toArray(arg0);
	}

}

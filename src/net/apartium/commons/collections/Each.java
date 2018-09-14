package net.apartium.commons.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import lombok.Getter;

public class Each<E> extends AbstractCollection<E> implements EachList<E>, EachSet<E>, Cloneable {
	
	@Getter
	Collection<E>
			coll;
	
	List<Consumer<? super E>>
			forEachRemoved = Lists.newCopyOnWriteArrayList(),
			forEachAdded = Lists.newCopyOnWriteArrayList();
	
	@Getter
	boolean
			immutable;
	
	public Each(Collection<E> collection) {
		this.coll = collection;
	}
	
	boolean isList() {
		return this.coll instanceof List;
	}
	
	@Override
	public Iterator<E> iterator() {
		return this.coll.iterator();
	}

	@Override
	public int size() {
		return this.coll.size();
	}

	@Override
	public void forEachRemoved(Consumer<? super E> arg0) {
		forEachRemoved.add(arg0);
		
	}

	@Override
	public void forEachAdded(Consumer<? super E> arg0) {
		forEachAdded.add(arg0);
		
	}

	@Override
	public void add(int arg0, E arg1) {
		if (isList() || isImmutable()) throw new UnsupportedOperationException();

		for (Consumer<? super E> cons : this.forEachAdded)
			cons.accept(arg1);
		
		((List<E>) this.coll).add(arg0, arg1);
		
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		if (isList() || isImmutable()) throw new UnsupportedOperationException();
		
		for (Consumer<? super E> cons : this.forEachAdded)
			for (E e : arg1)
				cons.accept(e);
		
		return ((List<E>) this.coll).addAll(arg0, arg1);
	}

	@Override
	public E get(int arg0) {
		if (isList()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).get(arg0);
	}

	@Override
	public int indexOf(Object arg0) {
		if (isList()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).indexOf(arg0);
	}

	@Override
	public int lastIndexOf(Object arg0) {
		if (isList()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).lastIndexOf(arg0);
	}

	@Override
	public ListIterator<E> listIterator() {
		if (isList()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int arg0) {
		if (isList()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).listIterator(arg0);
	}

	@Override
	public E remove(int arg0) {
		if (isList() || isImmutable()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).remove(arg0);
	}

	@Override
	public E set(int arg0, E arg1) {
		if (isList() || isImmutable()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).set(arg0, arg1);
	}

	@Override
	public List<E> subList(int arg0, int arg1) {
		if (isList()) throw new UnsupportedOperationException();
		
		return ((List<E>) this.coll).subList(arg0, arg1);
	}

	@Override
	public Spliterator<E> spliterator() {
		return EachList.super.spliterator();
	}
	
	@Override
	public boolean add(E arg0) {
		if (isImmutable()) throw new UnsupportedOperationException();
		
		return super.add(arg0);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		if (isImmutable()) throw new UnsupportedOperationException();

		return super.addAll(arg0);
	}
	
	@Override
	public boolean remove(Object arg0) {
		if (isImmutable()) throw new UnsupportedOperationException();

		return super.remove(arg0);
	}
	
	@Override
	public boolean removeAll(Collection<?> arg0) {
		if (isImmutable()) throw new UnsupportedOperationException();

		return super.removeAll(arg0);
	}
		
}

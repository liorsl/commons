package net.apartium.commons;

public class Container<T> {

	volatile T
			object;
	
	public Container(T object) {
		this.object = object;
		
	}
	
	public synchronized void setObject(T object) {
		this.object = object;
		
	}
	
	public T getObject() {
		return this.object;
	}
	
}

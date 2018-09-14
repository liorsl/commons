package net.apartium.commons;

public class Returns {
	
	public static class DoubleContainer<A, B> {
		
		final A
				a;
		
		final B
				b;
		
		private DoubleContainer(A a, B b) {
			this.a = a;
			this.b = b;
			
		}
		
		public A getA() {return this.a;}
		
		public B getB() {return this.b;}
		
	}
	
	public static class TripleContainer<A, B, C> extends DoubleContainer<A, B> {

		final C
				c;
		
		private TripleContainer(A a, B b, C c) {
			super(a, b);
			
			this.c = c;
		}
		
		public C getC() {return this.c;}
		
	}
	
}

package net.apartium.commons.tables;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;

public interface Table extends Iterable<Table.Row> {
	
	public static final Object EMPTY = new Object();
	
	Row[] getRows();
	
	Column<?>[] getColumns();
	
	int getMaxCapacity();
	
	Row[] find(List<Object> query);
	
	Row put(List<Object> row);
	
	Row[] remove(List<Object> query);
	
	void remove(Row row);
	
	public class Row extends CopyOnWriteArrayList<Object> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1309524827932153968L;
		
		public boolean match(List<Object> list) {
			for (int i = 0; i < list.size(); i++) {
				Object object = list.get(i);
				if (object == null) continue;
				
				if (get(i) != list.get(i)) return false;
			}
			
			return true;
		}
		
	}
	
	public static class Column<T> {
		
		@Getter
		final Class<T>
				type;
		
		@Getter
		final boolean
				unique;
		
		public Column(Class<T> type, boolean unique) {
			this.type = type;
			this.unique = unique;
			
		}
		
	}
	
}

package net.apartium.commons.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;

import lombok.Getter;

public class SimpleTable implements Table {
	
	@Getter
	Row[]
			rows;
	
	@Getter
	Column<?>[]
			columns;
	
	@Getter
	int
			maxCapacity;
	
	public SimpleTable() {
		
	}
	

	@Override
	public Row[] find(List<Object> query) {
		List<Row> list = new ArrayList<>();
		for (Row row : this) 
			if (row.match(query)) list.add(row);
		
		return list.toArray(new Row[list.size()]);
	}
	
	protected Row createRow(List<Object> query) {
		return null;
	}


	@Override
	public Row put(List<Object> row) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Row[] remove(List<Object> query) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void remove(Row row) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Iterator<Row> iterator() {
		return Arrays.asList(this.rows).iterator();
	}

}

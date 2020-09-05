package com.ahjo.jdbc.dumper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Row {
	private final List<String> columnList;
	private final List<String> valueList;

	public Row() {
		columnList = new ArrayList<>();
		valueList = new ArrayList<>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Row row = (Row) o;
		return Objects.equals(columnList, row.columnList)
				&& Objects.equals(valueList, row.valueList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(columnList, valueList);
	}

	public Row put(String key, String value) {
		columnList.add(key);
		valueList.add(value);
		return this;
	}

	private String listToString(Iterable<String> iterable) {
		return String.join(", ", iterable);
	}

	public String keyToString() {
		return listToString(columnList);
	}

	public String valueToString() {
		return listToString(valueList);
	}
}

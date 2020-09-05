package com.ahjo.jdbc.dumper;

import java.util.Objects;

public class ColumnInfo {
	private final String columnName;
	private final DataType dataType;

	public ColumnInfo(String columnName, DataType dataType) {
		this.columnName = columnName;
		this.dataType = dataType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ColumnInfo that = (ColumnInfo) o;
		return Objects.equals(columnName, that.columnName)
				&& dataType == that.dataType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(columnName, dataType);
	}

	public enum DataType {
		STRING
	}

	public String getColumnName() {
		return columnName;
	}

	public DataType getDataType() {
		return dataType;
	}
}

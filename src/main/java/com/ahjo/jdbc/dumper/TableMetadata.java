package com.ahjo.jdbc.dumper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableMetadata {
	private final String tableName;
	private final List<ColumnInfo> columnInfos;
	private final Map<String, Object> optionMap;

	public TableMetadata(String tableName) {
		this.tableName = tableName;
		this.columnInfos = new ArrayList<>();
		this.optionMap = new HashMap<>();
	}

	public void addColumnInfo(ColumnInfo columnInfo) {
		this.columnInfos.add(columnInfo);
	}

	public void addColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos.addAll(columnInfos);
	}

	public void putOption(String key, Object value) {
		optionMap.put(key, value);
	}
}

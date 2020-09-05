package com.ahjo.jdbc.dumper.parser;

import com.ahjo.jdbc.dumper.ColumnInfo;
import com.ahjo.jdbc.dumper.Row;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultParser implements Parser {
	private final TypeMatcher typeMatcher;

	public DefaultParser(TypeMatcher typeMatcher) {
		this.typeMatcher = typeMatcher;
	}

	@Override
	public List<ColumnInfo> parseColumnInfo(ResultSet resultSet) {
		List<ColumnInfo> columnInfos = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String columnName = resultSet.getString("COLUMN_NAME");
				ColumnInfo.DataType dataType = typeMatcher.match(resultSet.getString("DATA_TYPE"));
				columnInfos.add(new ColumnInfo(columnName, dataType));
			}
		} catch (SQLException e) {

		}
		return columnInfos;
	}

	@Override
	public List<Row> parseRow(ResultSet resultSet) {
		List<Row> rows = new ArrayList<>();
		try {
			while (resultSet.next()) {
				Row row = new Row();
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				int columnCount = resultSetMetaData.getColumnCount();
				for (int i = 0; i < columnCount; i++) {
					row.put(resultSetMetaData.getColumnName(i), resultSet.getString(i));
				}
				rows.add(row);
			}
		} catch (SQLException e) {

		}
		return rows;
	}

	@Override
	public List<String> parseTableNames(ResultSet resultSet) {
		List<String> tableNames = new ArrayList<>();
		try {
			while (resultSet.next()) {
				tableNames.add(resultSet.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {

		}
		return tableNames;
	}
}
